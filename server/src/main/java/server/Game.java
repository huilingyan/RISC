package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import shared.*;


public class Game {

    private int gid;
    private int playerNum;
    private int stage;
    private Map map;
    private HashMap<Integer, Player> playerList;

    public static final int WAIT_FOR_PLAYERS = 0;
    public static final int INITIALIZE_UNITS = 1;
    public static final int GAME_PLAY = 2;
    public static final int GAME_OVER = 3;

    // initialize a game
    public Game(int g_id, int player_num, Map m, Player first_player){
        gid = g_id;
        playerNum = player_num;
        stage = WAIT_FOR_PLAYERS;
        map = m;
        playerList = new HashMap<Integer, Player>();
        playerList.put(0, first_player);  // put the first player into playerlist

    }
}