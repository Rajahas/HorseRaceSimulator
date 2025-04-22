abstract class File_methods
{
  // A method to write to a file
  //
  public static void addFile_horse(ArrayList<Horse> horses) throws IOException
  {
    String file_name = "horse.csv";
    PrintWriter outputStream = new PrintWriter (new FileWriter(new File(file_name), true));

    for (int i=0; i<horses.size(); i++)
    {
      Horse horse = horses.get(i);

      if (horse == null) continue;

      String horse_name = horse.getName();
      char horse_symbol = horse.getSymbol();
      double horse_confidence = horse.getConfidence();
      int horse_lane = horse.getLane();

      outputStream.println(horse_name + "," + horse_symbol + "," + horse_confidence + "," + horse_lane);
    }
    
    outputStream.close();
  } // END file_write()

  // a method to read from files and make a leaderboard
  //
  public static ArrayList<Horse> readFile_horse() throws IOException
  {
    ArrayList<Horse> horses = new ArrayList<>();
    String file_name = "horse.csv";
    BufferedReader inputStream = new BufferedReader(new FileReader(file_name));
    String s = inputStream.readLine();
    while (s != null)
    {
      // code to do
      String[] data = s.split(",");
      String name = data[0];
      char symbol = data[1].charAt(0);
      double confidence = Double.parseDouble(data[2]);
      int lane = Integer.parseInt(data[3]);
      horses.add(new Horse(symbol, name, confidence, lane));
      s = inputStream.readLine();
    }
    inputStream.close();
    return horses;
  }
}