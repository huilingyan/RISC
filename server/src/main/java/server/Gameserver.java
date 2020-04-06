package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import shared.*;

/****
 * A class that acts like a game server for RISC
 ***/
public class Gameserver {

  private ServerSocket mySocket; // server socket
  private ArrayList<Player> userList; // list of Player
  private ArrayList<Game> gameList; // list of games
  private int currentGid = 10; // gid start from 10
  private ArrayList<ClientWorker> clientThreads;

  public Gameserver() {
    userList = new ArrayList<Player>();
    gameList = new ArrayList<Game>();
    clientThreads = new ArrayList<ClientWorker>();  // ClientWorkers
  }

  public ArrayList<ClientWorker> getClientThreads(){
    return clientThreads;
  }

  /***
   * Run the server
   */
  public void run() {
    bindSocket(); // initialize server socket
    // moniter worker
    // MoniterWorker mWorker = new MoniterWorker(this);
    // mWorker.start();
    // accept connection and assign to a ClientWorker
    // add default users to list
    addAdminUsers();
    while (true) {
      Socket newSocket;
      while ((newSocket = acceptConnection()) == null) {
      } // loops until accept one connection
      ClientWorker worker = new ClientWorker(newSocket, this);
      synchronized(this){
        clientThreads.add(worker);  // add ClientWorker to the list
      }
      worker.start();
    }
  }

  private void addAdminUsers(){
    addUser(new Player("admin1", "1234"));
    addUser(new Player("admin2", "1234"));
    addUser(new Player("admin3", "1234"));
    addUser(new Player("admin4", "1234"));
  }

  /****
   * Accept a connection from client
   ****/
  private Socket acceptConnection() {
    try {
      Socket newSocket = mySocket.accept();
      return newSocket;
    } catch (IOException e) {
    }
    return null;
  }

  /****
   * Bind the server socket to the given port
   *****/
  private void bindSocket() {
    Config config = new Config("config.properties");
    String port = config.readProperty("port");
    if (port == null) {
      System.out.println("Cannot find port property in config file");
      System.exit(0);
    }
    try {
      ServerSocket newSocket = new ServerSocket(Integer.parseInt(port));
      mySocket = newSocket;
    } catch (IOException e) {
      System.out.println("Failed to bind socket to port " + port);
      System.exit(1); // exit program if fail to bind socket
    }
  }

