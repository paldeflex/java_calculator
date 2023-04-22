import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel {
    private String[] symbols = {"C", "±", "%", "+", "7", "8", "9", "−", "4", "5", "6", "×", "1", "2", "3", "÷", "0", ".", "="};
    private JButton[] btns = new JButton[symbols.length];

    ButtonPanel(ActionListener listener) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Определяет, каким образом компонент будет растягиваться в ячейке.
        gbc.weightx = gbc.weighty = 1; // Распределние пространства между компонентами
        gbc.insets = new Insets(2, 2, 2, 2);

        for (int i = 0; i < btns.length; i++) {
            gbc.gridx = i % 4; // Когда i равно 0, 4, 8 и т.д., компонент будет находиться в первом столбце, а когда i равно 3, 7, 11 и т.д. - в четвертом столбце.
            gbc.gridy = i / 4; // Когда i равно 0, 1, 2, 3, компонент будет находиться в первой строке, когда i равно 4, 5, 6, 7 - во второй строке и т.д.
            gbc.gridwidth = (i == symbols.length - 1) ? GridBagConstraints.REMAINDER : 1; // Растягивает последнюю кнопку до конца панели

            btns[i] = new CustomButton(symbols[i], 18f, listener);
            add(btns[i], gbc);

            if (symbols[i].matches("[0-9.]")) { // Проверяем, не является ли символ цифрой или точкой
                btns[i].setForeground(new Color(0x898989));
            }
        }

    }
}