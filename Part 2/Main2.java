import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

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
            "Show Stats", "Add Lanes", "Horse Data", "Add Random Horse",
            "Compare Horses", "Exit"
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

        // Race setup dialog
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
                case "Create Horse":
                    createHorse();
                    break;
                case "Move Horse":
                    moveHorse();
                    break;
                case "Remove Horse":
                    removeHorse();
                    break;
                case "Start Race":
                    startRaceAnimation();
                    break;
                case "Show Stats":
                    showStats();
                    break;
                case "Add Lanes":
                    addLanes();
                    break;
                case "Horse Data":
                    horseData();
                    break;
                case "Add Random Horse":
                    addRandomHorse();
                    break;
                case "Compare Horses":
                    compareHorses();
                    break;
                case "Exit":
                    System.exit(0);
                    break;
            }
        } catch (Exception ex) {
            log("Error: " + ex.getMessage());
        }
    }

    private int promptInt(String message) {
        while (true) {
            String s = JOptionPane.showInputDialog(this, message);
            if (s == null) return 0;
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException ignored) {}
        }
    }

    private double promptDouble(String message) {
        while (true) {
            String s = JOptionPane.showInputDialog(this, message);
            if (s == null) return 0;
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException ignored) {}
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

        Horse2 horse = new Horse2(symbol, name, conf, lane, 0, 0, breed, saddle, coatColor);
        race.addHorse(horse);
        log("Horse created: " + name + " (" + breed + ", " + saddle + ", " + coatColor + ")");
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
        race.applyWeather();
        log("Weather for this race: " + race.getWeather());
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
                if (race.raceWonByAnyHorse()) {
                    race.announceWinner();
                } else {
                    log("No winner—all horses fallen!");
                }
                if (didBet[0]) processBetResults();
                try {
                    race.overwriteHorsesToFile("horse.csv");
                    log("Saved current horses to horse.csv");
                    race.saveHorsesToFile("horse_history.csv");
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
    
        // Show name, confidence, win rate, expected profit and loss per $1
        StringBuilder sb = new StringBuilder("Available horses if player bet everything:\n");
        for (Horse2 h : horses) {
            double winRate        = h.getWinRate();
            double confidence     = h.getConfidence();
            double expectedProfit = player.getBalance() * confidence;                // expected profit per $1
            double expectedLoss   = player.getBalance() * confidence;    // expected loss per $1
            sb.append(String.format(
                "- %s | Conf: %.2f | WinRate: %.2f | ExpProfit: $%.2f | ExpLoss: $%.2f\n",
                h.getName(), confidence, winRate, expectedProfit, expectedLoss
            ));
        }
        JOptionPane.showMessageDialog(this,
            sb.toString() + "\nCurrent balance: $" + player.getBalance()
        );
    
        // Continue with bet selection
        String choice;
        do {
            choice = JOptionPane.showInputDialog(this, "Enter horse name to bet on:");
            if (choice == null) return false;
        } while (!race.existsHorse(choice));
    
        double amount;
        do {
            String amt = JOptionPane.showInputDialog(
                this,
                "Enter amount to bet (Available: $" + player.getBalance() + "):"
            );
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
            player.decreaseBalance(change); // apply same penalty
            log("You lost the bet! Lost: $" + change);
        }
        log("Current balance: $" + player.getBalance());
    }

    private void showStats() {
        List<Horse2> horses = race.getHorses();
        String[] cols = {"Name","Confidence","Win Rate","Lane","Breed","Saddle","Coat"};
        Object[][] data = new Object[horses.size()][cols.length];
        for (int i = 0; i < horses.size(); i++) {
            Horse2 h = horses.get(i);
            data[i] = new Object[] {
                h.getName(), h.getConfidence(),
                String.format("%.3f", h.getWinRate()),
                h.getLane(), h.getBreed(), h.getSaddle(), h.getCoatColor()
            };
        }
        JTable table = new JTable(data, cols);
        JOptionPane.showMessageDialog(this, new JScrollPane(table), "Horse Stats", JOptionPane.INFORMATION_MESSAGE);
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

    private void compareHorses() {
        List<Horse2> horses = race.getHorses();
        if (horses.size() < 2) {
            JOptionPane.showMessageDialog(this, "Need at least two horses to compare.", "Compare Horses", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] names = horses.stream().map(Horse2::getName).toArray(String[]::new);
        String first = (String) JOptionPane.showInputDialog(this, "Select first horse:", "Compare Horses", JOptionPane.PLAIN_MESSAGE, null, names, names[0]);
        if (first == null) return;
        String[] names2 = Arrays.stream(names).filter(n -> !n.equals(first)).toArray(String[]::new);
        String second = (String) JOptionPane.showInputDialog(this, "Select second horse:", "Compare Horses", JOptionPane.PLAIN_MESSAGE, null, names2, names2[0]);
        if (second == null) return;
        Horse2 h1 = horses.stream().filter(h -> h.getName().equals(first)).findFirst().orElse(null);
        Horse2 h2 = horses.stream().filter(h -> h.getName().equals(second)).findFirst().orElse(null);
        String[] cols = {"Attribute", h1.getName(), h2.getName()};
        Object[][] data = {
            {"Confidence", h1.getConfidence(), h2.getConfidence()},
            {"Win Rate", String.format("%.3f", h1.getWinRate()), String.format("%.3f", h2.getWinRate())},
            {"Lane", h1.getLane(), h2.getLane()},
            {"Breed", h1.getBreed(), h2.getBreed()},
            {"Saddle", h1.getSaddle(), h2.getSaddle()},
            {"Coat Color", h1.getCoatColor(), h2.getCoatColor()}
        };
        JTable table = new JTable(data, cols);
        JOptionPane.showMessageDialog(this, new JScrollPane(table), "Compare Horses", JOptionPane.INFORMATION_MESSAGE);
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

    private static class RacePanel extends JPanel {
        private Race2 race;
        public void setRace(Race2 r) { this.race = r; }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (race == null) return;
            int width = getWidth() - 40;
            int laneHeight = getHeight() / (race.getLanes() + 1);
            for (int i = 0; i < race.getLanes(); i++) {
                g.drawLine(20, (i+1)*laneHeight, width, (i+1)*laneHeight);
                for (Horse2 hh : race.getHorses()) {
                    if (hh != null && hh.getLane() == i+1) {
                        int x = 20 + (int)((double)hh.getDistanceTravelled() / race.getRaceLength() * (width - 20));
                        g.drawString(String.valueOf(hh.getSymbol()), x, (i+1)*laneHeight - 5);
                    }
                }
            }
        }
    }
}
