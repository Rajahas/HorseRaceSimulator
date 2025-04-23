package GUITEST;

import javax.swing.*;
import java.awt.event.*;

public class HelloSwing2 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Button Example");
        JButton button = new JButton("Click Me!");
        JLabel label = new JLabel("Waiting...");

        button.setBounds(130, 100, 100, 40);
        label.setBounds(130, 150, 200, 40);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                label.setText("Button clicked!");
            }
        });

        frame.add(button);
        frame.add(label);
        frame.setSize(400, 300);
        frame.setLayout(null); // Absolute positioning
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

