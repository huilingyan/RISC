package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


import java.util.ArrayList;
import java.util.HashSet;

import shared.*;

/***
 * A class that functions as a game client
 ****/
public class Gameclient {

  Socket serverSocket; // set up when connect
  ObjectInputStream inStream; // set up right before first recv
  ObjectOutputStream outStream; // set up when connect
  Scanner scanner; // constructor
  String name;

  /****
   * The consructor that takes a scanner and initialize the InputTaker and
   * Displayer
   *****/
  public Gameclient(Scanner sc) {
    scanner = sc;
  }

  /****
   * Connect to the server host provided in config file
   ***/
  private void connectToServer() {
    Config config = new Config("config.properties");
    String host = config.readProperty("hostname");
    String port = config.readProperty("port");
    try {
      Socket newSocket = new Socket(host, Integer.parseInt(port));
      serverSocket = newSocket;
    } catch (IOException e) {
      System.out.println("Cannnot connect to server at " + host + " : " + port);
    }
    try {
      outStream = new ObjectOutputStream(serverSocket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("fail to set up ObjectOutputStream");
    }
  }

  /****
   * Initialize an inputstream from the socket. Call it right before receiving
   * RoomMessage from server
   ****/
  public void setUpInputStream() {
    try {
      inStream = new ObjectInputStream(serverSocket.getInputStream());
    } catch (IOException e) {
      System.out.println("Fail to set up input stream");
      e.printStackTrace();
    }
  }

  /****
   * Recv an object from servre, return null on error
   ****/
  private Object recvObject() {
    try {
      return inStream.readObject();
    } catch (IOException e) {
      closeSocket();
      System.exit(0); // exit program if server's down
    } catch (ClassNotFoundException e) {
      closeSocket();
      System.exit(0); // exit program if server's down
    }
    return null;
  }

  /****
   * Send an object to server
   *****/
  private void sendObject(Object ob) {
    try {
      outStream.writeObject(ob);
    } catch (IOException e) {
      closeSocket();
      System.exit(0); // exit program if server's down
    }

  }

  /****
   * Initailization stage of the game
   *****/
  private void initializeGame() {
    connectToServer();
    setUpInputStream();
  }

  /****
   * Close the socket and any open output/input stream
   ****/
  private void closeSocket() {
    try {
      outStream.close();
    } catch (IOException e) {
    }
  }

  private void register(){
    boolean success = false;
    while (!success){
      // user input
      System.out.println("User register");
      // username
      System.out.println("Type your username:");
      String username = scanner.nextLine();
      System.out.println(username);
      name = username;
      // password
      System.out.println("Type your password:");
      String password = scanner.nextLine();
      System.out.println(password);
      // send user message
      sendObject(new UserMessage(username, password, false));
      // receive room message
      RoomMessage msg = (RoomMessage)recvObject();
      // update success
      success = msg.isValid();
      System.out.println(success);
    }

  }

  
  private void login(){
    boolean success = false;
    while (!success){
      // user input
      System.out.println("User login");
      // username
      System.out.println("Type your username:");
      String username = scanner.nextLine();
      System.out.println(username);
      name = username;
      // password
      System.out.println("Type your password:");
      String password = scanner.nextLine();
      System.out.println(password);
      // send user message
      sendObject(new UserMessage(username, password, true));
      // receive room message
      RoomMessage msg = (RoomMessage)recvObject();
      // update success
      success = msg.isValid();
      System.out.println(success);
    }
  }

  /****
   * Run the game
   *****/
  public void runGame() {
    initializeGame();
    // register
    register();
    System.out.println("Successfully register as " + name);
    // login
    login();
    System.out.println("Successfully login as " + name);
    // initialize
    while(true){}
    // play

    // playGame();
    // closeSocket();
  }

  public static void main(String[] args) {
    // run the game
    Gameclient game = new Gameclient(new Scanner(System.in));
    game.runGame();
  }
}
