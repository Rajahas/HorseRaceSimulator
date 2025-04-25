import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

/**
 * A multi-horse race, each horse running in its own lane
 * for a given distance. Displays in-place to overwrite previous output,
 * and shows status messages in a separate message panel.
 */
public class Race {
    ArrayList<Horse> allHorses = new ArrayList<>();
    private int raceLength;
    private int LANES;
    private final static String horse_file = "horse.csv";
    private static final String horse_history = "horse_history.csv";
    private static final String[] RANDOM_PREFIXES = {"Thunder", "Lightning", "Midnight", "Silver", "Golden", "Dashing", "Flying"};
    private static final String[] RANDOM_SUFFIXES = {"Storm", "Blaze", "Shadow", "Dream", "Star", "Rider", "Chaser"};
    private static final char[] RANDOM_SYMBOLS = {'♞', '♘', '♔', '♕', '♖', '♗', '♙'};
    private Horse winner;
    private MethodTimer timer;

    // Separate message window
    private static JFrame msgFrame;
    private static JTextArea msgArea;

    private static void initMessagePanel() {
        if (msgFrame != null) return;
        msgFrame = new JFrame("Race Messages");
        msgArea = new JTextArea(15, 40);
        msgArea.setEditable(false);
        msgFrame.add(new JScrollPane(msgArea));
        msgFrame.pack();
        msgFrame.setLocation(750, 50);
        msgFrame.setVisible(true);
    }

    private static void showMessage(String msg) {
        SwingUtilities.invokeLater(() -> {
            initMessagePanel();
            msgArea.append(msg + "\n");
        });
    }

    public int getRaceLength() {
        return this.raceLength;
    }

