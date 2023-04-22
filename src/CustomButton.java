import javax.swing.*;
import java.awt.event.ActionListener;

public class CustomButton extends JButton {
    public CustomButton(String title, float fontSize, ActionListener listener) {
        super(title);
       addActionListener(listener);
       setFocusPainted(false);
       setFont(getFont().deriveFont(fontSize));
    }
}