  /**
   * Check if user with the given username existed in server memory
   * @param name
   * @return
   */
  public boolean hasUser(String name) {
    for (Player p : userList) {
      if (p.getUsername().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /***
   * Check if username exists, password matches, and the user is currently logged out
   * @param name
   * @param password
   * @return
   */
  public boolean isValidUser(String name, String password) {
    if (!hasUser(name)) {
      return false;
    }
    for (Player p : userList) {
      if (p.getUsername().equals(name) && p.getPassword().equals(password) && (!p.isLoggedin())) {
        return true;
      }
    }
    return false;
  }

  /***
   * Return the roomlist visible to the player, given the username
   * @param name
   * @return
   */
  public synchronized ArrayList<Room> gatherRooms(String name) {
    ArrayList<Room> rooms = new ArrayList<Room>();
    for (Game g : gameList) {
      // filled active game with the player in, or not filled game
      if (g.hasPlayer(name) && g.getStage() < GameMessage.GAME_OVER || !g.isFull()) {
        rooms.add(new Room(g.getGid(), g.getPlayerNum(), g.isFull()));
      }
    }
    return rooms;
  }

  public synchronized Player updateUser(String name, Player p) {
    for (Player old : userList) {
      if (old.getUsername().equals(name)) {
        System.out.println("update socket info");
        old.updateSocketandStreams(p); // connected is set to true
        return old;
      }
    }
    System.out.println("Error: user not found");
    return null; // not found, return null
  }

  /**
   * Add a copy of Player p to the list, and set connected and logged in to be false
   * @param p
   */
  public void addUser(Player p) {
    Player copy = new Player(p);
    copy.setConnected(false);
    copy.setLoggedin(false);
    synchronized (this) {
      userList.add(copy); // add a copy of Player p to the list
    }

  }

  private synchronized void addGame(Game g) {
    gameList.add(g);
    currentGid++;   // increment gid counter
  }

  public Game startNewGame(int playerNum, Player firstP) {
    // generate a new map with only territory list
    Map m = new Map(initializeTerritoryList(playerNum));
    Game g = new Game(currentGid, playerNum, m, firstP);
    addGame(g);
    return g;
  }

  /***
   * Generate a random ordered territory name list
   ****/
  private ArrayList<String> shuffleTerritoryNames() {
    ArrayList<String> list = new ArrayList<String>(Arrays.asList(Map.TERRITORY_NAME_LIST));
    Collections.shuffle(list);
    return list;
  }

  /*****
   * Group territory ids according to player number
   ****/
  private ArrayList<ArrayList<Integer>> groupTerritories(int playerNum) {
    ArrayList<ArrayList<Integer>> list = new ArrayList<ArrayList<Integer>>();
    switch (playerNum) {
      case 2:
        list.add(new ArrayList<Integer>(Arrays.asList(2, 5, 9)));
        list.add(new ArrayList<Integer>(Arrays.asList(6, 10, 13)));
        break;
      case 3:
        list.add(new ArrayList<Integer>(Arrays.asList(1, 4, 9)));
        list.add(new ArrayList<Integer>(Arrays.asList(5, 10, 13)));
        list.add(new ArrayList<Integer>(Arrays.asList(2, 6, 11)));
        break;
      case 4:
        list.add(new ArrayList<Integer>(Arrays.asList(1, 2, 5)));
        list.add(new ArrayList<Integer>(Arrays.asList(3, 6, 7)));
        list.add(new ArrayList<Integer>(Arrays.asList(9, 10, 13)));
        list.add(new ArrayList<Integer>(Arrays.asList(11, 14, 15)));
        break;
      case 5:
        list.add(new ArrayList<Integer>(Arrays.asList(0, 4, 8)));
        list.add(new ArrayList<Integer>(Arrays.asList(1, 2, 3)));
        list.add(new ArrayList<Integer>(Arrays.asList(5, 9, 12)));
        list.add(new ArrayList<Integer>(Arrays.asList(6, 7, 10)));
        list.add(new ArrayList<Integer>(Arrays.asList(11, 13, 14)));
        break;
    }
    return list;
  }

  // assign pid, tid and name to territories
  private ArrayList<Territory> assignTidandName(ArrayList<String> names, ArrayList<Integer> tids) {
    ArrayList<Territory> newMap = new ArrayList<Territory>();
    for (int i = 0; i < tids.size(); i++) {
      int pid = i / Map.INIT_T_NUM; // each player has three territories
      int tid = tids.get(i);
      String name = names.get(tid);
      Territory t = new Territory(pid, tid, name);
      newMap.add(t); // right now territories ordered as in tids
    }
    return newMap;
  }

  // assign neighbors to territories
  private void assignNeighborstoTs(ArrayList<Territory> list, ArrayList<Integer> tids) {
    for (Territory t : list) {
      for (int j = 0; j < Territory.MAX_NEIGHBOR; j++) {
        int nbID = t.calcNbID(j);
        if (nbID >= 0 && tids.contains(nbID)) {
          t.setNeighbor(j, nbID);
        }
      }
    }
  }

  // generate a random group of ints with size of 3, and sum equals to total
  public ArrayList<Integer> generateRandomAttriGroup(int total) {
    ArrayList<Integer> list = new ArrayList<Integer>();
    Random ran = new Random();
    // 1st number: min~total/2
    int min = Math.max(total / 3 - 1, 1);
    int max = total / 2;
    int result = ran.nextInt(max - min + 1) + min;
    list.add(result);
    // 2nd number: min~rest-min
    int rest = total - result;
    max = rest - min;
    result = ran.nextInt(Math.max(max, min) - Math.min(max, min) + 1) + Math.min(max, min);
    list.add(result);
    // 3rd number: rest
    rest = rest - result;
    list.add(rest);
    // shuffle list
    Collections.shuffle(list);
    return list;
  }

  // assign size, food production, gold production to territories
  private void assignAttributestoTs(ArrayList<Territory> list) {
    ArrayList<Integer> sizes = new ArrayList<Integer>(Map.INIT_T_NUM);
    ArrayList<Integer> foods = new ArrayList<Integer>(Map.INIT_T_NUM);
    ArrayList<Integer> golds = new ArrayList<Integer>(Map.INIT_T_NUM);
    for (int index = 0; index < list.size(); index++) {
      int pos = index % Map.INIT_T_NUM;
      if (pos == 0) {
        sizes = generateRandomAttriGroup(Map.INIT_T_SIZE);
        foods = generateRandomAttriGroup(Map.INIT_FOOD_PROD);
        golds = generateRandomAttriGroup(Map.INIT_GOLD_PROD);
      }
      list.get(index).set_terr_attributes(sizes.get(pos), foods.get(pos), golds.get(pos));
    }

  }

  /****
   * Initialize territories according to ordered territory names and tid groups
   ***/
  private ArrayList<Territory> initializeTerritories(ArrayList<String> names, ArrayList<ArrayList<Integer>> tidGroups) {
    // append all tids into a new list
    ArrayList<Integer> tids = new ArrayList<Integer>();
    for (ArrayList<Integer> group : tidGroups) {
      tids.addAll(group);
    }
    // assign pid, tid and name to territories
    ArrayList<Territory> newMap = assignTidandName(names, tids);
    // assign size, food production, gold production
    assignAttributestoTs(newMap);
    // set neighbors
    assignNeighborstoTs(newMap, tids);
    return newMap;

  }

  // Initialize a territory list for a new game, given the number of players
  private ArrayList<Territory> initializeTerritoryList(int playerNum) {
    // randomize the territory name order
    ArrayList<String> nameList = shuffleTerritoryNames();
    // group the tids according to player num
    ArrayList<ArrayList<Integer>> tidGroups = groupTerritories(playerNum);
    // assign group of territories to player id
    Collections.shuffle(tidGroups);
    // initialize territories
    return initializeTerritories(nameList, tidGroups);
  }

  // return true if the game exists and is active
  public boolean hasActiveGame(int gid) {
    for (Game g : gameList) {
      if (g.getGid() == gid && g.getStage() < GameMessage.GAME_OVER) {
        return true;
      }
    }
    return false;
  }

  public Game getGame(int gid) {
    for (Game g : gameList) {
      if (g.getGid() == gid) {
        return g;
      }
    }
    return null;
  }

  public static void main(String[] args) {
    // run the game
    Gameserver server = new Gameserver();
    server.run();
  }

}
