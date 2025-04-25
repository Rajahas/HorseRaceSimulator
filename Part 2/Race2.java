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
public class Race2 {
    ArrayList<Horse2> allHorses = new ArrayList<>();
    private int raceLength;
    private int LANES;
    private final static String horse_file = "horse.csv";
    private static final String horse_history = "horse_history.csv";
    private static final String[] RANDOM_PREFIXES = {"Thunder", "Lightning", "Midnight", "Silver", "Golden", "Dashing", "Flying"};
    private static final String[] RANDOM_SUFFIXES = {"Storm", "Blaze", "Shadow", "Dream", "Star", "Rider", "Chaser"};
    private static final char[] RANDOM_SYMBOLS = {'♞', '♘', '♔', '♕', '♖', '♗', '♙'};
    private Horse2 winner;
    private MethodTimer2 timer;

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
        for (Horse2 horse : allHorses) {
            if (horse != null && horse.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    void setWinner(Horse2 h) {
        this.winner = h;
    }

    public Horse2 getWinner() {
        return this.winner;
    }

    public void addRandomHorse() {
        int emptyLane = findEmptyLane();
        if (emptyLane == -1) {
            showMessage("No empty lanes available for random horse");
            return;
        }
        Horse2 randomHorse = generateRandomHorse(emptyLane + 1);
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

    public Horse2 generateRandomHorse(int lane) {
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
        confidence = Validation2.roundToNDecimalPlaces(confidence, 3);
        return new Horse2(symbol, name, confidence, lane);
    }

    public void fillEmptyLanes() {
        for (int i = 0; i < LANES; i++) {
            if (i >= allHorses.size() || allHorses.get(i) == null) {
                Horse2 randomHorse = generateRandomHorse(i + 1);
                addHorse(randomHorse);
                showMessage("Added random horse " + randomHorse.getName() + " to lane " + randomHorse.getLane());
                return;
            }
        }
    }

    public boolean isNameUnique(String name) {
        for (Horse2 horse : allHorses) {
            if (horse != null && horse.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public Race2(int distance, int lanes) throws IOException {
        File_methods2.createFile(horse_file);
        File_methods2.createFile(horse_history);
        if (distance > 0 && lanes >= 2) {
            raceLength = distance;
            setLanes(lanes);
            startLanes();
            loadHorsesFromFile(horse_file);
            timer = new MethodTimer2();
        } else {
            showMessage("Race length must be a positive integer. Setting defaults.");
            raceLength = 10;
            setLanes(5);
            startLanes();
            loadHorsesFromFile(horse_file);
            timer = new MethodTimer2();
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

    public void addHorse(Horse2 theHorse) {
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

    private Horse2 getHorseInLane(int laneNumber) {
        for (Horse2 h : allHorses) {
            if (h != null && h.getLane() == laneNumber) return h;
        }
        return null;
    }

    private String formatLane(Horse2 theHorse) {
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
        for (Horse2 h : allHorses) {
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

    private void moveHorse(Horse2 theHorse) {
        if (theHorse == null || theHorse.hasFallen()) return;
        if (Math.random() < theHorse.getConfidence()) theHorse.moveForward();
        if (Math.random() < 0.1 * theHorse.getConfidence() * theHorse.getConfidence()) {
            theHorse.fall();
            theHorse.decreaseConfidence();
        }
    }

    public void resetAllHorses() {
        for (Horse2 h : allHorses) {
            if (h != null) {
                h.resetFallen();
                h.goBackToStart();
            }
        }
    }

    public void moveAllHorses() {
        for (Horse2 h : allHorses) moveHorse(h);
    }

    public boolean raceWonByAnyHorse() {
        for (Horse2 h : allHorses) if (h != null && h.getDistanceTravelled() == raceLength) return true;
        return false;
    }

    public void removeHorse(String name) {
        for (int i = 0; i < allHorses.size(); i++) {
            Horse2 h = allHorses.get(i);
            if (h != null && h.getName().equals(name)) {
                allHorses.set(i, null);
                showMessage("Horse " + name + " has been removed from the race.");
                return;
            }
        }
        showMessage("No horse found with the name " + name + ".");
    }

    public boolean allHorsesFallen() {
        for (Horse2 h : allHorses) if (h != null && !h.hasFallen()) return false;
        return true;
    }

    public void showHorseStats() {
        for (Horse2 h : allHorses) {
            if (h != null) {
                double conf = Validation2.roundToNDecimalPlaces(h.getConfidence(), 3);
                double winRate = (h.getRaces() != 0) ? Validation2.roundToNDecimalPlaces(h.getWinRate(), 3) : 0;
                showMessage("Name: " + h.getName() +
                            " Confidence: " + conf +
                            " Win rate: " + winRate +
                            " Lane: " + h.getLane());
            }
        }
    }

    void loadHorsesFromFile(String file_name) throws IOException {
        allHorses = File_methods2.readFile_horse(file_name);
    }

    public void saveHorsesToFile(String file_name) throws IOException {
        File_methods2.addFile_horse(allHorses, file_name);
    }

    public void overwriteHorsesToFile(String file_name) throws IOException {
        File_methods2.addFile_horse_overwrite(allHorses, file_name);
    }

    public int getLanes() {
        return LANES;
    }

    public void increaseRace() {
        for (Horse2 h : allHorses) {
            if (h != null) h.setRaces(h.getRaces() + 1);
        }
    }

    public ArrayList<Horse2> getHorses() {
        return allHorses;
    }

    public void horseData(String name) throws IOException {
        File_methods2.readHorse(horse_history, name);
    }
}
