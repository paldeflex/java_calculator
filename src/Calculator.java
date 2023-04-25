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
    private final String[] symbols = {"C", "±", "%", "+", "7", "8", "9", "-", "4", "5", "6", "*", "1", "2", "3", "/", "0", ".", "="};

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

    public void toggleNegativeSign(CustomTextField field) {
        double neg = Double.parseDouble(field.getText());
        neg *= -1;

        // Проверяем, является ли число целым
        if (neg % 1 == 0) {
            // Если число целое, преобразовываем его в int и затем в строку
            field.setText(String.valueOf((long) neg));
        } else {
            // Если число нецелое, форматируем его и преобразовываем в строку
            field.setText(formatNumber(neg));
        }
    }

    // ОСНОВНОЕ
    // TODO: Если есть пересечение арифметических знаков с отрицательным числом, то отрицательное число должно браться в скобки, чтобы была возможность вычисления т.е например вместо "80 + - 6.23" должно быть 80 + (-6.23)"
    // TODO: Добавить возможность считать проценты

    // ДОПОЛНИТЕЛЬНО
    // TODO: Менять размер шрифта, если количество символов выходит за пределы экрана
    // TODO: Заменить знаки минуса, умножения и деления
    // TODO: Добавить начальное значения в outputField - 0 и чтобы при этом всё работало как надо
    // TODO: Сделать круглые кнопки

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
                case "±" -> toggleNegativeSign(outputField);
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
