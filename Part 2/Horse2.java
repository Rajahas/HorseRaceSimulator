import java.util.Arrays;

/**
 * Write a description of class Horse here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class Horse2 {
    // Fields of class Horse2
    private String horseName;
    private char horseSymbol;
    private int horseDistance;
    private double horseConfidence;
    private boolean fallen;
    private int lane;
    private int wins;
    private int races;
    private double win_rate;

    // New fields: breeds, saddles, and coat colors
    private String breed;
    private String saddle;
    private String coatColor;

    // Available options
    private static final String[] BREED_OPTIONS = {
        "Thoroughbred", "Arabian", "Paint", "Appaloosa"
    };
    private static final String[] SADDLE_OPTIONS = {
        "Soft", "Hard", "Big", "Small", "Round"
    };
    private static final String[] COAT_COLOR_OPTIONS = {
        "Black", "White", "Green", "Grey", "Yellow"
    };

    /**
     * Full constructor for Horse2.
     */
    public Horse2(char horseSymbol, String horseName, double horseConfidence,
                  int lane, int races, int wins,
                  String breed, String saddle, String coatColor) {
        this.horseSymbol = horseSymbol;
        this.horseName = horseName;
        setConfidence(horseConfidence);
        this.horseDistance = 0;
        this.fallen = false;
        this.lane = lane;
        this.wins = wins;
        this.races = races;
        this.win_rate = getWinRate();

        // Initialize new fields
        setBreed(breed);
        setSaddle(saddle);
        setCoatColor(coatColor);
    }

    /**
     * Legacy constructor for Horse2 (matches old signature).
     * Uses default breed, saddle, and coat color.
     */
    public Horse2(char horseSymbol, String horseName, double horseConfidence,
                  int lane, int races, int wins) {
        this(horseSymbol, horseName, horseConfidence,
             lane, races, wins,
             BREED_OPTIONS[0], SADDLE_OPTIONS[0], COAT_COLOR_OPTIONS[0]);
    }

    /**
     * Default constructor creating a horse with no wins or races.
     */
    public Horse2(char horseSymbol, String horseName,
                  double horseConfidence, int lane) {
        this(horseSymbol, horseName, horseConfidence,
             lane, 0, 0,
             BREED_OPTIONS[0], SADDLE_OPTIONS[0], COAT_COLOR_OPTIONS[0]);
    }

    // Core methods
    public void fall() { this.fallen = true; }
    public double getConfidence() { return this.horseConfidence; }
    public int getDistanceTravelled() { return this.horseDistance; }
    public String getName() { return this.horseName; }
    public char getSymbol() { return this.horseSymbol; }
    public void goBackToStart() { this.horseDistance = 0; }
    public boolean hasFallen() { return this.fallen; }
    public void moveForward() { this.horseDistance += 1; }

    public void setConfidence(double newConfidence) {
        if (newConfidence > 1.0) this.horseConfidence = 1.0;
        else if (newConfidence < 0.0) this.horseConfidence = 0.0;
        else this.horseConfidence = newConfidence;
    }

    public void setSymbol(char newSymbol) { this.horseSymbol = newSymbol; }

    public int getLane() { return this.lane; }
    public void setLane(int n) { this.lane = n; }

    public void increaseConfidence() { this.horseConfidence = Math.min(1.0, this.horseConfidence + 0.05); }
    public void decreaseConfidence() { this.horseConfidence = Math.max(0.0, this.horseConfidence - 0.05); }
    public void resetFallen() { this.fallen = false; }

    public void setWins(int n) { this.wins = n; }
    public int getWins() { return this.wins; }

    public void setRaces(int n) { this.races = n; }
    public int getRaces() { return this.races; }

    public double getWinRate() {
        if (races == 0) return 0.0;
        this.win_rate = (double) wins / races;
        return this.win_rate;
    }

    // Getters and setters for new fields
    public String getBreed() { return this.breed; }
    public void setBreed(String breed) {
        if (Arrays.asList(BREED_OPTIONS).contains(breed)) {
            this.breed = breed;
        } else {
            this.breed = BREED_OPTIONS[0];
        }
    }
    public static String[] getBreedOptions() { return BREED_OPTIONS.clone(); }

    public String getSaddle() { return this.saddle; }
    public void setSaddle(String saddle) {
        if (Arrays.asList(SADDLE_OPTIONS).contains(saddle)) {
            this.saddle = saddle;
        } else {
            this.saddle = SADDLE_OPTIONS[0];
        }
    }
    public static String[] getSaddleOptions() { return SADDLE_OPTIONS.clone(); }

    public String getCoatColor() { return this.coatColor; }
    public void setCoatColor(String coatColor) {
        if (Arrays.asList(COAT_COLOR_OPTIONS).contains(coatColor)) {
            this.coatColor = coatColor;
        } else {
            this.coatColor = COAT_COLOR_OPTIONS[0];
        }
    }
    public static String[] getCoatColorOptions() { return COAT_COLOR_OPTIONS.clone(); }

    private void applyBreed()
{
  if (this.breed.equals("Thoroughbred"))
  {
    increaseConfidence();
    return;
  }
  if (this.breed.equals("Arabian"))
  {
    increaseConfidence();
    return;
  }
  if (this.breed.equals("Paint"))
  {
    increaseConfidence();
    return;
  }
  if (this.breed.equals("Appaloosa"))
  {
    decreaseConfidence();
    return;
  }
}

    private void applySaddle()
{
  if (this.saddle.equals("Soft"))
  {
    increaseConfidence();
    return;
  }
  if (this.saddle.equals("Hard"))
  {
    increaseConfidence();
    increaseConfidence();
    return;
  }
  if (this.saddle.equals("Big"))
  {
    decreaseConfidence();
    return;
  }
  if (this.saddle.equals("Small"))
  {
    return;
  }
  if (this.saddle.equals("Round"))
  {
    increaseConfidence();
    return;
  }
}

private void applyCoat()
{
  if (this.coatColor.equals("Black"))
  {
    increaseConfidence();
    return;
  }
  if (this.coatColor.equals("White"))
  {
    return;
  }
  if (this.coatColor.equals("Green"))
  {
    decreaseConfidence();
    return;
  }
  if (this.coatColor.equals("Grey"))
  {
    increaseConfidence();
    return;
  }
  if (this.coatColor.equals("Yellow"))
  {
    return;
  }
}

public void applyAttributes()
{
  applyBreed();
  applySaddle();
  applyCoat();
}
}
