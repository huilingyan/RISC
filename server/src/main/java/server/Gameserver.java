package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import shared.*;

/****
 * A class that acts like a game server for RISC
 ***/
public class Gameserver {

  public static final int UNIT_PER_PLAYER = 50;

  private ServerSocket mySocket; // server socket
  private ArrayList<Player> userList; // list of Player
  private ArrayList<Game> gameList; // list of games
  private int currentGid = 10;  // gid start from 10

  public Gameserver() {
    userList = new ArrayList<Player>();
    gameList = new ArrayList<Game>();
  }

  // run the server
  public void runGame() {
    bindSocket(); // initialize server socket
    // accept connection and assign to a ClientWorker
    while (true) {
      Socket newSocket;
      while ((newSocket = acceptConnection()) == null) {} // loops until accept one connection
      ClientWorker worker = new ClientWorker(newSocket, this);
      worker.start();
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

  public boolean hasUser(String name) {
    for (Player p : userList) {
      if (p.getUsername().equals(name)) {
        return true;
      }
    }
    return false;
  }

  public boolean isValidUser(String name, String password) {
    if (!hasUser(name)) {
      return false;
    }
    for (Player p : userList) {
      if (p.getUsername().equals(name) && p.getPassword().equals(password)) {
        return true;
      }
    }
    return false;
  }

  public ArrayList<Room> gatherRooms(String name) {
    ArrayList<Room> rooms = new ArrayList<Room>();
    for (Game g : gameList) {
      if (g.hasPlayer(name)) {
        rooms.add(new Room(g.getGid(), g.getPlayerNum(), g.isFull()));
      }
    }
    return rooms;
  }

  public synchronized Player updateUser(String name, Player p) {
    for (Player old : userList) {
      if (p.getUsername().equals(name)) {
        old.updateSocketandStreams(p);
        return old;
      }
    }
    return null; // not found, return null
  }

  public synchronized void addUser(Player p) {
    userList.add(p);
  }

  private synchronized void addGame(Game g) {
    gameList.add(g);
  }

  public Game startNewGame(int playerNum, Player firstP){
    // TODO: generate new map
    Map m = new Map();
    Game g = new Game(currentGid, playerNum, m, firstP);
    currentGid++;
    return g;
  }

  public boolean hasGame(int gid){
    for (Game g: gameList){
      if (g.getGid()==gid){
        return true;
      }
    }
    return false;
  }

  public Game getGame(int gid){
    for (Game g: gameList){
      if (g.getGid()==gid){
        return g;
      }
    }
    return null;
  }

  public static void main(String[] args) {
    // run the game
    Gameserver server = new Gameserver();
    server.runGame();
  }

}
