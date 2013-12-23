import java.io.*;
import java.util.*;

public class Master
{
  private String fileName;
  private String outputFile;
  private List<Juggler> jugglers;
  private int numCircuits;
  private int numJugglers;
  private int jugglersPerCircuit;
  
  private Map<String, Circuit> circuitNamesMap;
  
  private Map<Circuit, List<Juggler>> circuitToJugglerMap;
  
  public Master(String fileName, String outputFile) throws Exception
  {
    this.fileName = fileName;
    this.outputFile = outputFile;
    jugglers = new LinkedList<Juggler>();
    circuitToJugglerMap = new HashMap<Circuit, List<Juggler>>();
    circuitNamesMap = new HashMap<String, Circuit>();
    numCircuits = 0;
    numJugglers = 0;
    initialize();
  }
  private void initialize() throws Exception
  {
    FileReader fr = new FileReader(fileName); 
    BufferedReader br = new BufferedReader(fr); 
    String line; 
    while((line=br.readLine())!=null) 
    {
      if(line.startsWith("C"))
      {
        String name = "";
        int coordination = 0;
        int pizzazz = 0;
        int endurance = 0;
        StringTokenizer tok = new StringTokenizer(line);
        
        //first token is just the first letter that tells us if it's a circuit or juggler
        tok.nextToken();
        
        while(tok.hasMoreTokens())
        {
          String token = tok.nextToken();
          if(token.startsWith("C"))
          {
            name = token;
          }
          if(token.startsWith("H"))
          {
            coordination = (new Integer(token.substring(2))).intValue();
          }
          if(token.startsWith("E"))
          {
            endurance = (new Integer(token.substring(2))).intValue();
          }
          if(token.startsWith("P"))
          {
            pizzazz = (new Integer(token.substring(2))).intValue();
          }
        }
        Circuit circuit = new Circuit(name, coordination, endurance, pizzazz);
        
        circuitNamesMap.put(name, circuit);
        circuitToJugglerMap.put(circuit, new LinkedList<Juggler>());
        numCircuits++;
      }
      if(line.startsWith("J"))
      {
        String name = "";
        int coordination = 0;
        int endurance = 0;
        int pizzazz = 0;
        String[] preferences = new String[numCircuits];
        int prefCount = 0;
        StringTokenizer tok = new StringTokenizer(line, ", ");
        
        tok.nextToken();
        
        while(tok.hasMoreTokens())
        {
          String token = tok.nextToken();
          if(token.startsWith("J"))
          {
            name = token;
          }
          if(token.startsWith("H"))
          {
            coordination = (new Integer(token.substring(2))).intValue();
          }
          if(token.startsWith("E"))
          {
            endurance = (new Integer(token.substring(2))).intValue();
          }
          if(token.startsWith("P"))
          {
            pizzazz = (new Integer(token.substring(2))).intValue();
          }
          if(token.startsWith("C"))
          {
            preferences[prefCount] = token;
            prefCount++;
          }
        }
        Juggler juggler = new Juggler(name, coordination, endurance, pizzazz, preferences, numCircuits);
        jugglers.add(juggler);
        numJugglers++;
      }
    }
    jugglersPerCircuit = numJugglers/numCircuits;
  }
  private void assignJugglersToCircuits()
  {
    List<Juggler> removeList = new LinkedList<Juggler>();
    for(Juggler juggler: jugglers)
    {
      String preferredDestination = juggler.getNameOfMostPreferredAvailableCircuits();
      Circuit preferredCircuit = circuitNamesMap.get(preferredDestination);
      List<Juggler> list = circuitToJugglerMap.get(preferredCircuit);
      //we must assign the juggler to their least preferred list
      if(list == null )
      {
        preferredDestination = juggler.getLeastPreferredCircuit();
        preferredCircuit = circuitNamesMap.get(preferredDestination);
        list = circuitToJugglerMap.get(preferredCircuit);
        //assign them to their last preference and remove them from jugglers
        list = addJugglerToList(juggler, list, preferredCircuit, true);
        removeList.add(juggler);
        juggler.setImmovable();
      }
      if(!list.contains(juggler))
      {
        list = addJugglerToList(juggler, list, preferredCircuit, false);
        circuitToJugglerMap.put(preferredCircuit, list);
      }
    }
    jugglers.removeAll(removeList);
  }
  //add jugglers to the list so that the ones at the beginning of the list have a lower dot product than the later ones
  private List<Juggler> addJugglerToList(Juggler juggler, List<Juggler> list, Circuit circuit, boolean lastPreference)
  {
    boolean added = false;
    if(lastPreference)
    {
      //find first moveable on the list
      for(int i = 0; i<list.size(); i++)
      {
        if(list.get(i).isMoveable())
        {
          list.add(i, juggler);
          return list;
        }
      }
    }
    //otherwise place the juggler relative to the other dot products of the other jugglers in the list
    if(list.size()>0)
    {
      for(int i = 0; i<list.size(); i++)
      {
        if(juggler.getDotProduct(circuit)<list.get(i).getDotProduct(circuit))
        {
          list.add(i, juggler);
          added = true;
          break;
        }
      }
      if(!added)
      {
        list.add(juggler);
      }
    }
    else
    {
      list.add(juggler);
    }
    return list;
  }
  //jugglers that inhabit lists that are greater than the alloted size of jugglers in a circuit
  private List<Juggler> createListOfBadJugglers()
  {
    List<Juggler> badJugglers = new LinkedList<Juggler>();
    //create initial list of bad jugglers
    for(Circuit circuit: circuitToJugglerMap.keySet())
    {
      while(circuitToJugglerMap.get(circuit).size()>jugglersPerCircuit)
      {
        Juggler juggler = circuitToJugglerMap.get(circuit).remove(0);
        List<Juggler> jugglers = circuitToJugglerMap.get(circuit);
        for(int i = 0; i<jugglers.size(); i++)
        {
          if(jugglers.get(i).isMoveable())
          {
            jugglers.remove(i);
            break;
          }
        }
        juggler.incrementPotentialCircuitIndex();
        badJugglers.add(juggler);
      }
    }
    return badJugglers;
  }
  public void run() throws Exception
  {
    assignJugglersToCircuits();
    List<Juggler> list = createListOfBadJugglers();
    while( list.size() > 0)
    {    
      assignJugglersToCircuits();
      list = createListOfBadJugglers();
    }
    printOutput();
  }
  private void printOutput() throws Exception
  {
    PrintWriter out = new PrintWriter(new FileWriter(outputFile)); 
    
    for(Circuit circuit:circuitToJugglerMap.keySet())
    {
      String output = "";
      List<Juggler> list = circuitToJugglerMap.get(circuit);

      output = circuit.getName() + " ";
      for(Juggler juggler: list)
      {
        output = output + juggler.getName() + juggler.getCircuitDotProductString(circuitNamesMap) + ", ";
      }
      out.println(output.substring(0, output.length()-2));
    }
    out.close();
  }
  
  public static void main(String [] args) throws Exception
  {
    Master master = new Master("jugglefest.txt", "output.txt");
    master.run();
  }
  
}