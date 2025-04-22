import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.ArrayList;

/**
* A three-horse race, each horse running in its own lane
* for a given distance
* 
* @author McRaceface
* @version 1.0
*/
public class Race
{
  private int raceLength;
  private int LANES;
  ArrayList<Horse> allHorses = new ArrayList<Horse>();

  /**
   * Constructor for objects of class Race
   * Initially there are no horses in the lanes
   * 
   * @param distance the length of the racetrack (in metres/yards...)
   */
  public Race(int distance, int lanes)
  {
    if ((distance > 0) && (lanes > 0))
    {
      raceLength = distance;
      setLanes(lanes);
      startLanes();
    }
    else
    {
      System.out.println("Race length must be a positive integer. Setting default length to 10.");
      raceLength = 10; // Default race length
      setLanes(5); // Default lane amount
      startLanes();
    }
  }

  public void startLanes()
  {
    for (int i=1; i <= LANES; i++)
    {
      allHorses.add(null);
    }
  }

  public void setLanes(int lanes)
  {
    if (lanes > 0)
    {
      LANES = lanes;
    }
    else
    {
      System.out.println("number of lanes have not been changed");
    }
  }
  
  /**
   * Adds a horse to the race in a given lane
   * 
   * @param theHorse the horse to be added to the race
   * @param laneNumber the lane that the horse will be added to
   */
  public void addHorse(Horse theHorse)
  {
    int laneIndex = theHorse.getLane() - 1; // Convert lane to 0-based index

    while (allHorses.size() <= laneIndex)
    {
      allHorses.add(null);  // Fill empty slots with null if needed
    }

    // Place the horse in the correct lane (or replace a null spot)
    allHorses.set(laneIndex, theHorse);
  }
  
  /**
   * Start the race
   * The horse are brought to the start and
   * then repeatedly moved forward until the 
   * race is finished
   */
  public void startRace()
  {
    //declare a local variable to tell us when the race is finished
    boolean finished = false;
    
    // reset all the lanes (all horses not fallen and back to 0). 
    resetAllHorses();
                  
    while (!finished)
    {
      //move each horse
      moveAllHorses();
                  
      //print the race positions
      printRace();
        
      //if any of the three horses has won the race is finished
      if (raceWonByAnyHorse())
      {
        for (int i = 0; i < allHorses.size(); i++)
        {
          Horse temp = allHorses.get(i);
          if (temp == null) continue;
          if (raceWonBy(temp))
          {
            temp.increaseConfidence();  // Increase confidence when they win
            System.out.println("\nAnd the winner is… " + temp.getName() + "!");
            break;
          }
        }
        finished = true;
      }

      if (allHorsesFallen())
      {
        System.out.println("All horses have fallen! The race is over.");
        finished = true;
        break;
      }
       
      //wait for 100 milliseconds
      try
      { 
        TimeUnit.MILLISECONDS.sleep(200);
      }
      catch(Exception e)
      {
        // TO DO
      }
    }
    showHorseStats();
  }
  
