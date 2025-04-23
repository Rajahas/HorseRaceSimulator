import java.util.Scanner;

abstract class Validation
{

  public static double roundToNDecimalPlaces(double number, int n)
  {
    double scale = Math.pow(10, n);
    return Math.round(number * scale) / scale;
  }
  
  // Useful methods that can be used for validation
  //
  // Method to get user input (String)
  public static String input(Scanner s, String p)
  {
    System.out.println(p);
    return s.nextLine();
  }

  // Method to get user input as an integer
  public static int int_input(Scanner s, String p)
  {
    String input = input(s, p);
  
    while (number_check(input) == -1)
    {
      input = input(s, "Enter a number: ");
    } // END while
  
    return Integer.parseInt(input);
  }

  // Method to get user input as a double with validation
  public static double double_input(Scanner s, String p)
  {
    String input = input(s, p);
    while (!isValidDouble(input))
    {
      input = input(s, "Invalid input. Enter a number: ");
    }
    return Double.parseDouble(input);
  }

  // Method to check if a string is a valid double
  public static boolean isValidDouble(String p)
  {
    // Check if the input is empty
    if (isEmpty(p))
    {
      System.out.println("Cannot be empty.");
      return false;
    }

    // Check if the input contains only digits, optional decimal point, and optional negative sign
    if (!isAllDigitsOrDecimal(p))
    {
      System.out.println("Invalid characters. Only digits, decimal point, and optional negative sign are allowed.");
      return false;
    }

    // Check if the number is negative (if applicable)
    if (isNegativeDouble(p))
    {
      System.out.println("Cannot be negative.");
      return false;
    }

    // If all checks pass, the input is valid
    return true;
  }

  // Method to check if a string contains only digits, optional decimal point, and optional negative sign
  public static boolean isAllDigitsOrDecimal(String p)
  {
    int decimalCount = 0; // Track the number of decimal points

    for (int i = 0; i < p.length(); i++)
    {
      char c = p.charAt(i);

      // Allow negative sign only at the beginning
      if (i == 0 && c == '-')
      {
          continue;
      }

      // Allow digits
      if (Character.isDigit(c))
      {
          continue;
      }

      // Allow one decimal point
      if (c == '.')
      {
          decimalCount++;
          if (decimalCount > 1)
          {
              return false; // More than one decimal point is invalid
          }
          continue;
      }
      
      // If any other character is found, the input is invalid
      return false;
    }
    return true;
  }

  // Method to check if a double is negative
  public static boolean isNegativeDouble(String p)
  {
    // Check if the input starts with a negative sign
    if (p.charAt(0) == '-')
    {
      return true;
    }
    return false;
  }
  
  // Method to check if a string is empty
  public static boolean isEmpty(String p)
  {
    if (p.length() == 0)
    {
      System.out.println("Cannot be empty");
      return true;
    }
    return false;
  }

  // Method to check if a character is a digit from 0-9
  public static boolean isDigit(char c)
  {
    return Character.isDigit(c);
  }

  // Method to check if an entire string is all digits
  public static boolean isAllDigit(String p)
  {
    for (int i = 0; i < p.length(); i++)
    {
      char character = p.charAt(i);
      if (!isDigit(character))
      {
        System.out.println("Not all digits");
        return false;
      }
    }
    return true;
  }

  // Method to check if a given number is negative
  public static boolean isNegative(int n)
  {
    if (n < 0)
    {
      System.out.println("Cannot be negative");
      return true;
    }
    return false;
  }

  // Method to perform a number check
  public static int number_check(String p)
  {
    // Check if the string is empty
    if (isEmpty(p))
    {
      return -1; // Return -1 if the string is empty
    }

    // Check if the string contains all digits
    if (!isAllDigit(p))
    {
      return -1; // Return -1 if not all characters are digits
    }

    // Convert the string to an integer
    int number = Integer.parseInt(p);

    // Check if the number is negative
    if (isNegative(number))
    {
      return -1; // Return -1 if the number is negative
    }

    // If all checks pass, return the positive number
    return number;
  }

  // Method to check if a number is within a specific range
  public static boolean isInRange(int number, int min, int max)
  {
    if (number < min || number > max)
    {
      System.out.println("Number must be between " + min + " and " + max);
      return false;
    }
    return true;
  }

  // Method to check if a number is within a specific range
  public static boolean isInRange(double number, double min, double max)
  {
    if (number < min || number > max)
    {
      System.out.println("Number must be between " + min + " and " + max);
      return false;
    }
    return true;
  }

  // Method to check if a string is a valid name (letters and spaces only)
  public static boolean isValidName(String name)
  {
    if (name.matches("[a-zA-Z ]+"))
    {
      return true;
    }
    System.out.println("Name can only contain letters and spaces");
    return false;
  }

  // Method to check if a string is a valid symbol (single character)
  public static boolean isValidSymbol(String symbol)
  {
    if (symbol.length() == 1)
    {
      return true;
    }
    System.out.println("Symbol must be a single character");
    return false;
  }
}