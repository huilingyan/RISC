package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import shared.*;

public class Gameserver {

  public static final String[] PLAYER_NAME_LIST = {"Blue", "Red", "Green", "Yellow", "Purple"};  // hardcoded player name list

  private ArrayList<Player> playerList;  // list of Player
  private ArrayList<Territory> gameMap;  // list of Territory
  private ServerSocket mySocket;         // server socket

  // Bind the server socket to the given port
  private void bindSocket(int port) {
    try (ServerSocket newSocket = new ServerSocket(port)){
      mySocket = newSocket;
    } catch (IOException e) {
      System.out.println("Failed to bind socket to port " + port);
      System.exit(1);    // exit program if fail to bind socket
    }
  }

  // Accept a connection from client
  private Socket acceptConnection() {
    try(Socket newSocket = mySocket.accept()){
      return newSocket;   // not sure if return socket in try block can work
    } catch (IOException e) {
      System.out.println("IOException when accept()");
    }
    return null;
  }

  // Add a player to playerList
  private void addPlayer(int id, Socket playerSocket) {
    String playerName = PLAYER_NAME_LIST[id];
    playerList.add(new Player(id, playerName, playerSocket));
  }

  // TODO
  private void initializeGame() {
    // bind socket
    bindSocket(4444);  // bind socket to port 4444
    // accept first player
    Socket newSocket;
    while ((newSocket = acceptConnection()) == null) {}  // loops until accept one connection
    addPlayer(0, newSocket);  // add the first player to player list
    // TODO: receive player number

    // TODO: accept other players

    // TODO: initialize playerlist and territory list
    
  }

  // TODO: change later
  private void playGame() {
    // TODO: send a string to the first player for now
    
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
