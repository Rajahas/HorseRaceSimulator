import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
* A three-horse race, each horse running in its own lane
* for a given distance
* 
* @author McRaceface
* @version 1.0
*/
public class Race
{
  ArrayList<Horse> allHorses = new ArrayList<Horse>();
  private int raceLength;
  private int LANES;
  private final static String horse_file = "horse.csv";
  private static final String horse_history = "horse_history.csv";
  private static final String[] RANDOM_PREFIXES = {"Thunder", "Lightning", "Midnight", "Silver", "Golden", "Dashing", "Flying"};
  private static final String[] RANDOM_SUFFIXES = {"Storm", "Blaze", "Shadow", "Dream", "Star", "Rider", "Chaser"};
  private static final char[] RANDOM_SYMBOLS = {'♞', '♘', '♔', '♕', '♖', '♗', '♙'};
  private Horse winner;
  private MethodTimer2 timer;
  

  public boolean existsHorse(String name)
  {
    for (Horse horse : allHorses)
    {
      if (horse != null && horse.getName().equals(name))
      {
        return true;
      }
    }
    return false;
  }
  
  void setWinner(Horse h)
  {
    this.winner = h;
  }

  public Horse getWinner()
  {
    return this.winner;
  }
  
  /**
 * Adds a randomly generated horse to the first available lane
 */
  public void addRandomHorse()
  {
    int emptyLane = findEmptyLane();
    if (emptyLane == -1)
    {
      System.out.println("No empty lanes available for random horse");
    }
    
    Horse randomHorse = generateRandomHorse(emptyLane + 1); // +1 because lanes are 1-based
    addHorse(randomHorse);
    System.out.println("Added random horse " + randomHorse.getName() + " to lane " + randomHorse.getLane());
  }

  /*
  Finds the first empty lane (0-based index)
  @return index of empty lane, or -1 if no empty lanes
  */
  private int findEmptyLane()
  {
    for (int i = 0; i < LANES; i++)
    {
      if (i >= allHorses.size() || allHorses.get(i) == null)
      {
        return i;
      }
    }
    return -1;
  }

  public Horse generateRandomHorse(int lane)
  {
    Random rand = new Random();
    String name = "Default"; 
    char symbol = 'D'; 
    boolean isUnique = false;
    while (!isUnique)
    {
      String prefix = RANDOM_PREFIXES[rand.nextInt(RANDOM_PREFIXES.length)];
      String suffix = RANDOM_SUFFIXES[rand.nextInt(RANDOM_SUFFIXES.length)];
      symbol = RANDOM_SYMBOLS[rand.nextInt(RANDOM_SYMBOLS.length)];
      name = prefix + " " + suffix;
      isUnique = isNameUnique(name);
    }

    // Generate random confidence between 0.0 and 1.0 (inclusive)
    double confidence = rand.nextDouble();  // Default range is [0.0, 1.0)
    confidence = Validation2.roundToNDecimalPlaces(confidence, 3);
    return new Horse(symbol, name, confidence, lane);
  }
  
  /*
  Fills the first empty lanes with randomly generated horses
  */
  public void fillEmptyLanes()
  {
    for (int i = 0; i < LANES; i++)
    {
      // Check if current lane is empty (null or index beyond current size)
      if (i >= allHorses.size() || allHorses.get(i) == null)
      {
        Horse randomHorse = generateRandomHorse(i + 1); // Lanes are 1-based
        addHorse(randomHorse);
        System.out.println("Added random horse " + randomHorse.getName() + " to lane " + randomHorse.getLane());
        return;
      }
    }
  }
  
  /*
  * Checks if a horse name is unique in the current race
  * @param name the name to check
  * @return true if unique, false otherwise
  */
  public boolean isNameUnique(String name)
  {
    for (Horse horse : allHorses)
    {
      if (horse != null && horse.getName().equals(name))
      {
        return false; // Not unqiue
      }
    }
    return true; // Name not found, is unique
  }