    public boolean existsHorse(String name) {
        for (Horse horse : allHorses) {
            if (horse != null && horse.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    void setWinner(Horse h) {
        this.winner = h;
    }

    public Horse getWinner() {
      return this.winner;
    }

    public void addRandomHorse() {
        int emptyLane = findEmptyLane();
        if (emptyLane == -1) {
            showMessage("No empty lanes available for random horse");
            return;
        }
        Horse randomHorse = generateRandomHorse(emptyLane + 1);
        addHorse(randomHorse);
        showMessage("Added random horse " + randomHorse.getName() + " to lane " + randomHorse.getLane());
    }

    private int findEmptyLane() {
        for (int i = 0; i < LANES; i++) {
            if (i >= allHorses.size() || allHorses.get(i) == null) {
                return i;
            }
        }
        return -1;
    }

    public Horse generateRandomHorse(int lane) {
        Random rand = new Random();
        String name;
        char symbol;
        do {
            String prefix = RANDOM_PREFIXES[rand.nextInt(RANDOM_PREFIXES.length)];
            String suffix = RANDOM_SUFFIXES[rand.nextInt(RANDOM_SUFFIXES.length)];
            symbol = RANDOM_SYMBOLS[rand.nextInt(RANDOM_SYMBOLS.length)];
            name = prefix + " " + suffix;
        } while (!isNameUnique(name));
        double confidence = rand.nextDouble();
        confidence = Validation.roundToNDecimalPlaces(confidence, 3);
        return new Horse(symbol, name, confidence, lane);
    }

    public void fillEmptyLanes() {
        for (int i = 0; i < LANES; i++) {
            if (i >= allHorses.size() || allHorses.get(i) == null) {
                Horse randomHorse = generateRandomHorse(i + 1);
                addHorse(randomHorse);
                showMessage("Added random horse " + randomHorse.getName() + " to lane " + randomHorse.getLane());
                return;
            }
        }
    }

    public boolean isNameUnique(String name) {
        for (Horse horse : allHorses) {
            if (horse != null && horse.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public Race(int distance, int lanes) throws IOException {
        File_methods.createFile(horse_file);
        File_methods.createFile(horse_history);
        if (distance > 0 && lanes >= 2) {
            raceLength = distance;
            setLanes(lanes);
            startLanes();
            loadHorsesFromFile(horse_file);
            timer = new MethodTimer();
        } else {
            showMessage("Race length must be a positive integer. Setting defaults.");
            raceLength = 10;
            setLanes(5);
            startLanes();
            loadHorsesFromFile(horse_file);
            timer = new MethodTimer();
        }
    }

    public void startLanes() {
        for (int i = 0; i < LANES; i++) allHorses.add(null);
    }

    public void setLanes(int lanes) {
        if (lanes > 0) {
            this.LANES = lanes;
        } else {
            showMessage("Number of lanes must be positive.");
        }
    }

    public void addHorse(Horse theHorse) {
        if (!isNameUnique(theHorse.getName())) {
            showMessage("A horse with the name '" + theHorse.getName() + "' already exists.");
            return;
        }
        int idx = theHorse.getLane() - 1;
        while (allHorses.size() <= idx) allHorses.add(null);
        allHorses.set(idx, theHorse);
    }

    public void startRace() {
        resetAllHorses();
        increaseRace();
        timer.reset(); timer.start();
        boolean finished = false;
        while (!finished) {
            moveAllHorses();
            renderRace();
            if (raceWonByAnyHorse()) {
                announceWinner();
                finished = true;
            } else if (allHorsesFallen()) {
                timer.stop();
                showMessage("All horses have fallen! The race is over.");
                timer.printDuration();
                finished = true;
            }
            try { TimeUnit.MILLISECONDS.sleep(200); } catch (InterruptedException e) {}
        }
        showHorseStats();
    }

    private void renderRace() {
        StringBuilder sb = new StringBuilder();
        sb.append('\u000C');
        for (int i = 0; i < raceLength + 3; i++) sb.append('=');
        sb.append('\n');
        for (int lane = 1; lane <= LANES; lane++) {
            sb.append(formatLane(getHorseInLane(lane))).append('\n');
        }
        for (int i = 0; i < raceLength + 3; i++) sb.append('=');
        sb.append('\n');
        System.out.print(sb.toString());
    }

    private Horse getHorseInLane(int laneNumber) {
        for (Horse h : allHorses) {
            if (h != null && h.getLane() == laneNumber) return h;
        }
        return null;
    }

    private String formatLane(Horse theHorse) {
        StringBuilder line = new StringBuilder();
        line.append('|');
        if (theHorse == null) {
            line.append(" Empty Lane");
            for (int i = 0; i < raceLength - 10; i++) line.append(' ');
            line.append('|');
            return line.toString();
        }
        int before = theHorse.getDistanceTravelled();
        for (int i = 0; i < before; i++) line.append(' ');
        line.append(theHorse.hasFallen() ? '\u2322' : theHorse.getSymbol());
        for (int i = 0; i < raceLength - before; i++) line.append(' ');
        line.append('|');
        return line.toString();
    }

    private void announceWinner() {
        for (Horse h : allHorses) {
            if (h != null && h.getDistanceTravelled() == raceLength) {
                timer.stop();
                h.increaseConfidence();
                showMessage("\nAnd the winner is… " + h.getName() + "!");
                h.setWins(h.getWins() + 1);
                setWinner(h);
                timer.printDuration();
                break;
            }
        }
    }

    private void moveHorse(Horse theHorse) {
        if (theHorse == null || theHorse.hasFallen()) return;
        if (Math.random() < theHorse.getConfidence()) theHorse.moveForward();
        if (Math.random() < 0.1 * theHorse.getConfidence() * theHorse.getConfidence()) {
            theHorse.fall();
            theHorse.decreaseConfidence();
        }
    }

    public void resetAllHorses() {
        for (Horse h : allHorses) {
            if (h != null) {
                h.resetFallen();
                h.goBackToStart();
            }
        }
    }

    public void moveAllHorses() {
        for (Horse h : allHorses) moveHorse(h);
    }

    public boolean raceWonByAnyHorse() {
        for (Horse h : allHorses) if (h != null && h.getDistanceTravelled() == raceLength) return true;
        return false;
    }

    public void removeHorse(String name) {
        for (int i = 0; i < allHorses.size(); i++) {
            Horse h = allHorses.get(i);
            if (h != null && h.getName().equals(name)) {
                allHorses.set(i, null);
                showMessage("Horse " + name + " has been removed from the race.");
                return;
            }
        }
        showMessage("No horse found with the name " + name + ".");
    }

    public boolean allHorsesFallen() {
        for (Horse h : allHorses) if (h != null && !h.hasFallen()) return false;
        return true;
    }

    public void showHorseStats() {
        for (Horse h : allHorses) {
            if (h != null) {
                double conf = Validation.roundToNDecimalPlaces(h.getConfidence(), 3);
                double winRate = (h.getRaces() != 0) ? Validation.roundToNDecimalPlaces(h.getWinRate(), 3) : 0;
                showMessage("Name: " + h.getName() +
                            " Confidence: " + conf +
                            " Win rate: " + winRate +
                            " Lane: " + h.getLane());
            }
        }
    }

    void loadHorsesFromFile(String file_name) throws IOException {
        allHorses = File_methods.readFile_horse(file_name);
    }

    public void saveHorsesToFile(String file_name) throws IOException {
        File_methods.addFile_horse(allHorses, file_name);
    }

    public void overwriteHorsesToFile(String file_name) throws IOException {
        File_methods.addFile_horse_overwrite(allHorses, file_name);
    }

    public int getLanes() {
        return LANES;
    }

    public void increaseRace() {
        for (Horse h : allHorses) {
            if (h != null) h.setRaces(h.getRaces() + 1);
        }
    }

    public ArrayList<Horse> getHorses() {
        return allHorses;
    }

    public void horseData(String name) throws IOException {
        File_methods.readHorse(horse_history, name);
    }
}
