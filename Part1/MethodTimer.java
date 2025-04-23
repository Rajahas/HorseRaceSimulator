public class MethodTimer
{
  private long startTime;
  private long endTime;

  public void start()
  {
    this.startTime = System.currentTimeMillis();
  }

  public void stop()
  {
    this.endTime = System.currentTimeMillis();
  }

  public long Duration()
  {
    if ((endTime == 0) || (startTime == 0))
    {
      System.out.println("Timer not done properly");
      return 0;
    }
    else
    {
      return endTime - startTime;
    }
  }

  public void printDuration()
  {
    long duration = Duration();
    System.out.println("Method took " + duration + " milliseconds");
  }

  public void reset()
  {
    this.startTime = 0;
    this.endTime = 0;
  }
}