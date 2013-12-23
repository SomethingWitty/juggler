import java.util.*;

public class Juggler
{
  private String name;
  private int coordination;
  private int endurance;
  private int pizzazz;
  private boolean moveable;
  private int index; //which is the current circuit that you prefer the most that you can get assigned to
  
  private String[] preferences;
  
  public Juggler(String name, int coordination, int endurance, int pizzazz, String[] preferences, int numCircuits)
  {
    this.name = name;
    this.coordination = coordination;
    this.endurance = endurance;
    this.pizzazz = pizzazz;
    this.preferences = preferences;
    this.index = 0;
    this.moveable = true;
  }
  public int getDotProduct(Circuit circuit)
  {
    return coordination * circuit.getCoordination() + endurance * circuit.getEndurance() + pizzazz * circuit.getPizzazz();
  }
  public void setImmovable()
  {
    moveable = false;
  }
  public boolean isMoveable()
  {
    return moveable;
  }
  public String getCircuitDotProductString(Map<String, Circuit> map)
  {
    String dotProducts = "";
    for(int i = 0; i<preferences.length; i++)
    {
      Circuit circuit = map.get(preferences[i]);
      if(circuit==null)
        break;
      dotProducts = dotProducts + " " + preferences[i] + ":" + getDotProduct(circuit);
    }
    return dotProducts;
  }
  public String getName()
  {
    return this.name;
  }
  public String getNameOfMostPreferredAvailableCircuits()
  {
    return preferences[index];
  }
  public void incrementPotentialCircuitIndex()
  {
    index++;
  }
  public String toString()
  {
    return "Name: " + name + " Coordination: " + coordination + " Endurance: " + endurance + " Pizzazz: " + pizzazz; 
  }
  public String getLeastPreferredCircuit()
  {
    String leastPreferred = "";
    for(int i = 0; i<preferences.length; i++)
    {
      if(preferences[i]==null)
      {
        leastPreferred = preferences[i-1];
        break;
      }
    }
    return leastPreferred;
  }
}