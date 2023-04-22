import javax.swing.*;

public class CustomTextField extends JTextField {
    public CustomTextField(String title, float fontSize) {
        super(title);
        setHorizontalAlignment(SwingConstants.RIGHT);
        setFont(getFont().deriveFont(fontSize));
        setBorder(null);
        setEditable(false);
        setFocusable(false);
    }
}
