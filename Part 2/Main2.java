import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;

public class Main2 extends JFrame {
    private Person2 player;
    private Race2 race;

    private JPanel buttonPanel;
    private RacePanel racePanel;
    private JScrollPane messageScroll;
    private JTextArea messageArea;

    public Main2() throws IOException {
        super("Horse Race Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Messages
        messageArea = new JTextArea(5, 40);
        messageArea.setEditable(false);
        messageScroll = new JScrollPane(messageArea);
        add(messageScroll, BorderLayout.SOUTH);

        // Buttons
        buttonPanel = new JPanel();
        String[] labels = {
            "Create Horse", "Move Horse", "Remove Horse", "Start Race",
            "Show Stats", "Add Lanes", "Horse Data", "Add Random Horse", "Exit"
        };
        for (String label : labels) {
            JButton btn = new JButton(label);
            btn.addActionListener(e -> handleAction(e.getActionCommand()));
            buttonPanel.add(btn);
        }
        add(buttonPanel, BorderLayout.NORTH);

        // Race display
        racePanel = new RacePanel();
        add(racePanel, BorderLayout.CENTER);

        // Initialize model
        player = new Person2("Sample", 100.0);

        JTextField lengthField = new JTextField();
        JTextField lanesField  = new JTextField();
        Object[] inputFields = {
            "Enter track length:", lengthField,
            "Enter number of lanes:", lanesField
        };

        int option = JOptionPane.showConfirmDialog(
            this, inputFields, "Race Setup", JOptionPane.OK_CANCEL_OPTION
        );
        if (option == JOptionPane.OK_OPTION) {
            int distance = Integer.parseInt(lengthField.getText());
            int lanes    = Integer.parseInt(lanesField.getText());
            race = new Race2(distance, lanes);
            racePanel.setRace(race);
            log(String.format("Initialized race: %d length, %d lanes", distance, lanes));
        } else {
            System.exit(0);
        }
    }

    private void handleAction(String cmd) {
        try {
            switch (cmd) {
                case "Create Horse":     createHorse();        break;
                case "Move Horse":       moveHorse();          break;
                case "Remove Horse":     removeHorse();        break;
                case "Start Race":       startRaceAnimation(); break;
                case "Show Stats":       showStats();          break;
                case "Add Lanes":        addLanes();           break;
                case "Horse Data":       horseData();          break;
                case "Add Random Horse": addRandomHorse();     break;
                case "Exit":             System.exit(0);       break;
            }
        } catch (Exception ex) {
            log("Error: " + ex.getMessage());
        }
    }

    private int promptInt(String message) {
        while (true) {
            String s = JOptionPane.showInputDialog(this, message);
            if (s == null) return 0;
            try { return Integer.parseInt(s); } catch (NumberFormatException ignored) {}
        }
    }

    private double promptDouble(String message) {
        while (true) {
            String s = JOptionPane.showInputDialog(this, message);
            if (s == null) return 0;
            try { return Double.parseDouble(s); } catch (NumberFormatException ignored) {}
        }
    }

    private void createHorse() {
        String name = JOptionPane.showInputDialog(this, "Name:");
        if (name == null) return;
        String input = JOptionPane.showInputDialog(this, "Symbol:");
        if (input == null || input.isEmpty()) return;
        char symbol = input.charAt(0);
        double conf = promptDouble("Confidence 0.0-1.0:");
        int lane = promptInt("Lane (1 to " + race.getLanes() + "): ");

        String[] breedOpts  = Horse2.getBreedOptions();
        String breed        = (String) JOptionPane.showInputDialog(
            this, "Select breed:", "Breed", JOptionPane.PLAIN_MESSAGE,
            null, breedOpts, breedOpts[0]
        );
        if (breed == null) breed = breedOpts[0];

        String[] saddleOpts = Horse2.getSaddleOptions();
        String saddle       = (String) JOptionPane.showInputDialog(
            this, "Select saddle:", "Saddle", JOptionPane.PLAIN_MESSAGE,
            null, saddleOpts, saddleOpts[0]
        );
        if (saddle == null) saddle = saddleOpts[0];

        String[] coatOpts   = Horse2.getCoatColorOptions();
        String coatColor    = (String) JOptionPane.showInputDialog(
            this, "Select coat color:", "Coat Color", JOptionPane.PLAIN_MESSAGE,
            null, coatOpts, coatOpts[0]
        );
        if (coatColor == null) coatColor = coatOpts[0];

        Horse2 horse = new Horse2(
            symbol, name, conf, lane,
            0, 0,
            breed, saddle, coatColor
        );
        race.addHorse(horse);
        log("Horse created: " + name +
            " (" + breed + ", " + saddle + ", " + coatColor + ")");
        racePanel.repaint();
    }

    private void moveHorse() {
        String name = JOptionPane.showInputDialog(this, "Horse name:");
        if (name == null) return;
        int lane = promptInt("New lane:");
        for (Horse2 h : race.getHorses()) {
            if (h != null && h.getName().equals(name)) {
                h.setLane(lane);
                log("Moved " + name);
                break;
            }
        }
        racePanel.repaint();
    }

    private void removeHorse() {
        String name = JOptionPane.showInputDialog(this, "Remove horse name:");
        if (name == null) return;
        race.removeHorse(name);
        log("Removed " + name);
        racePanel.repaint();
    }

    private void startRaceAnimation() {
        final boolean[] didBet = {false};
        int betOpt = JOptionPane.showConfirmDialog(
            this,
            "Do you want to place a bet? Current balance: $" + player.getBalance(),
            "Bet", JOptionPane.YES_NO_OPTION
        );
        if (betOpt == JOptionPane.YES_OPTION) {
            didBet[0] = placeBet();
        }
    
        race.resetAllHorses();
        race.increaseRace();
        Timer t = new Timer(200, null);
        t.addActionListener(e -> {
            race.moveAllHorses();
            racePanel.repaint();
    
            if (race.raceWonByAnyHorse() || race.allHorsesFallen()) {
                ((Timer) e.getSource()).stop();
    
                // ―― NEW: use your announceWinner method to bump wins/confidence
                if (race.raceWonByAnyHorse()) {
                    race.announceWinner();
                } else {
                    log("No winner—all horses fallen!");
                }
    
                if (didBet[0]) {
                    processBetResults();
                }
    
                try {
                    race.overwriteHorsesToFile("horse.csv");       // overwrite current session
                    log("Saved current horses to horse.csv");
                    race.saveHorsesToFile("horse_history.csv");    // append to history
                    log("Appended this race to horse_history.csv");
                } catch (IOException io) {
                    log("Error saving files: " + io.getMessage());
                }
            }
        });
        t.start();
    }
    

    private boolean placeBet() {
        List<Horse2> horses = race.getHorses();
        if (horses.isEmpty()) {
            log("No horses available to bet on.");
            return false;
        }

        StringBuilder sb = new StringBuilder("Available horses:\n");
        for (Horse2 h : horses) {
            sb.append(String.format("- %s (Confidence: %.2f)\n",
                                     h.getName(), h.getConfidence()));
        }
        JOptionPane.showMessageDialog(this,
            sb.toString() + "\nYour current balance: $" + player.getBalance()
        );

        String choice;
        do {
            choice = JOptionPane.showInputDialog(this, "Enter horse name to bet on:");
            if (choice == null) return false;
        } while (!race.existsHorse(choice));

        double amount;
        do {
            String amt = JOptionPane.showInputDialog(this,
                "Enter amount to bet (Available: $" + player.getBalance() + "):");
            if (amt == null) return false;
            try {
                amount = Double.parseDouble(amt);
            } catch (NumberFormatException ex) {
                amount = -1;
            }
        } while (amount <= 0 || amount > player.getBalance());

        player.setBet(choice);
        player.setBettingAmount(amount);
        player.decreaseBalance(amount);
        log("Bet $" + amount + " on " + choice);
        return true;
    }

    private void processBetResults() {
        Horse2 winner = race.getWinner();
        if (winner == null) return;

        double amt    = player.getBettingAmount();
        double change = (1 + winner.getConfidence()) * amt;
        if (player.getBet().equals(winner.getName())) {
            player.increaseBalance(change);
            log("You won the bet! Gained: $" + change);
        } else {
            log("You lost the bet! Lost: $" + amt);
        }
        log("Current balance: $" + player.getBalance());
    }

    private void showStats() {
        List<Horse2> horses = race.getHorses();
        String[] columnNames = {
            "Name","Confidence","Win Rate","Lane",
            "Breed","Saddle","Coat Color"
        };
        Object[][] data = new Object[horses.size()][7];
        for (int i = 0; i < horses.size(); i++) {
            Horse2 h = horses.get(i);
            data[i][0] = h.getName();
            data[i][1] = h.getConfidence();
            data[i][2] = Validation2.roundToNDecimalPlaces(h.getWinRate(), 3);
            data[i][3] = h.getLane();
            data[i][4] = h.getBreed();
            data[i][5] = h.getSaddle();
            data[i][6] = h.getCoatColor();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        JOptionPane.showMessageDialog(this, scrollPane,
            "Horse Stats", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addLanes() {
        int nl = promptInt("New lanes >= " + race.getLanes());
        race.setLanes(nl);
        log("Lanes: " + nl);
        racePanel.repaint();
    }

    private void horseData() throws IOException {
        String name = JOptionPane.showInputDialog(this, "Horse name:");
        if (name == null) return;
        race.horseData(name);
        log("Data for " + name);
    }

    private void addRandomHorse() {
        race.addRandomHorse();
        log("Added random horse");
        racePanel.repaint();
    }

    private void log(String msg) {
        messageArea.append(msg + "\n");
    }

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            try {
                new Main2().setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Panel to visualize the race
    private static class RacePanel extends JPanel {
        private Race2 race;
        public void setRace(Race2 r) { this.race = r; }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (race == null) return;
            int width      = getWidth() - 40;
            int laneHeight = getHeight() / (race.getLanes() + 1);
            for (int i = 0; i < race.getLanes(); i++) {
                g.drawLine(20, (i+1)*laneHeight, width, (i+1)*laneHeight);
                Horse2 h = null;
                for (Horse2 hh : race.getHorses()) {
                    if (hh != null && hh.getLane() == i+1) {
                        h = hh;
                        break;
                    }
                }
                if (h != null) {
                    int x = 20 +
                        (int)((double)h.getDistanceTravelled()
                              / race.getRaceLength()
                              * (width - 20));
                    g.drawString(String.valueOf(h.getSymbol()), x,
                                 (i+1)*laneHeight - 5);
                }
            }
        }
    }
}
