package GUITEST;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelloSwing6 {

    public static void main(String[] args) {
        JFrame frame = new JFrame("Method Call Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();

        JButton button1 = new JButton("Say Hello");
        JButton button2 = new JButton("Say Goodbye");

        JLabel label = new JLabel("Click a button!");

        // Button 1 calls sayHello method
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sayHello(label);
            }
        });

        // Button 2 calls sayGoodbye method
        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sayGoodbye(label);
            }
        });

        panel.add(button1);
        panel.add(button2);
        panel.add(label);

        frame.add(panel);
        frame.setVisible(true);
    }

    // Method to say hello
    public static void sayHello(JLabel label) {
        label.setText("Hello there!");
    }

    // Method to say goodbye
    public static void sayGoodbye(JLabel label) {
        label.setText("Goodbye!");
    }
}