  /**
   * Randomly make a horse move forward or fall depending
   * on its confidence rating
   * A fallen horse cannot move
   * 
   * @param theHorse the horse to be moved
   */
  private void moveHorse(Horse theHorse)
  {
    //if the horse has fallen it cannot move, 
    //so only run if it has not fallen

    if (theHorse == null)
    {
      return;
    }
    if (!theHorse.hasFallen())
    {
      //the probability that the horse will move forward depends on the confidence;
      if (Math.random() < theHorse.getConfidence())
      {
        theHorse.moveForward();
      }
          
      //the probability that the horse will fall is very small (max is 0.1)
      //but will also will depends exponentially on confidence 
      //so if you double the confidence, the probability that it will fall is *2
      if (Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence()))
      {
        theHorse.fall();
        theHorse.decreaseConfidence(); // Decrease confidence of a fallen horse
      }
    }
  }
      
  /** 
   * Determines if a horse has won the race
   *
   * @param theHorse The horse we are testing
   * @return true if the horse has won, false otherwise.
   */
  private boolean raceWonBy(Horse theHorse)
  {
    if (theHorse == null)
    {
      return false;
    }
    if (theHorse.getDistanceTravelled() == raceLength)
    {
      return true;
    }
    else
    {
      return false;
    }
  }
  
  /***
   * Print the race on the terminal
   */
  private void printRace()
  {
    System.out.print('\u000C');  // Clear the terminal window
    
    multiplePrint('=', raceLength + 3); // Top edge of track
    System.out.println();

    // Iterate through each lane
    for (int lane = 1; lane <= LANES; lane++)
    {
      Horse horseInLane = null;
      
      // Find the horse in the current lane
      for (Horse horse : allHorses)
      {
        if (horse != null && horse.getLane() == lane)
        {
          horseInLane = horse;
          break;
        }
      }
      
      // Print the lane with the horse (or empty if no horse is in this lane)
      printLane(horseInLane);
      System.out.println();
    }
    
    multiplePrint('=', raceLength + 3); // Bottom edge of track
    System.out.println();    
  }
  
  /**
   * print a horse's lane during the race
   * for example
   * |           X                      |
   * to show how far the horse has run
   */
  private void printLane(Horse theHorse)
  {
    // If the lane is empty, print "Empty Lane" and the rest as empty space
    if (theHorse == null)
    {
      System.out.print("| Empty Lane");
      multiplePrint(' ', raceLength - 10); // Adjust space to keep formatting
      System.out.print("|");
      return;
    }
    
    //calculate how many spaces are needed before
    //and after the horse
    int spacesBefore = theHorse.getDistanceTravelled();
    int spacesAfter = raceLength - theHorse.getDistanceTravelled();
    
    //print a | for the beginning of the lane
    System.out.print('|');
    
    //print the spaces before the horse
    multiplePrint(' ',spacesBefore);
    
    // if the horse has fallen then print dead
    //else print the horse's symbol
    if (theHorse.hasFallen())
    {
        System.out.print('\u2322');
    }
    else
    {
        System.out.print(theHorse.getSymbol());
    }
    
    //print the spaces after the horse
    multiplePrint(' ',spacesAfter);
    
    //print the | for the end of the track
    System.out.print('|');
  }
      
  
  /***
   * print a character a given number of times.
   * e.g. printmany('x',5) will print: xxxxx
   * 
   * @param aChar the character to Print
   */
  private void multiplePrint(char aChar, int times)
  {
    int i = 0;
    while (i < times)
    {
        System.out.print(aChar);
        i = i + 1;
    }
  }

  // Methods added to improve code
  //
  public void resetAllHorses()
  {
    for (int i = 0; i < allHorses.size(); i++)
    {
      Horse current = allHorses.get(i);
      if (current == null)
      {
        continue;
      }
      else
      {
        current.resetFallen(); // Rise again
        current.goBackToStart(); // Back to start of race
      }
    }
  }

  public void moveAllHorses()
  {
    for (int i = 0; i < allHorses.size(); i++)
    {
      
      Horse temp = allHorses.get(i);
      if (temp == null) continue;
      moveHorse(allHorses.get(i));  // Call moveHorse for each horse
    }
  }

  public boolean raceWonByAnyHorse()
  {
    // Iterate through all horses and check if any has won
    for (int i = 0; i < allHorses.size(); i++)
    {
      if (raceWonBy(allHorses.get(i)))
      {
        return true;  // If a horse has won, return true
      }
    }
    return false;  // No horse has won yet
  }

  public void removeHorse(String name)
  {
    for (int i = 0; i < allHorses.size(); i++)
    {
      Horse horse = allHorses.get(i);
      if (horse != null && horse.getName().equals(name))
      {
        allHorses.set(i, null); // Leave an empty lane instead of shifting
        System.out.println("Horse " + name + " has been removed from the race.");
        return;
      }
    }
    
    System.out.println("No horse found with the name " + name + ".");
  }

  private boolean allHorsesFallen()
  {
    for (int i=0; i<allHorses.size(); i++)
    {
      Horse temp = allHorses.get(i);
      if (temp == null) continue;
      if (!temp.hasFallen())
      {
        return false; // At least one horse is still running
      }
    }
    return true; // All horses have fallen
  }

  public void showHorseStats()
  {
    for (int i=0; i<allHorses.size(); i++)
    {
      Horse temp = allHorses.get(i);
      if (temp == null) continue;
      String t1 = "Name: " + temp.getName();
      String t2 = "Confidence: " + temp.getConfidence();
      String t3 = "Lane: " + temp.getLane();
      String t4 = t1 + " " + t2 + " " + t3;
      System.out.println(t4);
    }
  }
}
