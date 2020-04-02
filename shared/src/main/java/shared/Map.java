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

    private ArrayList<Territory> territories;
    private ArrayList<PlayerStat> playerStats;

    public static final int INIT_T_NUM = 3; // DONOT change, initial territory number per player

    public static final int INIT_FOOD = 10; // initial food resource per player
    public static final int INIT_GOLD = 10; // initial gold/technology resource per player
    public static final int INIT_UNIT = 15; // initial total units (in territory group) per player
    public static final int INIT_T_SIZE = 5; // initial total size (in territory group) per player
    public static final int INIT_FOOD_PROD = 10; // initial total food production (in territory group) per player
    public static final int INIT_GOLD_PROD = 10; // initial total gold production (in territory group) per player
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