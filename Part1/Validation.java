import java.util.Scanner;
public abstract class Validation
{
  // Useful methods that can be used for validation

  /**
   * Rounds a number to a specified number of decimal places.
   * 
   * @param number the number to be rounded
   * @param n the number of decimal places to round to
   * @return the rounded number
   */
  public static double roundToNDecimalPlaces(double number, int n)
  {
    double scale = Math.pow(10, n);
    return Math.round(number * scale) / scale;
  }
  
  /**
   * Prompts the user for input (String) and returns the input as a String.
   * 
   * @param s the Scanner object for reading input
   * @param p the prompt message displayed to the user
   * @return the user input as a String
   */
  public static String input(Scanner s, String p)
  {
    System.out.println(p);
    return s.nextLine();
  }

  /**
   * Prompts the user for input and validates that the input can be parsed as an integer.
   * If the input is invalid, it will prompt the user again until valid input is entered.
   * 
   * @param s the Scanner object for reading input
   * @param p the prompt message displayed to the user
   * @return the user input as an integer
   */
  public static int int_input(Scanner s, String p)
  {
    String input = input(s, p);

    while (number_check(input) == -1)
    {
      input = input(s, "Enter a number: ");
    } // END while

    return Integer.parseInt(input);
  }

  /**
   * Prompts the user for input and validates that the input can be parsed as a double.
   * If the input is invalid, it will prompt the user again until valid input is entered.
   * 
   * @param s the Scanner object for reading input
   * @param p the prompt message displayed to the user
   * @return the user input as a double
   */
  public static double double_input(Scanner s, String p)
  {
    String input = input(s, p);
    while (!isValidDouble(input))
    {
      input = input(s, "Invalid input. Enter a number: ");
    }
    return Double.parseDouble(input);
  }

   /**
   * Validates if the provided string can be parsed as a valid double (positive only).
   * 
   * @param p the input string to validate
   * @return true if the string is a valid double, false otherwise
   */
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

  /**
   * Checks if the string contains only digits, an optional decimal point, and an optional negative sign.
   * 
   * @param p the input string to check
   * @return true if the string matches the pattern, false otherwise
   */
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

  /**
   * Checks if a string represents a negative double.
   * 
   * @param p the input string to check
   * @return true if the string starts with a negative sign, false otherwise
   */
  public static boolean isNegativeDouble(String p)
  {
    // Check if the input starts with a negative sign
    if (p.charAt(0) == '-')
    {
      return true;
    }
    return false;
  }

  /**
   * Checks if a string is empty.
   * 
   * @param p the string to check
   * @return true if the string is empty, false otherwise
   */
  public static boolean isEmpty(String p)
  {
    if (p.length() == 0)
    {
      System.out.println("Cannot be empty");
      return true;
    }
    return false;
  }

  /**
   * Checks if a character is a digit (0-9).
   * 
   * @param c the character to check
   * @return true if the character is a digit, false otherwise
   */
  public static boolean isDigit(char c)
  {
    return Character.isDigit(c);
  }

  /**
   * Checks if an entire string consists of digits only.
   * 
   * @param p the string to check
   * @return true if the string consists only of digits, false otherwise
   */
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

  /**
   * Checks if a number is negative.
   * 
   * @param n the number to check
   * @return true if the number is negative, false otherwise
   */
  public static boolean isNegative(int n)
  {
    if (n < 0)
    {
      System.out.println("Cannot be negative");
      return true;
    }
    return false;
  }

  /**
   * Performs a number check to ensure the string is not empty, consists of digits, and is not negative.
   * 
   * @param p the input string to check
   * @return the parsed number if valid, or -1 if invalid
   */
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

  /**
   * Checks if a number is within a specified range (inclusive).
   * 
   * @param number the number to check
   * @param min the minimum valid value
   * @param max the maximum valid value
   * @return true if the number is within the range, false otherwise
   */
  public static boolean isInRange(int number, int min, int max)
  {
    if (number < min || number > max)
    {
      System.out.println("Number must be between " + min + " and " + max);
      return false;
    }
    return true;
  }

  /**
   * Checks if a number is within a specified range (inclusive) for doubles.
   * 
   * @param number the number to check
   * @param min the minimum valid value
   * @param max the maximum valid value
   * @return true if the number is within the range, false otherwise
   */
  public static boolean isInRange(double number, double min, double max)
  {
    if (number < min || number > max)
    {
      System.out.println("Number must be between " + min + " and " + max);
      return false;
    }
    return true;
  }

  /**
   * Validates if a string is a valid name (only letters and spaces allowed).
   * 
   * @param name the name to validate
   * @return true if the name contains only letters and spaces, false otherwise
   */
  public static boolean isValidName(String name)
  {
    if (name.matches("[a-zA-Z ]+"))
    {
      return true;
    }
    System.out.println("Name can only contain letters and spaces");
    return false;
  }

  /**
   * Validates if a string is a valid symbol (only one character allowed).
   * 
   * @param symbol the symbol to validate
   * @return true if the symbol is a single character, false otherwise
   */
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