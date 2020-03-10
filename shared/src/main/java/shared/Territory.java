package shared;

import java.io.Serializable;
import java.util.ArrayList;

/*****
Map

0  8
 4  12
1  9
 5  13
2 10
 6  14
3 11
 7  15

Territory shape: hexagon

 *****/

public class Territory implements Serializable{

  public static final int MAX_NEIGHBOR = 6;
  public static final int MAP_SIZE = 16;
  public static final int ROW_NUM = 4;

  
  private int ownership;//set to player id 1-5 who owns the territory
  private int tid; //territory id, 1-16
  private String name; //territory name
  //private int x,y; //location coordinate
  private Army defender; //currently only have num_units field
  private ArrayList<Territory> neighborList;//can change to List
  //neighbor list index 0-5 in counterclock direction
  //another way is only store tid or name of territories, but i think it is not enough

  // Initialize a territory with 0 defender
  public Territory(int pid, int t_id, String t_name) {
    //constructor that creates empty adj list
    ownership = pid;
    tid = t_id;
    name = t_name;
    defender = new Army(0);
    neighborList = new ArrayList<Territory>();
    // initialize neighbor list with all null
    for (int i = 0; i < MAX_NEIGHBOR; i++) {
      neighborList.add(null);
    }
  }

  /******
  public Territory(int pid, int t_id, String t_name, Army t_defender, ArrayList<Territory> adjList) {
    //constructor that initialize adj list
    ownership = pid;
    tid = t_id;
    name = t_name;
    defender = t_defender;
    neighborList = adjList;
  }
  ******/
  
  /******
  public Territory(Territory rhs) {//copy constructor
    ownership = rhs.ownership;
    tid = rhs.tid;
    name = rhs.name;
    defender = rhs.defender;
    neighborList = rhs.neighborList;
    //may need to throw exception here if rhs doesn't have some fields
  }
  *****/
  
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

  public void setNeighbor(int index, Territory neighbor) {
    //set the specified territory at the specified position in this list.
    neighborList.set(index, neighbor);
  }

  // return the tid of neighbor given the neighbor index(0-5)
  public int getNbID(int index) {
    int nbID;
    switch (index) {
    case 0:  // up
      if (tid % ROW_NUM == 0) {
        nbID = -1;
      } else {
        nbID = tid - 1;
      }
      break;
    case 3:  // down
      if ((tid + 1) % ROW_NUM == 0) {
        nbID = -1;
      } else {
        nbID = tid + 1;
      }
      break;
    case 1:
      if (tid >= MAP_SIZE - ROW_NUM || tid%(2*ROW_NUM)==0) {
        nbID = -1;
      } else if (tid % (2*ROW_NUM) < ROW_NUM) {
        nbID = tid + ROW_NUM - 1;
      } else {
        nbID = tid + ROW_NUM;
      }
      break;
    case 2:
      if (tid >= MAP_SIZE-ROW_NUM || (tid+1)%(2*ROW_NUM)==0) {
        nbID = -1;
      } else if (tid % (2*ROW_NUM) < ROW_NUM) {
        nbID = tid + ROW_NUM;
      } else {
        nbID = tid + ROW_NUM + 1;
      }
      break;
    case 5:
      if (tid < ROW_NUM || tid%(2*ROW_NUM)==0) {
        nbID = -1;
      } else if (tid % (2*ROW_NUM) >= ROW_NUM) {
        nbID = tid - ROW_NUM;
      } else {
        nbID = tid - (ROW_NUM + 1);
      }
      break;
    case 4:
      if (tid < ROW_NUM || (tid+1)%(2*ROW_NUM)==0) {
        nbID = -1;
      } else if (tid % (2*ROW_NUM) >= ROW_NUM) {
        nbID = tid - (ROW_NUM-1);
      } else {
        nbID = tid - ROW_NUM;
      }
      break;
    default:
      nbID = -1;
    }

    return nbID;
  }

  // return the neighbor given the neighbor index
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
