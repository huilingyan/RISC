package shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;


/***
 * A class that represents a game map, which includes a list of territories and
 * a list of player stats
 */
public class Map implements Serializable {

    /**
   * TODO: UID
   */
    private static final long serialVersionUID = -346790680799127139L;

    private ArrayList<Territory> territories;
    private ArrayList<PlayerStat> playerStats;

    public static final int INIT_T_NUM = 3; // DONOT change, initial territory number per player

    public static final int INIT_FOOD = 10; // initial food resource per player
    public static final int INIT_GOLD = 10; // initial gold/technology resource per player
    public static final int INIT_UNIT = 15; // initial total units (in territory group) per player
    public static final int INIT_T_SIZE = 5; // initial total size (in territory group) per player
    public static final int INIT_FOOD_PROD = 10; // initial total food production (in territory group) per player
    public static final int INIT_GOLD_PROD = 20; // initial total gold production (in territory group) per player
    public static final String[] TERRITORY_NAME_LIST = {"Pikachu", "Ditto", "Gengar", "Eevee", "Snorlax", "Mew", "Psyduck", "Magneton", "Vulpix", "Jumpluff", "Bulbasaur", "Charmandar", "Squirtle", "Pidgey", "Caterpie", "Rattata"};
    // blue, red, green, yellow, purple
    public static final String[] COLOR_LIST = {"87CEFA", "F08080", "90EE90", "FFE4B5", "DDA0DD"};  // hardcoded color list


    // default constructor
    public Map() {
        territories = new ArrayList<Territory>();
        playerStats = new ArrayList<PlayerStat>();
    }

    // constructor that takes a list of territory
    public Map(ArrayList<Territory> territory_list) {
        territories = territory_list;
        playerStats = new ArrayList<PlayerStat>();
    }

    // constructor that takes a list of territory and a list of playerstat
    public Map(ArrayList<Territory> territory_list, ArrayList<PlayerStat> playerstat_list) {
        territories = territory_list;
        playerStats = playerstat_list;
    }

    // copy constructor
    public Map(Map rhs) {
        // copy territories
        territories = new ArrayList<Territory>();
        for (Territory t : rhs.getTerritories()) {
            territories.add(new Territory(t));
        }
        // copy playerstats
        playerStats = new ArrayList<PlayerStat>();
        for (PlayerStat p : rhs.getPlayerStats()) {
            playerStats.add(new PlayerStat(p));
        }
    }

    public void addPlayerStat(PlayerStat p) {
        playerStats.add(p);
    }

    public void addTerritory(Territory t) {
        territories.add(t);
    }

    public void setTerritories(ArrayList<Territory> t_list) {
        territories = t_list;
    }

    public void setPlayerStats(ArrayList<PlayerStat> p_list) {
        playerStats = p_list;
    }

    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    public ArrayList<PlayerStat> getPlayerStats() {
        return playerStats;
    }

    public int getTerritoryNum() {
        return this.territories.size();
    }

    public Territory getTerritoryByName(String t_name) {
        for (Territory t : territories) {
            if (t.getName().equalsIgnoreCase(t_name)) {
                return t;
            }
        }
        return null;
    }

    public Territory getTerritoryByTid(int t_id) {
        for (Territory t : territories) {
            if (t.getTid() == t_id) {
                return t;
            }
        }
        return null;
    }

    public PlayerStat getPlayerStatByName(String p_name) {
        for (PlayerStat p : this.playerStats) {
            if (p.getPName().equals(p_name)) {
                return p;
            }
        }
        return null;
    }


    public int getPidByName(String p_name) {
      for (PlayerStat p : this.playerStats) {
        if (p.getPName().equals(p_name)) {
            return p.getPid();
        }
      }
      return -1;
    }

    public String getTerritoryNameByTid(int tid) {
      for (Territory t : this.territories) {
        if (t.getTid() == tid) {
            return t.getName();
        }
      }
      return null;
    }


    public PlayerStat getPlayerStatByPid(int p_id) {
        for (PlayerStat p : playerStats) {
            if (p.getPid() == p_id) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<String> getOwnTerritoryListName(int pid) {
      ArrayList<String> list = new ArrayList<>();
      for (Territory t : territories) {
            if (t.getOwnership() == pid) {
              list.add(t.getName());
            }
        }
        return list;
    }

  public ArrayList<String> getEnermyTerritoryListName(int pid) {
      ArrayList<String> list = new ArrayList<>();
      for (Territory t : territories) {
            if (t.getOwnership() != pid) {
              list.add(t.getName());
            }
        }
        return list;
    }

    public int CostofShortestPath(String src, String dest) {
      //Dijkstra's algorithm
      int masterPid = getTerritoryByName(src).getOwnership();
      HashMap<String, Integer> distQ = new HashMap<>();
      HashMap<String, Integer> visited = new HashMap<>();
      distQ.put(src, 0);
      while (distQ.size() > 0) {
        String minTName = popMinDistT(distQ);
        int d = distQ.remove(minTName);//remove the minimum distance T from the queue
        if (minTName.equalsIgnoreCase(dest)){
          System.out.println("CostofShortestPath: " + d);
          return d;
        }
        visited.put(minTName, d);

        Territory t = getTerritoryByName(minTName);
        //stem.out.println(t.getNeighborList());
        for (int tid : t.getNeighborList()) {
          if (tid != -1) {
            Territory neighbour = getTerritoryByTid(tid);
            if (neighbour.getOwnership() == masterPid) {
              String neigName = neighbour.getName();
              if (visited.containsKey(neigName) == false) {
                //this neighbout is not finally determined its minimum distance
                int potential_dist = d + neighbour.getSize();
                relax(distQ, potential_dist, neigName);
              }
            }
          }
        }
      }
      return -1;//no path found
    }

    private void relax(HashMap<String, Integer> distQ, int potential_dist, String neigName) {
      if (distQ.containsKey(neigName) == true) {
        if (distQ.get(neigName) > potential_dist) {
          distQ.replace(neigName, potential_dist);
        }
      }
      else {
        distQ.put(neigName, potential_dist);
      }
    }

    public String popMinDistT(HashMap<String, Integer> que) {
      int min_dist = 99999;
      String s = "";
      for (HashMap.Entry<String, Integer> e : que.entrySet()) {
        if (e.getValue() < min_dist) {
          min_dist = e.getValue();
          s = e.getKey();
        }
      }

      return s;
    }

    public void updateUnitandResource(){
        updateUnit();
        updateResource();
    }

    // add 1 base unit to each territory
    private void updateUnit(){
        for (Territory t : territories) {
            t.addDefender(new Army(1));
          }
    }

    // add each territory's resource production to its owner
    private void updateResource(){
        for (Territory t: territories){
            PlayerStat ps = getPlayerStatByPid(t.getOwnership());
            // food
            ps.addFood(t.getFood());
            // gold
            ps.addGold(t.getGold());
        }
    }

    

}
