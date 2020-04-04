package server;

import java.util.ArrayList;
import java.util.HashMap;

import shared.*;

/***
 * A Game class that used to store game states on the server side
 */
public class Game {

    private int gid; // game id
    private int playerNum; // number of players
    private int stage; // game stage
    private Map map; // game map
    private ArrayList<Player> playerList; // list of players
    private HashMap<Integer, Action> tempActionList; // store temperary actions in each turn

    // initialize a game
    public Game(int g_id, int player_num, Map m, Player first_player) {
        gid = g_id;
        playerNum = player_num;
        stage = GameMessage.WAIT_FOR_PLAYERS;  // game start at stage 0
        map = m;
        playerList = new ArrayList<Player>();
        playerList.add(first_player); // put the first player into playerlist
        tempActionList = new HashMap<Integer, Action>(); // empty action list
    }

    // default constuctor: game with gid=0, stage=ERROR
    public Game() {
        gid = 0;
        playerNum = 0;
        stage = GameMessage.ERROR;
        map = new Map();
        playerList = new ArrayList<Player>();
        tempActionList = new HashMap<Integer, Action>(); // empty action list
    }

    public HashMap<Integer, Action> getTempActionList(){
        return tempActionList;
    }

    public int getGid() {
        return gid;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public Map getMap() {
        return map;
    }

    public synchronized void setMap(Map m) {
        map = m;
    }

    public int getStage() {
        return stage;
    }

    public synchronized void setStage(int s) {
        stage = s;
    }

    public synchronized void addPlayer(Player p) {
        playerList.add(p);
    }

    public boolean isFull() {
        return playerNum == playerList.size();
    }

    public synchronized void addTempAction(int pid, Action ac) {
        if (tempActionList.containsKey(pid)) {
            System.out.println("Error: pid " + pid + " already wrote to tempActionList in game " + gid);
            return;
        }
        tempActionList.put(pid, ac);
    }

    public synchronized void clearTempActions() {
        tempActionList.clear();
    }

    public int getPidByName(String name) {
        PlayerStat p = map.getPlayerStatByName(name);
        return p.getPid();
    }

    public boolean hasPlayer(String name) {
        for (Player p : playerList) {
            if (p.getUsername().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // set PlayerStats according to playerList
    public void setPlayerStats() {
        ArrayList<PlayerStat> playerStats = new ArrayList<PlayerStat>();
        for (int i = 0; i < playerNum; i++){
            Player p = playerList.get(i);
            PlayerStat pStat = new PlayerStat(i, p.getUsername(), Map.INIT_FOOD, Map.INIT_GOLD, Map.INIT_T_NUM, Map.COLOR_LIST[i]);
            playerStats.add(pStat);
        }
        synchronized (this){
            map.setPlayerStats(playerStats);
        }
        
    }

    // return true if all active player has sent action to server,
    // which means one turn just finished
    public boolean turnFinished(){
        for (Player p: playerList){
            if (p.isConnected() && p.isLoggedin() && p.getActiveGid()==gid){
                int pid = getPidByName(p.getUsername());
                if (!tempActionList.containsKey(pid)){
                    return false;
                }
            }
        }
        return true;
    }

}