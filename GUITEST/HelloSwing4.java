package GUITEST;

import javax.swing.*;
import java.awt.event.*;

public class HelloSwing4 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Dropdown Menu Example");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Create label
        JLabel label = new JLabel("Choose a language:");
        label.setBounds(50, 30, 200, 30);
        
        // Create dropdown (JComboBox)
        String[] languages = { "Java", "Python", "C++", "JavaScript" };
        JComboBox<String> comboBox = new JComboBox<>(languages);
        comboBox.setBounds(50, 70, 150, 30);

        // Label to show selected value
        JLabel resultLabel = new JLabel("Selected: ");
        resultLabel.setBounds(50, 110, 200, 30);

        // Add action listener
        comboBox.addActionListener(e -> {
            String selected = (String) comboBox.getSelectedItem();
            resultLabel.setText("Selected: " + selected);
        });

        // Add to frame
        frame.add(label);
        frame.add(comboBox);
        frame.add(resultLabel);
        frame.setVisible(true);
    }
}

