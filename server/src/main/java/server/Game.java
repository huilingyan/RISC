package server;

import java.util.ArrayList;
import java.util.HashMap;

import javax.persistence.*;
import shared.*;
import lombok.*;

/***
 * A Game class that used to store game states on the server side
 */
@Entity
@Getter
@Setter
public class Game {

    @Id
    private int gid; // game id

    private int playerNum; // number of players
    private int stage; // game stage
    private Map map; // game map

    @OneToMany(mappedBy="game")
    @OrderColumn(name="user_index")
    private ArrayList<UserInfo> playerList; // list of players

    @OneToMany
    @MapKeyColumn(name="pid")
    private HashMap<Integer, Action> tempActionList; // store temperary actions in each turn

    private boolean full; // true if game is full, else false

    /****
     * Initialize a game with gid, player number, map and first player. Stage is
     * default to WAIT_FOR_PLAYERS, full is default to false
     * 
     * @param g_id
     * @param player_num
     * @param m
     * @param first_player
     */
    public Game(int g_id, int player_num, Map m, UserInfo first_player) {
        gid = g_id;
        playerNum = player_num;
        stage = GameMessage.WAIT_FOR_PLAYERS; // game start at stage 0
        map = m;
        playerList = new ArrayList<UserInfo>();
        playerList.add(first_player); // put the first player into playerlist
        System.out.println("Add the first player to game " + g_id);
        System.out.println("Player num: " + playerNum);
        tempActionList = new HashMap<Integer, Action>(); // empty action list
        full = false;
    }

    /**
     * Default constuctor: game with gid=0, stage=ERROR
     */
    public Game() {
        gid = 0;
        playerNum = 0;
        stage = GameMessage.ERROR;
        map = new Map();
        playerList = new ArrayList<UserInfo>();
        tempActionList = new HashMap<Integer, Action>(); // empty action list
        full = false;
    }

    public HashMap<Integer, Action> getTempActionList() {
        return tempActionList;
    }

    /**
     * Add a player p to playerlist. If after adding the game reaches full, set full
     * to true
     * 
     * @param p
     */
    public synchronized void addPlayer(UserInfo u) {
        playerList.add(u);
        System.out.println("Add a player " + u.getUsername());
        System.out.println("Player number in player list: " + playerList.size());
        if (playerList.size() == playerNum) {
            System.out.println("Set full to true");
            full = true;
        }
    }

    /**
     * After receives action from client, clientworker add a temp action to
     * tempActionList
     * 
     * @param pid
     * @param ac
     */
    public synchronized void addTempAction(int pid, Action ac) {
        if (tempActionList.containsKey(pid)) {
            System.out.println("Error: pid " + pid + " already wrote to tempActionList in game " + gid);
            return;
        }
        tempActionList.put(pid, ac);
    }

    /**
     * After the gameworker updates game state for one turn, it clears
     * tempActionList
     */
    public synchronized void clearTempActions() {
        tempActionList.clear();
    }

    /**
     * Return player's pid, given its username
     * 
     * @param name
     * @return
     */
    public int getPidByName(String name) {
        PlayerStat p = map.getPlayerStatByName(name);
        return p.getPid();
    }

    /**
     * Check if the game has the player, given the player's username
     * 
     * @param name
     * @return
     */
    public boolean hasPlayer(String name) {
        for (UserInfo u : playerList) {
            if (u.getUsername().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Set PlayerStats according to playerList
     */
    public void setPlayerStats() {
        ArrayList<PlayerStat> playerStats = new ArrayList<PlayerStat>();
        for (int i = 0; i < playerNum; i++) {
            UserInfo u = playerList.get(i);
            PlayerStat pStat = new PlayerStat(i, u.getUsername(), Map.INIT_FOOD, Map.INIT_GOLD, Map.INIT_T_NUM,
                    Map.COLOR_LIST[i]);
            playerStats.add(pStat);
        }
        synchronized (this) {
            map.setPlayerStats(playerStats);
        }

    }

    /***
     * @return true if all active player has sent action to server, which means one
     *         turn just finished
     */
    public boolean turnFinished() {
        for (UserInfo u : playerList) {
            if (u.isConnected() && u.isLoggedin() && u.getActiveGid() == gid) {
                int pid = getPidByName(u.getUsername());
                if (!tempActionList.containsKey(pid)) {
                    return false;
                }
            }
        }
        return true;
    }

    /***
     * Check if the game has no active player
     * 
     * @return
     */
    public boolean noActivePlayer() {
        for (UserInfo u : playerList) {
            // is active player
            if (u.isConnected() && u.isLoggedin() && u.getActiveGid() == gid) {
                return false;
            }
        }
        return true;
    }

}