  /**
   * Constructor for objects of class Race.
   * Initializes the race with a distance and number of lanes.
   * Creates necessary files for horses.
   * 
   * @param distance the length of the racetrack (in meters/yard)
   * @param lanes the number of lanes in the race
   * @throws IOException if there is an issue with file creation
   */
  public Race(int distance, int lanes) throws IOException
  {
    File_methods.createFile(horse_file);
    File_methods.createFile(horse_history);
      
    if ((distance > 0) && (lanes >= 2))
    {
      raceLength = distance;
      setLanes(lanes);
      startLanes();
      loadHorsesFromFile(horse_file);
      this.timer = new MethodTimer2();
    }
    else
    {
      System.out.println("Race length must be a positive integer.");
      System.out.println("Setting default length to 10. \n Default 5 lanes.");
      raceLength = 10; // Default race length
      setLanes(5); // Default lane amount
      startLanes();
      loadHorsesFromFile(horse_file);
      this.timer = new MethodTimer2();
    }
  }

  /**
   * Initializes all the lanes by adding null values (empty lanes).
   */
  public void startLanes()
  {
    for (int i=0; i < LANES; i++)
    {
      allHorses.add(null);
    }
  }

  public void setLanes(int lanes)
  {
    if (lanes > 0)
    {
      this.LANES = lanes;
    }
    else
    {
      System.out.println("number of lanes have not been changed");
    }
  }

  /**
   * Adds a horse to the race in a specified lane.
   * 
   * @param theHorse the horse to be added to the race
   */
  public void addHorse(Horse theHorse)
  {
    // Check if horse name is unique
    if (!isNameUnique(theHorse.getName()))
    {
      System.out.println("A horse with the name '" + theHorse.getName() + "' already exists in the race.");
      return;
    }
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
    increaseRace();

    // Reset and then Start the timer
    timer.reset();
    timer.start();

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
            timer.stop();
            temp.increaseConfidence();  // Increase confidence when they win
            System.out.println("\nAnd the winner is… " + temp.getName() + "!");
            temp.setWins(temp.getWins() + 1);
            setWinner(temp);
            timer.printDuration();
            break;
          }
        }
        finished = true;
      }

      if (allHorsesFallen())
      {
        timer.stop();
        System.out.println("All horses have fallen! The race is over.");
        timer.printDuration();
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
   * Determines if a horse has won the race.
   * 
   * @param theHorse the horse to check
   * @return true if the horse has won, false otherwise
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
  /**
   * Prints a single lane, showing the position of the horse or empty if no horse.
   * 
   * @param theHorse the horse in this lane, or null if the lane is empty
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

  /**
   * Prints a given character multiple times.
   * 
   * @param aChar the character to print
   * @param times the number of times to print it
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

  /**
   * Checks if any horse has won the race.
   * 
   * @return true if any horse has won, false otherwise
   */
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

  /**
   * Removes a horse from the race based on its name.
   * 
   * @param name the name of the horse to remove
   */
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

  /**
   * Checks if all horses have fallen during the race.
   * 
   * @return true if all horses have fallen, false otherwise
   */
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

  /**
   * Displays the statistics for each horse after the race.
   */
  public void showHorseStats()
  {
    for (int i=0; i<allHorses.size(); i++)
    {
      Horse temp = allHorses.get(i);
      if (temp == null) continue;
      String t1 = "Name: " + temp.getName();
      double x = Validation2.roundToNDecimalPlaces(temp.getConfidence(), 3);
      String t2 = "Confidence: " + x;
      double winRating = 0;
      if (temp.getRaces() != 0)
      {
        winRating = temp.getWinRate();
      }
      winRating = temp.getWinRate();
      winRating = Validation2.roundToNDecimalPlaces(winRating, 3);
      String t3 = "Win rate: " + winRating;
      String t4 = "Lane: " + temp.getLane();
      String t5 = t1 + " " + t2 + " " + t3 + " " + t4;
      System.out.println(t5);
    }
  }

  void loadHorsesFromFile(String file_name) throws IOException
  {
    allHorses = File_methods.readFile_horse(file_name);
  }

  // appending method
  public void saveHorsesToFile(String file_name) throws IOException
  {
    File_methods.addFile_horse(allHorses, file_name);
  }

  // overwititng method
  public void overwriteHorsesToFile(String file_name) throws IOException
  {
    File_methods.addFile_horse_overwrite(allHorses, file_name);
  }

  public int getLanes()
  {
    return LANES;
  }

  public void increaseRace()
  {
    for (int i=0; i< allHorses.size(); i++)
    {
      Horse h = allHorses.get(i);
      if (h == null) continue;
      h.setRaces(h.getRaces() + 1);
    }
  }

  public ArrayList<Horse> getHorses()
  {
    return allHorses;
  }

  public void horseData(String name) throws IOException
  {
    File_methods.readHorse(horse_history, name);
  }
}