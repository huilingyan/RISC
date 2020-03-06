package shared;

import java.util.ArrayList;


public class Territory {
  private int ownership;//set to player id 1-5 who owns the territory
  private int tid; //territory id, 1-16
  private String name; //territory name
  //private int x,y; //location coordinate
  private Army defender; //currently only have num_units field
  private ArrayList<Territory> neighborList;//can change to List
  //neighbor list index 0-5 in counterclock direction
  //another way is only store tid or name of territories, but i think it is not enough

  public Territory() {//empty constructor
    neighborList = new ArrayList<Territory>();
  };

  public Territory(int pid, int t_id, String t_name, Army t_defender) {
    //constructor that creates empty adj list
    ownership = pid;
    tid = t_id;
    name = t_name;
    defender = t_defender;
    neighborList = new ArrayList<Territory>();
  }

  public Territory(int pid, int t_id, String t_name, Army t_defender, ArrayList<Territory> adjList) {
    //constructor that initialize adj list
    ownership = pid;
    this.tid = t_id;
    name = t_name;
    defender = t_defender;
    neighborList = adjList;
  }

  public Territory(Territory rhs) {//copy constructor
    ownership = rhs.ownership;
    tid = rhs.tid;
    name = rhs.name;
    defender = rhs.defender;
    neighborList = rhs.neighborList;
    //may need to throw exception here if rhs doesn't have some fields
  }
  
  public void setOwnership(int pid) {
    ownership = pid;
  }

  public int getOwnership() {
    return ownership;
  }

  public void setTid(int t_id) {//may not be useful, can delete later
    tid = t_id;
  }

  public int getTid() {
    return tid;
  }

  public void setName(String t_name) {//may not be useful, can delete later
    name = t_name;
  }

  public String getName() {
    return name;
  }

  public void setDefenderNum(int num) {
    defender.setUnitNumber(num);
  }

  public int getDefenderNum() {//separate method from Army class
    return defender.getUnitNumber();
  }

  public void setNeighborList(ArrayList<Territory> adjList){
    neighborList = adjList;
  }

  public ArrayList<Territory> getNeighborList() {
    return neighborList;
  }

  public void addNeighbor(int index, Territory neighbor) {
    //add the specified territory at the specified position in this list.
    neighborList.add(index, neighbor);
  }

  public Territory getNeighbor(int index) {
  /*       0
         ----
      5 /    \ 1
      4 \    / 2
         ----
           3
     ugly but intuitive
   */
    return neighborList.get(index);
  }

  public void addDefender(int num) {
    defender.addUnits(num);
  }

  public void subtractDefender(int num){
    defender.subtractUnits(num);
  }
}
