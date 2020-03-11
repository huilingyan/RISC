package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import shared.Displayer;
import shared.Territory;

public class Gameclient {

  Socket serverSocket; // set up when connect
  ObjectInputStream inStream; // set up right before first recv
  ObjectOutputStream outStream; // set up when connect
  int id; // set up when recv from server
  Scanner scanner; // constructor
  InputTaker inTaker; // constructor
  Displayer displayer; // constructor
  int playerNum;      // set up when user prompt/recv from server

  public Gameclient(Scanner sc) {
    scanner = sc;
    inTaker = new InputTaker();
    displayer = Displayer.getInstance();
  }

  // Connect to the server at localhost and the given port
  private void connectToServer(int port) {
    try {
      Socket newSocket = new Socket("127.0.0.1", port);
      serverSocket = newSocket;
    } catch (IOException e){
      System.out.println("Cannnot connect to server at localhost, port " + port);
    }
    try {
      outStream = new ObjectOutputStream(serverSocket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("fail to set up ObjectOutputStream");
    }
  }

// TODO: change later
private void playGame() {
/****
    System.out.println("Connect to server");
    try {
      System.out.println("start create inputstream");
      ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());
      inStream = in;
      // print out the Territory
      ArrayList<Territory> list;
      System.out.println("reading");
      list = (ArrayList<Territory>) in.readObject();
      System.out.println("recv done");
      Territory newT = list.get(0);
      System.out.println("Name: " + newT.getName());
      System.out.println("Army num:" + newT.getDefenderNum());
      
    }
    catch (IOException e){
      e.printStackTrace();
      System.out.println("IOException");
    }
    catch (ClassNotFoundException e) {
      System.out.println("class not found in ObjectInputStream.read");
    }
*****/
    while (true) {
    }
  }

  

  // Set up ObjectInputStream and receive player id from server
  private void receiveID() {
    try {
      System.out.println("start create inputstream");
      ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());
      System.out.println("inputstream created");
      inStream = in;
      id = in.readInt();
      System.out.println(id);
    }
    catch (IOException e){
      e.printStackTrace();
      System.out.println("IOException");
    }
  }

  private void promptAndSendPlayerNum() {
    // prompt for player number and send to server
    displayer.inputPlayerNum();
    // System.out.println("Please input number of players:");
    int num = inTaker.readnofPlayers(scanner);
    playerNum = num;
    displayer.setNumOfPlayer(playerNum);
    // debug
    System.out.println("Number of players: " + playerNum);
    // send to server
    try {
      outStream.writeInt(num);
      outStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Fail to send player num");
    }
  }

  private void receivePlayerNum() {
    try {
      int num = inStream.readInt();
      playerNum = num;
      displayer.setNumOfPlayer(playerNum);
      // debug
      System.out.println("Number of players: " + num);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Fail to recv player num");
    }
  }

  private void initializeGame() {
    connectToServer(4444);
    receiveID();
    // call displayer, display connection message with pid
    displayer.connEstablishedMsg(id);
    // System.out.println("Connect to server. PID = " + id);
    if (id == 0) {
      promptAndSendPlayerNum();
    } else {
      receivePlayerNum();
    }
    // TODO: initialize units
  }
  
  public void runGame() {
    initializeGame();
    playGame();
  }

  public static void main(String[] args) {
    Gameclient game = new Gameclient(new Scanner(System.in));
    game.runGame();
  }
}
