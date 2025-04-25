import java.util.ArrayList;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public abstract class File_methods2 {
  public static void addFile_horse(ArrayList<Horse2> horses, String file_name) throws IOException {
    PrintWriter outputStream = new PrintWriter(new FileWriter(new File(file_name), true));
    for (Horse2 horse : horses) {
      if (horse == null) continue;
      outputStream.println(
        horse.getName()   + "," +
        horse.getSymbol() + "," +
        horse.getConfidence() + "," +
        horse.getLane()   + "," +
        horse.getRaces()  + "," +
        horse.getWins()   + "," +
        horse.getWinRate() + "," +
        horse.getBreed()  + "," +
        horse.getSaddle() + "," +
        horse.getCoatColor()
      );
    }
    outputStream.close();
  }

  public static void addFile_horse_overwrite(ArrayList<Horse2> horses, String file_name) throws IOException {
    PrintWriter outputStream = new PrintWriter(new FileWriter(file_name));
    for (Horse2 horse : horses) {
      if (horse == null) continue;
      outputStream.println(
        horse.getName()   + "," +
        horse.getSymbol() + "," +
        horse.getConfidence() + "," +
        horse.getLane()   + "," +
        horse.getRaces()  + "," +
        horse.getWins()   + "," +
        horse.getWinRate() + "," +
        horse.getBreed()  + "," +
        horse.getSaddle() + "," +
        horse.getCoatColor()
      );
    }
    outputStream.close();
  }

  public static ArrayList<Horse2> readFile_horse(String file_name) throws IOException {
    ArrayList<Horse2> horses = new ArrayList<>();
    BufferedReader inputStream = new BufferedReader(new FileReader(file_name));
    String s = inputStream.readLine();
    while (s != null) {
      String[] data = s.split(",");
      // expect 10 columns now
      String name        = data[0];
      char   symbol      = data[1].charAt(0);
      double confidence  = Validation2.roundToNDecimalPlaces(Double.parseDouble(data[2]), 3);
      int    lane        = Integer.parseInt(data[3]);
      int    races       = Integer.parseInt(data[4]);
      int    wins        = Integer.parseInt(data[5]);
      String breed       = data[7];
      String saddle      = data[8];
      String coatColor   = data[9];
      horses.add(new Horse2(symbol, name, confidence, lane, races, wins, breed, saddle, coatColor));
      s = inputStream.readLine();
    }
    inputStream.close();
    return horses;
  }

  public static void createFile(String file_name) throws IOException {
    File file = new File(file_name);
    if (!file.exists()) file.createNewFile();
  }

  public static void readHorse(String file_name, String name) throws IOException {
    System.out.println("----------------------");
    BufferedReader inputStream = new BufferedReader(new FileReader(file_name));
    String s = inputStream.readLine();
    while (s != null) {
      String[] data = s.split(",");
      if (data[0].equals(name)) {
        System.out.println("Name:       " + data[0]);
        System.out.println("Confidence: " + Validation2.roundToNDecimalPlaces(Double.parseDouble(data[2]), 3));
        System.out.println("Win rate:   " + Validation2.roundToNDecimalPlaces(Double.parseDouble(data[6]), 3));
        System.out.println("Breed:      " + data[7]);
        System.out.println("Saddle:     " + data[8]);
        System.out.println("Coat Color: " + data[9]);
        System.out.println("----------------------");
      }
      s = inputStream.readLine();
    }
    inputStream.close();
  }
}
