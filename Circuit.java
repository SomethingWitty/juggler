import java.util.*;

public class Circuit
{
  private String name;
  private int coordination;
  private int endurance;
  private int pizzazz;
  public Circuit(String name, int coordination, int endurance, int pizzazz)
  {
    this.name = name;
    this.coordination = coordination;
    this.endurance = endurance;
    this.pizzazz = pizzazz;
  }
  public String getName()
  {
    return this.name;
  }
  public int getCoordination()
  {
    return coordination;
  }
  public int getEndurance()
  {
    return endurance;
  }
  public int getPizzazz()
  {
    return pizzazz;
  }
  public String toString()
  {
    return "name: " + name + " coordination: " + coordination + " endurance: " + endurance + " pizzazz: " + pizzazz;
  }
  
}