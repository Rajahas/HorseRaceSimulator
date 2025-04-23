package GUITEST;
import javax.swing.*;
import java.awt.event.*;

public class HelloSwing3 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Menu Bar Example");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();

        // Create "File" menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Add items to "File"
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator(); // Adds a line separator
        fileMenu.add(exitItem);

        // Action for Exit
        exitItem.addActionListener(e -> System.exit(0));

        // Create "Edit" menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");

        // Add items to "Edit"
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        // Add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        // Set menu bar to the frame
        frame.setJMenuBar(menuBar);

        // Show the frame
        frame.setVisible(true);
    }
}
