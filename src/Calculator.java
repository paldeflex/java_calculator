import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Calculator extends JFrame implements ActionListener {
    private final ImageIcon icon = new ImageIcon("src/icon.png");
    private final JPanel panel = new JPanel(new BorderLayout(5, 5));
    private final JPanel outputPanel = new JPanel();
    private final ButtonPanel btnPanel = new ButtonPanel(this);

    private final CustomTextField expressionField = new CustomTextField("", 14f);
    private final CustomTextField outputField = new CustomTextField("0", 42f);

    private double firstNum;
    private int operator = 0;
    private boolean isInitialValueOverridden = false;

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

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if (outputField.getText().length() > 10 && outputField.getText().length() < 15) {
            outputField.setFont(outputField.getFont().deriveFont(30f));
        } else if (outputField.getText().length() >= 15) {
            outputField.setFont(outputField.getFont().deriveFont(20f));
        } else {
            outputField.setFont(outputField.getFont().deriveFont(42f));
        }

        if (!isInitialValueOverridden && cmd.matches("[1-9]") && outputField.getText().length() < 2) {
            outputField.setText("");
            isInitialValueOverridden = true;
        }

        if (outputField.getText().length() < 23) {
            switch (cmd) {
                case "." -> {
                    if (!outputField.getText().contains(".") && !outputField.getText().isEmpty()) {
                        outputField.setText(outputField.getText() + ".");
                    }
                }
                case "0" -> {
                    if (!outputField.getText().startsWith("0")) {
                        outputField.setText(outputField.getText() + "0");
                    }
                }
                case "1" -> {
                    outputField.setText(outputField.getText() + "1");
                    expressionField.setText("1");
                }
                case "2" -> outputField.setText(outputField.getText() + "2");
                case "3" -> outputField.setText(outputField.getText() + "3");
                case "4" -> outputField.setText(outputField.getText() + "4");
                case "5" -> outputField.setText(outputField.getText() + "5");
                case "6" -> outputField.setText(outputField.getText() + "6");
                case "7" -> outputField.setText(outputField.getText() + "7");
                case "8" -> outputField.setText(outputField.getText() + "8");
                case "9" -> outputField.setText(outputField.getText() + "9");
                case "+" -> {
                    if (!outputField.getText().isEmpty()) {
                        operator = 1;
                        firstNum = Double.parseDouble(outputField.getText());
                        isInitialValueOverridden = false;
                        outputField.setText("0");
                    }
                }
                case "−" -> {
                    if (!outputField.getText().isEmpty()) {
                        operator = 2;
                        firstNum = Double.parseDouble(outputField.getText());
                        isInitialValueOverridden = false;
                        outputField.setText("0");
                    }
                }
                case "×" -> {
                    if (!outputField.getText().isEmpty()) {
                        operator = 3;
                        firstNum = Double.parseDouble(outputField.getText());
                        isInitialValueOverridden = false;
                        outputField.setText("0");
                    }
                }
                case "÷" -> {
                    if (!outputField.getText().isEmpty()) {
                        operator = 4;
                        firstNum = Double.parseDouble(outputField.getText());
                        isInitialValueOverridden = false;
                        outputField.setText("0");
                    }
                }
                case "%" -> {
                    double num = Double.parseDouble(outputField.getText());
                    outputField.setText(String.valueOf(num / 100.0));
                }
                case "±" -> {
                    double neg = Double.parseDouble(outputField.getText());
                    neg *= -1;
                    outputField.setText(String.valueOf(neg));
                }

                case "=" -> {
                    if (!outputField.getText().isEmpty()) {
                        double secondNum = Double.parseDouble(outputField.getText());

                        switch (operator) {
                            case 1 -> {
                                outputField.setText(String.valueOf(firstNum + secondNum));
                                expressionField.setText(firstNum + " + " + secondNum + " = ");
                            }
                            case 2 -> {
                                outputField.setText(String.valueOf(firstNum - secondNum));
                                expressionField.setText(firstNum + " - " + secondNum + " = ");
                            }
                            case 3 -> {
                                outputField.setText(String.valueOf(firstNum * secondNum));
                                expressionField.setText(firstNum + " × " + secondNum + " = ");
                            }
                            case 4 -> {
                                outputField.setText(String.valueOf(firstNum / secondNum));
                                expressionField.setText(firstNum + " ÷ " + secondNum + " = ");
                            }
                        }
                    }
                }
            }
        }

        if (cmd.equals("C")) {
            isInitialValueOverridden = false;
            outputField.setText("0");
            expressionField.setText("");
            outputField.setFont(outputField.getFont().deriveFont(42f));
        }

    }
}
