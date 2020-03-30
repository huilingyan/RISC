package shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/***
 * A class that represents a game map, which includes a list of territories and
 * a list of player stats
 */
public class Map implements Serializable {

    private ArrayList<Territory> territories;
    private ArrayList<PlayerStat> playerStats;

    public static final int INIT_UNIT = 50; // initial unit per player
    public static final int INIT_FOOD = 50; // initial food resource per player
    public static final int INIT_GOLD = 50; // initial gold/technology resource per player
    public static final String[] COLOR_LIST = {"Blue", "Red", "Green", "Yellow", "Purple"};  // hardcoded color list


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
        for (PlayerStat p : playerStats) {
            if (p.getPName().equals(p_name)) {
                return p;
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

}