package server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import shared.*;

public class Gameserver {

  public static final String[] PLAYER_NAME_LIST = {"Blue", "Red", "Green", "Yellow", "Purple"};  // hardcoded player name list
  public static final String[] TERRITORY_NAME_LIST = {"Pikachu", "Ditto", "Gengar", "Eevee", "Snorlax", "Mew", "Psyduck", "Magneton", "Vulpix", "Jumpluff", "Bulbasaur", "Charmandar", "Squirtle", "Pidgey", "Caterpie", "Rattata"};

  
  private ArrayList<Player> playerList;  // list of Player
  private ArrayList<Territory> gameMap;  // list of Territory
  private ServerSocket mySocket;         // server socket
  private int playerNum;

  public Gameserver() {
    playerList = new ArrayList<Player>();
    gameMap = new ArrayList<Territory>();
  }

  // return game map
  public ArrayList<Territory> getMap() {
    return gameMap;
  }

  // Generate a random ordered territory name list
  private ArrayList<String> shuffleTerritoryNames() {
    ArrayList<String> list = new ArrayList<String>(Arrays.asList(TERRITORY_NAME_LIST));
    Collections.shuffle(list);
    return list;
  }

  // Group territory ids according to player number
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

  // Initialize territories according to ordered territory names and tid groups, and store them to map
  private ArrayList<Territory> initializeTerritories(ArrayList<String> names, ArrayList<ArrayList<Integer>> tidGroups){
    // append all tids into a new list
    ArrayList<Integer> tids = new ArrayList<Integer>();
    for (ArrayList<Integer> group : tidGroups) {
      tids.addAll(group);
    }
    // initialize territory
    ArrayList<Territory> newMap = new ArrayList<Territory>();
    for (int i = 0; i < tids.size(); i++) {
      int pid = i / 3;   // each player has three territories
      int tid = tids.get(i);
      String name = names.get(tid);
      Territory t = new Territory(pid, tid, name);
      newMap.add(t);  // right now territories ordered as in tids
    }
    // set neighbors
    for (Territory t: newMap) {
      for (int j = 0; j < 6; j++) {
        int nbID = t.getNbID(j);
        if (nbID >= 0 && tids.contains(nbID)) {
          Territory nb = null;
          for (Territory _t : newMap) {
            if (_t.getTid() == nbID) {
              nb = _t;
              break;
            }
          }
          t.setNeighbor(j, nb);
        }
      }
    }
    return newMap;
    
  }
  
  // TODO: Initialize the game map with the given player number. Each territory has 0 unit (defender)
  public void initializeMap(int playerNum) {
    // randomize the territory name order
    ArrayList<String> nameList = shuffleTerritoryNames();
    // group the tids according to player num
    ArrayList<ArrayList<Integer>> tidGroups = groupTerritories(playerNum);
    // assign group of territories to player id
    Collections.shuffle(tidGroups);
    // initialize territories
    gameMap = initializeTerritories(nameList, tidGroups);
  }

  // Bind the server socket to the given port
  private void bindSocket(int port) {
    try{
      ServerSocket newSocket = new ServerSocket(port);
      mySocket = newSocket;
    } catch (IOException e) {
      System.out.println("Failed to bind socket to port " + port);
      System.exit(1);    // exit program if fail to bind socket
    }
  }

  // Accept a connection from client
  private Socket acceptConnection() {
    try{
      Socket newSocket = mySocket.accept();
      return newSocket;   // not sure if return socket in try block can work
    } catch (IOException e) {
      System.out.println("IOException when accept()");
    }
    return null;
  }

  // TODO: Change later. Accept a player and add it to player list
  private void acceptPlayer(int pid) {
    Socket newSocket;
    while ((newSocket = acceptConnection()) == null) {}  // loops until accept one connection
    System.out.println("Accepts player connection");
    String playerName = PLAYER_NAME_LIST[pid];
    playerList.add(new Player(pid, playerName, newSocket));
    System.out.println("added player");
  }

  private void acceptPlayers() {
    // accept first player
    acceptPlayer(0);  // add the first player to player list
    // send player id
    Player firstPlayer = playerList.get(0);
    firstPlayer.sendInt(0);  // send pid to first player
    System.out.println("sent pid 0");
    // receive player num
    firstPlayer.setUpInputStream();
    playerNum = firstPlayer.recvInt();
    System.out.println("Received player num: " + playerNum);
    // TODO: accept other players
    for (int i = 1; i < playerNum; i++){
      acceptPlayer(i);
      Player p = playerList.get(i);
      p.sendInt(i);  // send pid
      p.sendInt(playerNum);  // send player num
    }

  }

  // TODO
  private void initializeGame() {
    // bind socket
    bindSocket(4444);  // bind socket to port 4444
    acceptPlayers();
    initializeMap(playerNum);
    // TODO: initialize units
  }

  // TODO: change later
  private void playGame() {
    /*******
    ArrayList<Territory> list = new ArrayList<Territory>();
    list.add(new Territory(0, 0, "Test Territory"));
    list.get(0).setDefenderNum(50);
    System.out.println("start sending");
    playerList.get(0).sendObject(list);      
    *****/
    
    /*****    
    // TODO: send a territory to the first player for now
    Socket clientSocket = playerList.get(0).getSocket();
    // TODO: take ObjectOutputStream out from try with, and store it into playerlist
    try(ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
      ArrayList<Territory> list = new ArrayList<Territory>();
      list.add(new Territory(0, 0, "Test Territory", new Army(10)));
      System.out.println("start sending");
      out.writeObject(list);
      out.flush();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("IOException");
    }
    *****/
    while (true) {    // keep running
    }
  }
  
  public void runGame() {
    initializeGame();
    playGame();
    
  }
  
  public static void main(String[] args) {
    Gameserver server = new Gameserver();
    server.runGame();
  }
}
