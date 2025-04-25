import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;


abstract class File_methods
{
  /**
   * Adds horse data to a file by appending to the existing content.
   * 
   * @param horses the list of Horse objects to be written to the file
   * @param file_name the name of the file to which the horse data will be appended
   * @throws IOException if an I/O error occurs during file writing
   */
  public static void addFile_horse(ArrayList<Horse> horses, String file_name) throws IOException
  {
    PrintWriter outputStream = new PrintWriter (new FileWriter(new File(file_name), true));

    for (int i=0; i<horses.size(); i++)
    {
      Horse horse = horses.get(i);

      if (horse == null) continue;

      String horse_name = horse.getName();
      char horse_symbol = horse.getSymbol();
      double horse_confidence = horse.getConfidence();
      int horse_lane = horse.getLane();
      int races = horse.getRaces();
      int wins = horse.getWins();
      double winRating = horse.getWinRate();

      outputStream.println(horse_name + "," + horse_symbol + "," + horse_confidence + "," + horse_lane + "," + races + "," + wins + "," + winRating);
    }

    outputStream.close();
  } // END file_write()

  /**
   * Adds horse data to a file by overwriting the existing content.
   * 
   * @param horses the list of Horse objects to be written to the file
   * @param file_name the name of the file to which the horse data will overwrite the existing content
   * @throws IOException if an I/O error occurs during file writing
   */
  public static void addFile_horse_overwrite(ArrayList<Horse> horses, String file_name) throws IOException
  {
    PrintWriter outputStream = new PrintWriter (new FileWriter(file_name));

    for (int i=0; i<horses.size(); i++)
    {
      Horse horse = horses.get(i);

      if (horse == null) continue;

      String horse_name = horse.getName();
      char horse_symbol = horse.getSymbol();
      double horse_confidence = horse.getConfidence();
      int horse_lane = horse.getLane();
      int races = horse.getRaces();
      int wins = horse.getWins();
      // double winRating = Validation.roundToNDecimalPlaces(horse.getWinRate(), 3);
      double winRating = horse.getWinRate();

      outputStream.println(horse_name + "," + horse_symbol + "," + horse_confidence + "," + horse_lane + "," + races + "," + wins + "," + winRating);
    }

    outputStream.close();
  }

  /**
   * Reads horse data from a file and returns it as a list of Horse objects.
   * 
   * @param file_name the name of the file to read horse data from
   * @return an ArrayList of Horse objects containing data from the file
   * @throws IOException if an I/O error occurs during file reading
   */
  public static ArrayList<Horse> readFile_horse(String file_name) throws IOException
  {
    ArrayList<Horse> horses = new ArrayList<Horse>();
    BufferedReader inputStream = new BufferedReader(new FileReader(file_name));
    String s = inputStream.readLine();
    while (s != null)
    {
      String[] data = s.split(",");
      String name = data[0];
      char symbol = data[1].charAt(0);
      double confidence = Validation2.roundToNDecimalPlaces(Double.parseDouble(data[2]), 3);
      int lane = Integer.parseInt(data[3]);
      int races = Integer.parseInt(data[4]);
      int wins = Integer.parseInt(data[5]);
      horses.add(new Horse(symbol, name, confidence, lane, races, wins));
      s = inputStream.readLine();
    }
    inputStream.close();
    return horses;
  }

  public static void createFile(String file_name) throws IOException
  {
    File file = new File(file_name);
    if (!file.exists())
    {
      file.createNewFile();
    }
    else
    {
      return;
    }
  }

  public static void readHorse(String file_name, String name) throws IOException
  {
    System.out.println("----------------------");
    BufferedReader inputStream = new BufferedReader(new FileReader(file_name));
    String s = inputStream.readLine();
    while (s != null)
    {
      String[] data = s.split(",");
      if (data[0].equals(name))
      {
        String horse_name = data[0];
        double confidence = Validation2.roundToNDecimalPlaces(Double.parseDouble(data[2]), 3);
        double winRating = Validation2.roundToNDecimalPlaces(Double.parseDouble(data[6]), 3);
        System.out.println("Name: " + horse_name);
        System.out.println("Confidence: " + confidence);
        System.out.println("Win rate: " + winRating);
        System.out.println("----------------------");
        s = inputStream.readLine();
      }
      else
      {
        s = inputStream.readLine();
      }
    }
    inputStream.close();
    return;
  }
}