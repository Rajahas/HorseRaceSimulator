import java.util.Scanner;

class Main
{
  public static void main(String[] args)
  {
    Scanner s = new Scanner(System.in);
    int distance = int_input(s, "Track length");
    int num_lanes = int_input(s, "Number of lanes");
    Race race = new Race(distance, num_lanes);
    
    boolean isRunning = true;

    while (isRunning)
    {
      // Display the menu options
      printMenu();

      // Get user input for the menu choice
      int choice = int_input(s, "Your choice: ");
      
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
        race.startRace();
      } 
      else if (choice == 5)
      {
        race.showHorseStats();
      } 
      else if (choice == 6)
      {
        int temp = int_input(s, "How many lanes should there be");
        if (temp < num_lanes)
        {
          System.out.println("Cant remove lanes");
        }
        else
        {
          race.setLanes(temp);
          System.out.println("There are now " + temp + " lanes");
        }
      } 
      else if (choice == 7)
      {
        System.out.println("Exiting the program. Goodbye!");
        isRunning = false;  // Stop the loop and exit
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
    System.out.println("[2] Add a Horse to a Lane");
    System.out.println("[3] Remove a Horse from the Race");
    System.out.println("[4] Start the Race");
    System.out.println("[5] Show Horse Stats");
    System.out.println("[6] Add more lanes");
    System.out.println("[7] Exit");
  }

  // Method to create a horse
  public static void createHorse(Scanner scanner, Race race)
  {
    // Prompt user to input horse details
    String name = input(scanner, "Enter the horse's name:");
    char symbol = input(scanner, "Enter the horse's symbol:").charAt(0);
    double confidence = double_input(scanner, "Enter the horse's confidence (0.0 to 1.0):");
    int lane = int_input(scanner, "Enter the lane number (1 to 5):");
    
    // Create a new horse
    Horse newHorse = new Horse(symbol, name, confidence, lane);
    System.out.println("Horse " + name + " created successfully!");
    
    // Add horse to the race
    race.addHorse(newHorse);
  }

  // Method to add a horse to a lane
  public static void addHorseToLane(Scanner scanner, Race race)
  {
    String name = input(scanner, "Enter the horse's name to add to a lane:");
    for (int i = 0; i < race.allHorses.size(); i++)
    {
      Horse horse = race.allHorses.get(i);
      if (horse == null) continue;
      if (horse.getName().equals(name))
      {
        int lane = int_input(scanner, "Enter the lane number (1 to 5) for " + name + ":");
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
    String name = input(scanner, "Enter the name of the horse to remove:");
    race.removeHorse(name);  // Remove the horse from the race
  }

  // Method to get user input (String)
  public static String input(Scanner s, String p)
  {
    System.out.println(p);
    return s.nextLine();
  }

  // Method to get user input as an integer
  public static int int_input(Scanner s, String p)
  {
    String t = input(s, p);
    return Integer.parseInt(t);
  }

  // Method to get user input as a double
  public static double double_input(Scanner s, String p)
  {
    String t = input(s, p);
    return Double.parseDouble(t);
  }
}

Main.main(null);