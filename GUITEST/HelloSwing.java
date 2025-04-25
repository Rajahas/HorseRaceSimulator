package GUITEST;
import javax.swing.*;

public class HelloSwing {
    public static void main(String[] args) {
        // Create the frame (window)
        JFrame frame = new JFrame("My First Swing App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Create a label
        JLabel label = new JLabel("Hello, Swing!", SwingConstants.CENTER);
        frame.add(label); // Add label to frame

        // Make it visible
        frame.setVisible(true);
    }
}
