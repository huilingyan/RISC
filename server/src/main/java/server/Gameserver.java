package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


import shared.*;

/****
 * A class that acts like a game server for RISC
 ***/
public class Gameserver {

  private ServerSocket mySocket; // server socket
  private HashMap<String, Player> userList; // list of Player
  private ArrayList<Game> gameList; // list of games
  private int nextGid = 10; // gid start from 10

  public Gameserver() {
    userList = new HashMap<String, Player>();
    gameList = new ArrayList<Game>();
    // hibernate = new HibernateUtil();
  }

  /***
   * Run the server
   */
  public void run() {
    bindSocket(); // initialize server socket
    // load users from database
    loadUsers();
    // add admin users to list, if not exist
    addAdminUsers();
    // load games from database, update nextGid
    loadGames();
    // run gameworkers on existing games
    startGameWorkers();
    // create a thread for ChatServer
    ChatServer chatServer = new ChatServer(this);
    chatServer.start();
    // accept connection and assign to a ClientWorker
    while (true) {
      Socket newSocket;
      while ((newSocket = acceptConnection()) == null) {
      } // loops until accept one connection
      ClientWorker worker = new ClientWorker(newSocket, this);
      worker.start();
    }
  }

  private void startGameWorkers(){
    for (Game g: gameList){
      GameWorker gWorker = new GameWorker(g, this);
      gWorker.start();
    }
  }

  private void loadUsers() {
    List<UserInfo> users = HibernateUtil.getUserInfoList();
    for (UserInfo u : users) {
      if (!u.isOffline()) {
        u.setOffline();
        HibernateUtil.updateUserInfo(u);
      }
      appendUser(new Player(u));
    }
  }

  private void loadGames() {
    List<Game> games = HibernateUtil.getGameList();
    int currentGid = 9;
    for (Game g : games) {
      if (!g.getTempActionList().isEmpty()){
        g.setTempActionList(new HashMap<Integer, Action>());
        HibernateUtil.updateGame(g);
      }
      appendGame(g);
      currentGid = Math.max(currentGid, g.getGid()); // update currentGid
    }
    nextGid = currentGid + 1;
  }

  /***
   * Add 4 admin users to userlist, if not exist
   */
  private void addAdminUsers() {
    // admin1 to admin4
    for (int i = 1; i < 5; i++) {
      UserInfo user = new UserInfo("admin" + i, "1234");
      if (HibernateUtil.addUserInfoIfNone(user)) { // successfully saved to db
        appendUser(new Player(user));
      }
    }

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
   * 
   * @param name
   * @return
   */
  public boolean hasUser(String name) {
    if (userList.containsKey(name)) {
      return true;
    }
    return false;
  }

  /***
   * Check if username exists, password matches, and the user is currently logged
   * out
   * 
   * @param name
   * @param password
   * @return
   */
  public boolean isValidUser(String name, String password) {
    if (!hasUser(name)) {
      return false;
    }
    Player p = userList.get(name);
    if (p.getPassword().equals(password) && (!p.isLoggedin())) {
      return true;
    }
    return false;
  }

  /***
   * Return the roomlist visible to the player, given the username
   * 
   * @param name
   * @return
   */
  public synchronized ArrayList<Room> gatherRooms(String name) {
    ArrayList<Room> rooms = new ArrayList<Room>();
    for (Game g : gameList) {
      // filled active game with the player in, or not filled game
      if (g.hasPlayer(name) && g.getStage() < GameMessage.GAME_OVER || !g.isFilled()) {
        rooms.add(new Room(g.getGid(), g.getPlayerNum(), g.isFilled()));
      }
    }
    return rooms;
  }

  public synchronized Player updateSocketForUser(String name, Player p) {
    Player old = userList.get(name);
    System.out.println("update socket info");
    old.updateSocketandStreams(p); // connected is set to true
    return old;
    
  }

  public void appendUser(Player p) {
    userList.put(p.getUsername(), p);
  }

  /**
   * Add a copy of Player p to the list, and set connected and logged in to be
   * false
   * 
   * @param p
   */
  public void addUserCopyToList(Player p) {
    Player copy = new Player(p);
    copy.setConnected(false);
    copy.setLoggedin(false);
    synchronized (this) {
      userList.put(copy.getUsername(), copy); // add a copy of Player p to the list
    }

  }

  private void appendGame(Game g) {
    gameList.add(g);
  }

  private synchronized void addGame(Game g) {
    gameList.add(g);
    // save game to database
    HibernateUtil.addGame(g);
    nextGid++; // increment gid counter
  }

  public Game startNewGame(int playerNum, UserInfo firstP) {
    // generate a new map with only territory list
    Map m = new Map(initializeTerritoryList(playerNum));
    Game g = new Game(nextGid, playerNum, m, firstP.getUsername());
    addGame(g);
    return g;
  }

  /***
   * @return true if all active player has sent action to server, which means one
   *         turn just finished
   */
  public boolean turnFinished(Game g) {
    for (String name : g.getPlayerList()) {
      Player p = userList.get(name);
      if (p.isConnected() && p.isLoggedin() && p.getActiveGid() == g.getGid()) {
        int pid = g.getPidByName(p.getUsername());
        if (!g.getTempActionList().containsKey(pid)) {
          // debug
          // System.out.println("pid " + pid + "is in game but not yet sends action");
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
  public boolean noActivePlayer(Game g) {
    for (String name : g.getPlayerList()) {
      Player p = userList.get(name);
      // is active player
      if (p.isConnected() && p.isLoggedin() && p.getActiveGid() == g.getGid()) {
        return false;
      }
    }
    return true;
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

  public void deleteGame(Game g) {
    gameList.remove(g);
    // delete from database
    HibernateUtil.deleteGame(g);
  }

  public int getActiveGidByName(String username) {
    return this.userList.get(username).getActiveGid();
  }

  public static void main(String[] args) {
    // run the game
    Gameserver server = new Gameserver();
    server.run();
  }

}
