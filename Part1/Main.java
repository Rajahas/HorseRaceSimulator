import java.io.IOException;
import java.util.Scanner;

public class Main
{
  public static void main(String[] args) throws IOException
  {
    Scanner s = new Scanner(System.in);
    final double BALANCE = 100.0;
    Person player = new Person("Sample", BALANCE);
    int distance = Validation.int_input(s, "Track length");
    while (distance <= 0)
    {
      System.out.println("Distance must be greater than 0. Please try again.");
      distance = Validation.int_input(s, "Track length");
    }
    int num_lanes = Validation.int_input(s, "Number of lanes");
    while (num_lanes <= 0)
    {
      System.out.println("Number of lanes must be greater than 0. Please try again.");
      num_lanes = Validation.int_input(s, "Enter the number of lanes:");
    }
    Race race = new Race(distance, num_lanes);

    boolean isRunning = true;

    while (isRunning)
    {
      // Display the menu options
      printMenu();

      // Get user input for the menu choice
      int choice = Validation.int_input(s, "Your choice: ");

      // Handle user choice using if statements
      if (choice == 1)
      {
        createHorse(s, race);
      } 
      else if (choice == 2)
      {
        addHorseToLane(s, race);
      } 
      else if (choice == 3)
      {
        removeHorseFromRace(s, race);
      } 
      else if (choice == 4)
      {
        player.showBalance();
        String wantToBet = Validation.input(s, "Do you want to place a bet before the race? (yes/no)");
        if (wantToBet.equals("yes"))
        {
          placeBet(s, player, race);
        }
        race.startRace();
        if (wantToBet.equals("yes"))
        {
          processBetResults(player, race);
        }
        race.overwriteHorsesToFile("horse.csv");
        File_methods.addFile_horse(race.getHorses(), "horse_history.csv");
      } 
      else if (choice == 5)
      {
        race.showHorseStats();
      } 
      else if (choice == 6)
      {
        int temp = Validation.int_input(s, "Enter the new number of lanes:");
        if (temp < num_lanes)
        {
          System.out.println("Cannot reduce the number of lanes. Please enter a number greater than or equal to " + num_lanes + ".");
        }
        else
        {
          race.setLanes(temp);
          System.out.println("There are now " + temp + " lanes.");
        }
      }
      else if (choice == 7)
      {
        String horse_name = Validation.input(s, "Enter the horse's name:");
        race.horseData(horse_name);
        
      }
      else if (choice == 8)
      {
        System.out.println("Exiting the program. Goodbye!");
        isRunning = false;  // Stop the loop and exit
      }
      else if (choice == 9)
      {
        race.addRandomHorse();
      }
      else
      {
        System.out.println("Invalid option. Please try again.");
      }
    }
  }

  // Method to print the menu options
  public static void printMenu()
  {
    System.out.println("\nMenu:");
    System.out.println("[1] Create a Horse");
    System.out.println("[2] Move a Horse to a Lane");
    System.out.println("[3] Remove a Horse from the Race");
    System.out.println("[4] Start the Race");
    System.out.println("[5] Show Horse Stats");
    System.out.println("[6] Add more lanes");
    System.out.println("[7] Horse Data");
    System.out.println("[8] Exit");
    System.out.println("[9] Add Random Horses");
  }

  // Method to create a horse
  public static void createHorse(Scanner scanner, Race race)
  {
    // Prompt user to input horse details
    String name = Validation.input(scanner, "Enter the horse's name:");
    while (!race.isNameUnique(name))
    {
      System.out.println("A horse with that name already exists. Please choose another name.");
      name = Validation.input(scanner, "Enter the horse's name:");
    }
    
    char symbol = Validation.input(scanner, "Enter the horse's symbol:").charAt(0);
    double confidence = Validation.double_input(scanner, "Enter the horse's confidence (0.0 to 1.0):");
    while (!Validation.isInRange(confidence, 0.0, 1.0))
    {
      confidence = Validation.double_input(scanner, "Enter the horse's confidence (0.0 to 1.0):");
    }
    int lane = Validation.int_input(scanner, "Enter the lane number (1 to " + race.getLanes() + "):");
    while (!Validation.isInRange(lane, 1, race.getLanes()))
    {
      System.out.println("Invalid lane number. Please enter a number between 1 and " + race.getLanes() + ".");
      lane = Validation.int_input(scanner, "Enter the lane number (1 to " + race.getLanes() + "):");
    }

    // Create a new horse
    Horse newHorse = new Horse(symbol, name, confidence, lane);
    System.out.println("Horse " + name + " created successfully!");

    // Add horse to the race
    race.addHorse(newHorse);
  }

  // Method to add a horse to a lane
  public static void addHorseToLane(Scanner scanner, Race race)
  {
    String name = Validation.input(scanner, "Enter the horse's name to add to a lane:");
    for (int i = 0; i < race.getLanes(); i++)
    {
      Horse horse = race.allHorses.get(i);
      if (horse == null) continue;
      if (horse.getName().equals(name))
      {
        int lane = Validation.int_input(scanner, "Enter the lane number (1 to " + race.getLanes() + ") for " + name + ":");
        while (!Validation.isInRange(lane, 0, race.getLanes()))
        {
          System.out.println("Invalid lane number. Please enter a number between 1 and " + race.getLanes() + ".");
          lane = Validation.int_input(scanner, "Enter the lane number (1 to " + race.getLanes() + ") for " + name + ":");
          return;
        }
        horse.setLane(lane);
        System.out.println(name + " has been added to lane " + lane + "!");
        return;
      }
    }
    System.out.println("No horse found");
    return;
  }

  // Method to remove a horse from the race
  public static void removeHorseFromRace(Scanner scanner, Race race)
  {
    String name = Validation.input(scanner, "Enter the name of the horse to remove:");
    race.removeHorse(name);  // Remove the horse from the race
  }

  public static void placeBet(Scanner scanner, Person player, Race race)
  {
    if (race.getHorses().isEmpty())
    {
      System.out.println("No horses in race to bet on!");
      return;
    }

    // Show available horses
    System.out.println("\nAvailable horses:");
    for (Horse horse : race.getHorses())
    {
      if (horse != null)
      {
        System.out.println("- " + horse.getName() + " Confidence: " + horse.getConfidence());
      }
    }

    // Get bet details
    String horseName = Validation.input(scanner, "Enter horse name to bet on:");
    while(!race.existsHorse(horseName))
    {
      System.out.println("Invalid horse choice");
      horseName = Validation.input(scanner, "Enter horse name to bet on:");
    }
    double amount = Validation.double_input(scanner, "Enter amount to bet:");
    while ((amount > player.getBalance()) || amount <= 0)
    {
      System.out.println("Invalid amount");
      amount = Validation.double_input(scanner, "Enter amount to bet:");
    }
    player.setBet(horseName);
    player.decreaseBalance(amount);
    player.setBettingAmount(amount);
  }
  
  public static void processBetResults(Person player, Race race)
  {
    // Determine winning horse
    Horse winner = race.getWinner();
    double change = ((1 + winner.getConfidence()) * player.getBettingAmount());
    if (player.getBet().equals(race.getWinner().getName()))
    {
      player.increaseBalance(change);
    }
    else
    {
      player.decreaseBalance(change);
    }
    player.showBalance();
  }
}