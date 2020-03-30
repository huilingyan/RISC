package server;

import java.util.ArrayList;
import java.util.HashMap;

import shared.*;

/***
 * A Game class that used to store game states on the server side
 */
public class Game {

    private int gid;  // game id
    private int playerNum;  // number of players
    private int stage;  // game stage
    private Map map;  // game map
    private ArrayList<Player> playerList;  // list of players
    private HashMap<Integer, Action> tempActionList;  // store temperary actions in each turn

    // initialize a game
    public Game(int g_id, int player_num, Map m, Player first_player){
        gid = g_id;
        playerNum = player_num;
        stage = GameMessage.WAIT_FOR_PLAYERS;
        map = m;
        playerList = new ArrayList<Player>();
        playerList.add(first_player);  // put the first player into playerlist
        tempActionList = new HashMap<Integer, Action>();  // empty action list
    }

    public int getGid(){
        return gid;
    }

    public int getPlayerNum(){
        return playerNum;
    }

    public Map getMap(){
        return map;
    }

    public void setMap(Map m){
        map = m;
    }

    public int getStage(){
        return stage;
    }

    public void setStage(int s){
        stage = s;
    }

    public synchronized void addPlayer(Player p){
        playerList.add(p);
    }

    public boolean isFull(){
        return playerNum == playerList.size();
    }

    public void addTempAction(int pid, Action ac){
        tempActionList.put(pid, ac);
    }

    public void clearTempActions(){
        tempActionList.clear();
    }

    public int getPidByName(int pid){
        PlayerStat p = map.getPlayerStatByPid(pid);
        return p.getPid();
    }


}