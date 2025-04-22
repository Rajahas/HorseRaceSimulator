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
    }
    else
    {
      System.out.println("Race length must be a positive integer. Setting default length to 100.");
      raceLength = 10; // Default race length
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
    allHorses.add(theHorse.getLane(), theHorse);
  }

  public void removeHorse(String name)
  {
    for (int i = 0; i < allHorses.size(); i++)
    {
      if (allHorses.get(i).getName().equals(name))
      {
        allHorses.remove(i);
        System.out.println("Horse " + name + " has been removed from the race.");
        LANES -= 1;
        return;
      }
    }
    
    System.out.println("No horse found with the name " + name + ".");
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

    // NAme of list of horses is allHorses
    
    Horse lane1Horse = new Horse('a', "wrd1", 0.5, 1);
    Horse lane2Horse = new Horse('b', "wrd2", 0.5, 2);
    Horse lane3Horse = new Horse('c', "wrd3", 0.5, 3);
    
    addHorse(lane1Horse);
    addHorse(lane2Horse);
    addHorse(lane3Horse);
    
    //reset all the lanes (all horses not fallen and back to 0). 
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
          if (raceWonBy(allHorses.get(i)))
          {
            allHorses.get(i).increaseConfidence();  // Increase confidence when they win
            break;
          }
        }
        finished = true;
      }
       
      //wait for 100 milliseconds
      try
      { 
        TimeUnit.MILLISECONDS.sleep(100);
      }
      catch(Exception e)
      {
        // TO DO
      }
    }
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
    if  (!theHorse.hasFallen())
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
    System.out.print('\u000C');  //clear the terminal window
    
    multiplePrint('=',raceLength+3); //top edge of track
    System.out.println();

    int temp = 0;
    while (temp < LANES)
    {
      printLane(allHorses.get(temp));
      System.out.println();
      temp += 1;
    }
    
    multiplePrint('=',raceLength+3); //bottom edge of track
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
      allHorses.get(i).goBackToStart();  // Call backToStart for each horse
    }
  }

  public void moveAllHorses()
  {
    for (int i = 0; i < allHorses.size(); i++)
    {
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
}
