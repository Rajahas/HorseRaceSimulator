import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

abstract class File_methods
{
  // A method to write to a file
  //
  // appending 
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

      outputStream.println(horse_name + "," + horse_symbol + "," + horse_confidence + "," + horse_lane + "," + races + "," + wins);
    }

    outputStream.close();
  } // END file_write()

  // This method will overwrite whats in the file
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

      outputStream.println(horse_name + "," + horse_symbol + "," + horse_confidence + "," + horse_lane + "," + races + "," + wins);
    }

    outputStream.close();
  }

  // a method to read from files and make a leaderboard
  //
  public static ArrayList<Horse> readFile_horse(String file_name) throws IOException
  {
    ArrayList<Horse> horses = new ArrayList<Horse>();
    BufferedReader inputStream = new BufferedReader(new FileReader(file_name));
    String s = inputStream.readLine();
    while (s != null)
    {
      // code to do
      String[] data = s.split(",");
      String name = data[0];
      char symbol = data[1].charAt(0);
      double confidence = Validation.roundToNDecimalPlaces(Double.parseDouble(data[2]), 3);
      int lane = Integer.parseInt(data[3]);
      int races = Integer.parseInt(data[4]);
      int wins = Integer.parseInt(data[5]);
      horses.add(new Horse(symbol, name, confidence, lane, races, wins));
      s = inputStream.readLine();
    }
    inputStream.close();
    return horses;
  }
}
