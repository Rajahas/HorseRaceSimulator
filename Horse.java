/**
* Write a description of class Horse here.
* 
* @author (your name) 
* @version (a version number or a date)
*/
public class Horse
{
  //Fields of class Horse
  private String horseName;
  private char horseSymbol;
  private int horseDistance;
  private double horseConfidence;
  private boolean fallen;
  private int lane;
    
  //Constructor of class Horse
  /**
   * Constructor for objects of class Horse
   */
  public Horse(char horseSymbol, String horseName, double horseConfidence, int lane)
  {
    this.horseSymbol = horseSymbol;
    this.horseName = horseName;
    this.horseConfidence = horseConfidence;
    this.horseDistance = 0;
    this.fallen = false;
    this.lane = lane;
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

  public int getLane()
  {
    return this.lane;
  }

  public void setLane(int n)
  {
    this.lane = n;
  }

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
  
  public void setSymbol(char newSymbol)
  {
    this.horseSymbol = newSymbol;
  }
}