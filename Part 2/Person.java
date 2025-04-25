public class Person
{
  private String name;
  private double balance;
  private String bet;
  private double bettingAmount;

  public void showBalance()
  {
    System.out.println();
    System.out.println("Balance: " + Validation.roundToNDecimalPlaces(this.balance, 2));
  }

  public void setBettingAmount(double d)
  {
    bettingAmount = d;
  }

  public double getBettingAmount()
  {
    return this.bettingAmount;
  }

  public void setBet(String s)
  {
    this.bet = s;
  }

  public String getBet()
  {
    return this.bet;
  }

  public Person(String name, double initialBalance)
  {
    this.name = name;
    this.balance = initialBalance;
    this.bet = "";
  }

  public void increaseBalance(double amount)
  {
    this.balance += amount; 
  }

  public void decreaseBalance(double amount)
  {
    if (amount > this.balance)
    {
      System.out.println("Negative Balance");
    }
    this.balance -= amount; 
  }

  public void setName(String newName)
  {
    this.name = newName;
  }

  public void setBalance(double newBalance)
  {
    if (newBalance < 0)
    {
      System.out.println("Balance is now negative");
      return;
    }
    this.balance = newBalance;
  }

  public String getName()
  {
    return this.name;
  }

  public double getBalance()
  {
    return this.balance;
  }
}