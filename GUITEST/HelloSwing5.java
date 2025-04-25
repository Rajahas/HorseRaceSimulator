package GUITEST;

import javax.swing.*;
import java.awt.event.*;

public class HelloSwing5 {
    private static Timer timer = new Timer();
    public static void main(String[] args) {
        // Frame setup
        JFrame frame = new JFrame("Method Timer");
        frame.setSize(350, 250);
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Buttons
        JButton startButton = new JButton("Start");
        JButton stopButton = new JButton("Stop");
        JButton printButton = new JButton("Print Duration");
        JButton resetButton = new JButton("Reset");

        // Position buttons
        startButton.setBounds(30, 30, 120, 30);
        stopButton.setBounds(170, 30, 120, 30);
        printButton.setBounds(30, 80, 260, 30);
        resetButton.setBounds(30, 130, 260, 30);

        // Label for showing result
        JLabel resultLabel = new JLabel("Duration: ");
        resultLabel.setBounds(30, 170, 300, 30);

        // Button actions
        startButton.addActionListener(e -> {
            timer.start();
            resultLabel.setText("Timer started...");
        });

        stopButton.addActionListener(e -> {
            timer.stop();
            resultLabel.setText("Timer stopped.");
        });

        printButton.addActionListener(e -> {
            long duration = timer.Duration();
            if (duration > 0) {
                resultLabel.setText("Duration: " + duration + " ms");
            } else {
                resultLabel.setText("Start or stop not called properly!");
            }
        });

        resetButton.addActionListener(e -> {
            timer.reset();
            resultLabel.setText("Timer reset.");
        });

        // Add everything to frame
        frame.add(startButton);
        frame.add(stopButton);
        frame.add(printButton);
        frame.add(resetButton);
        frame.add(resultLabel);

        // Show GUI
        frame.setVisible(true);
    }
}

