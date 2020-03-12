package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;

import shared.*;

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
      // System.out.println("start create inputstream");
      ObjectInputStream in = new ObjectInputStream(serverSocket.getInputStream());
      // System.out.println("inputstream created");
      inStream = in;
      id = in.readInt();
      // System.out.println(id);
    }
    catch (IOException e){
      e.printStackTrace();
      System.out.println("IOException");
    }
    // call displayer, display connection message with pid
    displayer.connEstablishedMsg(id);
    
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

  private int recvInt() {
    try {
      int num = inStream.readInt();
      return num;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Fail to recv int");
    }
    return 0;
  }

  private Object recvObject() {
    try {
      return inStream.readObject();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Fail to recv object");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.out.println("Class not found");
    }
    return null;
  }

  private void sendObject(Object ob) {
    try {
      outStream.writeObject(ob);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Fail to send object");
    }

  }

  private void receivePlayerNum() {
    playerNum = recvInt();
    displayer.setNumOfPlayer(playerNum);
    // debug
    System.out.println("Number of players: " + playerNum);
  }

  // TODO
  private void initializeUnits() {
    int totalUnit = recvInt();
    ArrayList<Territory> gameMap = (ArrayList<Territory>)recvObject();
    // for each territory, prompt for num of unit
    OperationValidator validator = new OperationValidator(id, gameMap);
    for (Territory t : gameMap) {
      if (t.getOwnership() == id) {
        int state = -1;
        while (state < 0) {
          InitOperation op;
          displayer.displayBeforeInitMap();
          displayer.displayOnePlayerMap(id, validator.getCurrentMapState()); // show map
          String name = t.getName();
          displayer.showRemainUnitNumber(validator.getRemainingUnit(totalUnit), name); // ask for unit
          op = inTaker.readInitOperation(name, scanner);
          state = validator.isValidInitOperation(op, totalUnit);
          if (state > 0) {
            displayer.deployUnits(op);
          } else {
            displayer.showErrorMsg(state);
          }

        } // while
      } // if
    } // for
    // send action to server
    sendObject(validator.getAction());
  }

  private void initializeGame() {
    connectToServer(4444);
    receiveID();
    if (id == 0) {
      promptAndSendPlayerNum();
    } else {
      receivePlayerNum();
    }
    // TODO: initialize units
    initializeUnits();
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
