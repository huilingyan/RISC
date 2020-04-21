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

  
  private int ownership;//set to player id 0-4 who owns the territory
  private int tid; //territory id, 0-15
  private String name; //territory name
  //private int x,y; //location coordinate
  private Army defender;
  private Army friendDefender;//ally's army
  private ArrayList<Integer> neighborList;//can change to List
  //neighbor list index 0-5 in counterclock direction
  //stored tid of the neighbor
  private int size;//the larger the size, the more food will cost to go through this territory
  private int food_resource;
  private int gold_resource;//resources to upgrade the technology

  // Initialize a territory with 0 defender
  public Territory(int pid, int t_id, String t_name) {
    //constructor that creates empty adj list
    ownership = pid;
    tid = t_id;
    name = t_name;
    defender = new Army(0);
    friendDefender = new Army(0);
    neighborList = new ArrayList<Integer>();
    // initialize neighbor list with all null
    for (int i = 0; i < MAX_NEIGHBOR; i++) {
      neighborList.add(-1);   // -1 means no neighbor
    }
  }

  // Initialize a territory with 0 defender
  public Territory(int pid, int t_id, String t_name,int t_size, int t_food, int t_gold) {
    //constructor that creates empty adj list
    ownership = pid;
    tid = t_id;
    name = t_name;
    defender = new Army(0);
    friendDefender = new Army(0);
    neighborList = new ArrayList<Integer>();
    // initialize neighbor list with all null
    for (int i = 0; i < MAX_NEIGHBOR; i++) {
      neighborList.add(-1);   // -1 means no neighbor
    }
    this.size = t_size;
    this.food_resource = t_food;
    this.gold_resource = t_gold;
  }

  public void set_terr_attributes(int t_size, int t_food, int t_gold) {
    this.size = t_size;
    this.food_resource = t_food;
    this.gold_resource = t_gold;
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
  
  
  public Territory(Territory rhs) {//copy constructor
    ownership = rhs.ownership;
    tid = rhs.tid;
    name = rhs.name;
    this.size = rhs.size;
    this.food_resource = rhs.food_resource;
    this.gold_resource = rhs.gold_resource;
    defender = new Army(rhs.defender);
    friendDefender = new Army(rhs.friendDefender);
    neighborList = new ArrayList<Integer>(rhs.getNeighborList());
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

  public int getSize() {
    return size;
  }

  public int getFood() {
    return food_resource;
  }

  public int getGold() {
    return gold_resource;
  }

  public void setDefender(Army rhs) {
    defender = new Army(rhs);//use copy constructor, more secure
  }

  public Army getDefender() {//separate method from Army class
    return defender;
  }

  public void setFriendDefender(Army rhs) {
    friendDefender = new Army(rhs);//use copy constructor, more secure
  }

  public Army getFriendDefender() {
    return friendDefender;
  }

  public void setNeighborList(ArrayList<Integer> adjList){
    neighborList = adjList;
  }

  public ArrayList<Integer> getNeighborList() {
    return neighborList;
  }

  public void setNeighbor(int index, int nbid) {
    //set the specified territory at the specified position in this list.
    neighborList.set(index, nbid);
  }

  // calculate the tid of neighbor given the neighbor index(0-5), under condition of full map (16 territories)
  // -1 means no neighbor
  public int calcNbID(int index) {
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
  public int getNeighbor(int index) {
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

  public void addDefender(Army rhs) {
    defender.joinArmy(rhs);
  }

  public void subtractDefender(Army rhs) {
    defender.subtractArmy(rhs);
  }

  public void addFriendDefender(Army rhs) {
    friendDefender.joinArmy(rhs);
  }

  public void subtractFriendDefender(Army rhs){
    friendDefender.subtractArmy(rhs);
  }

  public int countNeighbors() {
    int cnt = 0;
    for (int nbid : neighborList) {
      if (nbid != -1) {
        cnt++;
      }
    }
    return cnt;
  }

}
