import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

/**
* Write a description of class Horse here.
* 
* @author (your name) 
* @version (a version number or a date)
*/
class Horse
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

  //Constructor of class Horse
  /**
   * Constructor for objects of class Horse
   */
  // Added methods to overload
  public Horse(char horseSymbol, String horseName, double horseConfidence, int lane, int races, int wins)
  {
    this.horseSymbol = horseSymbol;
    this.horseName = horseName;
    this.setConfidence(horseConfidence);
    this.horseDistance = 0;
    this.fallen = false;
    this.lane = lane;
    this.wins = races;
    this.races = wins;
  }

  // Default constructor
  public Horse(char horseSymbol, String horseName, double horseConfidence, int lane)
  {
    this.horseSymbol = horseSymbol;
    this.horseName = horseName;
    this.setConfidence(horseConfidence);
    this.horseDistance = 0;
    this.fallen = false;
    this.lane = lane;
    this.wins = 0;
    this.races = 0;
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

  // Added methods
  public int getLane()
  {
    return this.lane;
  }

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
}
