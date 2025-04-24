import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class Main extends JFrame implements ActionListener {
    private Person player;
    private Race race;
    private JTextArea outputArea;

    public Main() throws IOException {
        super("Horse Race Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        // Output area for logs
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        add(scrollPane, BorderLayout.CENTER);

        // Redirect all System.out and System.err to the text area
        PrintStream printStream = new PrintStream(new TextAreaOutputStream(outputArea));
        System.setOut(printStream);
        System.setErr(printStream);

        // Menu setup
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        String[] options = {
            "Create Horse", "Move Horse to Lane", "Remove Horse",
            "Start Race", "Show Stats", "Add Lanes",
            "Horse Data", "Add Random Horse", "Exit"
        };
        for (String opt : options) {
            JMenuItem item = new JMenuItem(opt);
            item.setActionCommand(opt);
            item.addActionListener(this);
            menu.add(item);
        }
        menuBar.add(menu);
        setJMenuBar(menuBar);

        // Initialize player and race
        player = new Person("Sample", 100.0);
        int distance = promptForInt("Enter track length:", 1, Integer.MAX_VALUE);
        int lanes    = promptForInt("Enter number of lanes:", 1, Integer.MAX_VALUE);
        race = new Race(distance, lanes);
        System.out.println(String.format("Initialized race: distance=%d, lanes=%d", distance, lanes));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        try {
            switch (cmd) {
                case "Create Horse":      createHorse();    break;
                case "Move Horse to Lane": moveHorse();     break;
                case "Remove Horse":      removeHorse();    break;
                case "Start Race":        startRace();      break;
                case "Show Stats":        showStats();      break;
                case "Add Lanes":         addLanes();       break;
                case "Horse Data":        horseData();      break;
                case "Add Random Horse":  addRandomHorse(); break;
                case "Exit":              exitProgram();    break;
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void createHorse() {
        String name = JOptionPane.showInputDialog(this, "Enter horse's name:");
        if (name == null) return;
        while (!race.isNameUnique(name)) {
            name = JOptionPane.showInputDialog(this, "Name exists. Enter another name:");
            if (name == null) return;
        }
        String sym = JOptionPane.showInputDialog(this, "Enter horse's symbol (single char):");
        if (sym == null || sym.isEmpty()) return;
        char symbol = sym.charAt(0);
        double confidence = promptForDouble("Enter confidence (0.0 to 1.0):", 0.0, 1.0);
        int lane = promptForInt(String.format("Enter lane (1 to %d):", race.getLanes()), 1, race.getLanes());
        Horse h = new Horse(symbol, name, confidence, lane);
        race.addHorse(h);
        System.out.println(String.format("Horse created: %s (symbol=%c, conf=%.2f) in lane %d", name, symbol, confidence, lane));
    }

    private void moveHorse() {
        String name = JOptionPane.showInputDialog(this, "Enter horse's name to move:");
        if (name == null) return;
        if (!race.existsHorse(name)) {
            JOptionPane.showMessageDialog(this, "No such horse.");
            return;
        }
        int lane = promptForInt(String.format("Enter new lane for %s (1 to %d):", name, race.getLanes()), 1, race.getLanes());
        for (Horse h : race.getHorses()) {
            if (h != null && h.getName().equals(name)) {
                h.setLane(lane);
                System.out.println(String.format("Moved %s to lane %d", name, lane));
                return;
            }
        }
    }

    private void removeHorse() {
        String name = JOptionPane.showInputDialog(this, "Enter horse's name to remove:");
        if (name == null) return;
        race.removeHorse(name);
        System.out.println("Removed horse: " + name);
    }

    private void startRace() throws IOException {
        System.out.println(String.format("Current balance: %.2f", player.getBalance()));
        int betOpt = JOptionPane.showConfirmDialog(this, "Do you want to place a bet?", "Bet", JOptionPane.YES_NO_OPTION);
        boolean didBet = false;
        if (betOpt == JOptionPane.YES_OPTION) {
            didBet = placeBet();
        }
        race.startRace();
        System.out.println("Race finished.");
        if (didBet) {
            processBetResults();
        }
        race.overwriteHorsesToFile("horse.csv");
        File_methods.addFile_horse(race.getHorses(), "horse_history.csv");
        System.out.println("Saved race data to files.");
    }

    private boolean placeBet() {
        List<Horse> horses = race.getHorses();
        if (horses.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No horses available to bet on.");
            return false;
        }
        StringBuilder sb = new StringBuilder("Available horses:\n");
        for (Horse h : horses) {
            sb.append(String.format("- %s (%.2f)\n", h.getName(), h.getConfidence()));
        }
        JOptionPane.showMessageDialog(this, sb.toString());
        String choice;
        do {
            choice = JOptionPane.showInputDialog(this, "Enter horse name to bet on:");
            if (choice == null) return false;
        } while (!race.existsHorse(choice));
        double amount;
        do {
            String amt = JOptionPane.showInputDialog(this, "Enter amount to bet:");
            if (amt == null) return false;
            try { amount = Double.parseDouble(amt); }
            catch (NumberFormatException e) { amount = -1; }
        } while (amount <= 0 || amount > player.getBalance());
        player.setBet(choice);
        player.decreaseBalance(amount);
        player.setBettingAmount(amount);
        System.out.println(String.format("Bet %.2f on %s", amount, choice));
        return true;
    }

    private void processBetResults() {
        Horse winner = race.getWinner();
        double amt = player.getBettingAmount();
        double change = (1 + winner.getConfidence()) * amt;
        if (player.getBet().equals(winner.getName())) {
            player.increaseBalance(change);
            System.out.println(String.format("You won the bet! Gained %.2f", change));
        } else {
            player.decreaseBalance(change);
            System.out.println(String.format("You lost the bet! Lost %.2f", change));
        }
        System.out.println(String.format("New balance: %.2f", player.getBalance()));
    }

    private void showStats() {
        race.showHorseStats();
        System.out.println("Displayed horse stats.");
    }

    private void addLanes() {
        int newLanes = promptForInt(String.format("Enter new number of lanes (>= %d):", race.getLanes()), race.getLanes(), Integer.MAX_VALUE);
        race.setLanes(newLanes);
        System.out.println("Updated lanes to: " + newLanes);
    }

    private void horseData() throws IOException {
        String name = JOptionPane.showInputDialog(this, "Enter horse's name for data:");
        if (name == null) return;
        race.horseData(name);
        System.out.println("Displayed data for horse: " + name);
    }

    private void addRandomHorse() {
        race.addRandomHorse();
        System.out.println("Added a random horse.");
    }

    private void exitProgram() {
        System.out.println("Exiting...");
        System.exit(0);
    }

    private int promptForInt(String message, int min, int max) {
        int value = min - 1;
        while (value < min || value > max) {
            String input = JOptionPane.showInputDialog(this, message);
            if (input == null) System.exit(0);
            try {
                value = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                value = min - 1;
            }
        }
        return value;
    }

    private double promptForDouble(String message, double min, double max) {
        double value = min - 1;
        while (value < min || value > max) {
            String input = JOptionPane.showInputDialog(this, message);
            if (input == null) return min;
            try {
                value = Double.parseDouble(input.trim());
            } catch (NumberFormatException e) {
                value = min - 1;
            }
        }
        return value;
    }

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            try {
                new Main().setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Custom OutputStream that redirects outputs to a JTextArea
     */
    private static class TextAreaOutputStream extends OutputStream {
        private final JTextArea textArea;
        private final StringBuilder buffer = new StringBuilder();

        TextAreaOutputStream(JTextArea textArea) {
            this.textArea = textArea;
        }

        @Override
        public void write(int b) {
            if (b == '\r') return;
            if (b == '\n') {
                final String text = buffer.toString() + "\n";
                SwingUtilities.invokeLater(() -> textArea.append(text));
                buffer.setLength(0);
            } else {
                buffer.append((char) b);
            }
        }
    }
}
