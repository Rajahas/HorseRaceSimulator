/**
* Write a description of class Horse here.
* 
* @author (your name) 
* @version (a version number or a date)
*/
public class Horse2
{
  //Fields of class Horse
  private String horseName;
  private char horseSymbol;
  private int horseDistance;
  private double horseConfidence;
  private boolean fallen;
  private int lane;
  private int wins;
  private int races;
  private double win_rate;

  //Constructor of class Horse
  /**
   * Constructor for objects of class Horse.
   * Initializes a horse with symbol, name, confidence, lane, number of races, and number of wins.
   * 
   * @param horseSymbol the symbol representing the horse
   * @param horseName the name of the horse
   * @param horseConfidence the confidence level of the horse
   * @param lane the lane in which the horse will race
   * @param races the number of races the horse has participated in
   * @param wins the number of races the horse has won
   */
  public Horse2(char horseSymbol, String horseName, double horseConfidence, int lane, int races, int wins)
  {
    this.horseSymbol = horseSymbol;
    this.horseName = horseName;
    this.setConfidence(horseConfidence);
    this.horseDistance = 0;
    this.fallen = false;
    this.lane = lane;
    this.wins = wins;
    this.races = races;
    this.win_rate = getWinRate();
  }

  /**
   * Default constructor creating a horse with no wins or races.
   * 
   * @param horseSymbol the symbol representing the horse
   * @param horseName the name of the horse
   * @param horseConfidence the confidence level of the horse
   * @param lane the lane in which the horse will race
   */
  public Horse2(char horseSymbol, String horseName, double horseConfidence, int lane)
  {
    this.horseSymbol = horseSymbol;
    this.horseName = horseName;
    this.setConfidence(horseConfidence);
    this.horseDistance = 0;
    this.fallen = false;
    this.lane = lane;
    this.wins = 0;
    this.races = 0;
    this.win_rate = 0;
  }

  //Other methods of class Horse
  public void fall()
  {
    this.fallen = true;
  }

  public double getConfidence()
  {
    return this.horseConfidence;
  }

  public int getDistanceTravelled()
  {
    return this.horseDistance;
  }

  public String getName()
  {
    return this.horseName;
  }

  public char getSymbol()
  {
    return this.horseSymbol;
  }

  public void goBackToStart()
  {
    this.horseDistance = 0;
  }

  public boolean hasFallen()
  {
    return this.fallen;
  }

  public void moveForward()
  {
    this.horseDistance += 1; 
  }

  /**
   * Sets the horse's confidence to a new value.
   * The confidence value is clamped between 0.0 and 1.0.
   * 
   * @param newConfidence the new confidence level to set
   */
  public void setConfidence(double newConfidence)
  {
    if (newConfidence > 1.0)
    {
      this.horseConfidence = 1.0;
    }
    if (newConfidence < 0.0)
    {
      this.horseConfidence = 0.0;
    }
    else
    {
       this.horseConfidence = newConfidence; 
    }
  }

  /**
   * Sets the symbol representing the horse.
   * 
   * @param newSymbol the new symbol to set for the horse
   */
  public void setSymbol(char newSymbol)
  {
    this.horseSymbol = newSymbol;
  }

  // Added methods
  public int getLane()
  {
    return this.lane;
  }

  /**
   * Sets the lane number for the horse.
   * 
   * @param n the lane number to set
   */
  public void setLane(int n)
  {
    this.lane = n;
  }

  public void increaseConfidence()
  {
    this.horseConfidence = Math.min(1.0, this.horseConfidence + 0.05);
  }

  public void decreaseConfidence()
  {
    this.horseConfidence = Math.max(0.0, this.horseConfidence - 0.05);
  }

  public void resetFallen()
  {
    this.fallen = false;
  }

  public void setWins(int n)
  {
    this.wins = n;
  }

  public int getWins()
  {
    return this.wins;
  }

  public void setRaces(int n)
  {
    this.races = n;
  }

  public int getRaces()
  {
    return this.races;
  }

    /**
   * Returns the win rate of the horse, calculated as the number of wins divided by the number of races.
   * If the horse has not participated in any races, the win rate is 0.
   * 
   * @return the win rate (wins/races)
   */
  public double getWinRate()
  {
    if (races == 0)
    {
      return 0.0;
    }
    this.win_rate = (double) wins / races;
    return this.win_rate;
  }
}