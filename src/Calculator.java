import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator extends JFrame implements ActionListener {
    private final ImageIcon icon = new ImageIcon("src/icon.png");
    private final String[] symbols = {"7", "8", "9", "+", "4", "5", "6", "-", "1", "2", "3", "*", "0", ".", "C", "/", "="};

    private final JPanel panel = new JPanel(new BorderLayout(5, 5));
    private final JPanel outputPanel = new JPanel();
    private final ButtonPanel btnPanel = new ButtonPanel(symbols, this);

    private final CustomTextField expressionField = new CustomTextField("", 14f);
    private final CustomTextField outputField = new CustomTextField("", 42f);

    private void init() {
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        outputPanel.add(expressionField);
        outputPanel.add(outputField);
        add(panel);
        panel.add(outputPanel, BorderLayout.NORTH);
        panel.add(btnPanel, BorderLayout.CENTER);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setIconImage(icon.getImage());
        setTitle("Калькулятор");
        setSize(300, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public Calculator() {
        init();
    }

    public static void main(String[] args) {
        new Calculator();
    }

    public String formatNumber(double number) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.#######", symbols);
        return df.format(number);
    }

    public void outputFieldValidator(String input, CustomTextField field) {
        // Проверяем, является ли символ цифрой, точкой или знаком минуса
        if (input.matches("[0-9.]")) {
            // Если символ является цифрой, точкой или знаком минуса, формируем текущий текст
            String currentText = field.getText() + input;

            // Создаем регулярное выражение для проверки корректности текущего текста.
            // Добавляем поддержку знака минуса в начале строки
            String regex = "^-?(?!0{2,})\\d*(\\.\\d+)?([0-9]+\\.?)*[0-9]*$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(currentText);

            if (matcher.matches()) {
                // Проверяем, является ли вводимый символ точкой и содержит ли поле ввода уже точку
                if (input.equals(".") && field.getText().contains(".")) {
                    return;
                }

                // Проверяем, является ли вводимый символ минусом и содержит ли поле ввода уже минус
                if (input.equals("-") && field.getText().contains("-")) {
                    return;
                }

                field.setText(currentText);
            }
        }
    }

    public double evaluateExpression(String expression) {
        // Используем StringTokenizer для разделения чисел и операторов
        StringTokenizer tokenizer = new StringTokenizer(expression, "+-*/", true);

        double result = 0;
        String operator = "+";
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().trim();
            if (token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/")) {
                operator = token;
            } else {
                double number = Double.parseDouble(token);
                switch (operator) {
                    case "+" -> result += number;
                    case "-" -> result -= number;
                    case "*" -> result *= number;
                    case "/" -> {
                        if (number != 0) {
                            result /= number;
                        } else {
                            throw new ArithmeticException("Нельзя делить на 0");
                        }
                    }
                }
            }
        }
        return result;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (cmd.matches("[0-9.]")) {
            outputFieldValidator(cmd, outputField);
        } else if (!outputField.getText().isEmpty()) {
            switch (cmd) {
                case "+", "-", "*", "/" -> {
                    expressionField.setText(expressionField.getText() + outputField.getText() + " " + cmd + " ");
                    outputField.setText("");
                }
                case "=" -> {
                    try {
                        String expression = expressionField.getText() + outputField.getText();
                        double result = evaluateExpression(expression);
                        outputField.setText(formatNumber(result));
                        expressionField.setText(""); // Добавьте эту строку для очистки поля expressionField
                    } catch (NumberFormatException ex) {
                        outputField.setText("Ошибка");
                    } catch (ArithmeticException ex) {
                        outputField.setText("Деление на ноль невозможно!");
                    }
                }
            }
        }

        if (cmd.equals("C")) {
            outputField.setText("");
            expressionField.setText("");
        }
    }
}
