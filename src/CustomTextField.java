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

    public void resizeFont() {
        if (this.getText().length() > 9 && this.getText().length() <= 13) {
            setFont(getFont().deriveFont(30f));
        } else if (this.getText().length() > 13) {
            setFont(getFont().deriveFont(20f));
        } else {
            setFont(getFont().deriveFont(42f));
        }
    }
}
