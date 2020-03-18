package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;

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
  boolean isActive;   // constructor
  
  public Gameclient(Scanner sc) {
    scanner = sc;
    inTaker = new InputTaker();
    displayer = Displayer.getInstance();
    isActive = true;
  }

  // Connect to the server at localhost and the given port
  private void connectToServer() {
    Config config = new Config("config.properties");
    String host = config.readProperty("hostname");
    String port = config.readProperty("port");
    try {
      Socket newSocket = new Socket(host, Integer.parseInt(port));
      serverSocket = newSocket;
    } catch (IOException e){
      System.out.println("Cannnot connect to server at " + host + " : " + port);
    }
    try {
      outStream = new ObjectOutputStream(serverSocket.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("fail to set up ObjectOutputStream");
    }
  }

  // Count the number of territories owned by the player
  private int getMyNumOfT(ArrayList<Territory> map) {
    int count = 0;
    for (Territory t : map) {
      if (t.getOwnership() == id) {
        count++;
      }
    }
    return count;
  }

  private int checkWinner(ArrayList<Territory> map) {
    int winner = map.get(0).getOwnership();
    for (int i = 1; i < map.size(); i++) {
      if (map.get(i).getOwnership() != winner) {
        return -1;   // no winner yet
      }
    }
    return winner;
  }

  // TODO
  private void promptForAction(ArrayList<Territory> map) {
    OperationValidator validator = new OperationValidator(id, map);
    while (true) {  // prompt for one operation in each while loop
      displayer.displayIntroduction(id);
      String selection = inTaker.readselectionStr(scanner);
      int state;
      if (selection.equalsIgnoreCase("D")) {  // player finish entering operations
        break;      // break the loop and send action
      } else if (selection.equalsIgnoreCase("M")) {
        // move operation
        MoveOperation op = inTaker.readMoveOperation(scanner);
        state = validator.isValidMoveOperation(op);
        if (state < 0) {  // invalid move
          displayer.showErrorMsg(state);
        } else {    
          displayer.moveUnits(op);
        }
      } else {     // attack operation
        AttackOperation op = inTaker.readAttackOperation(scanner);
        state = validator.isValidAttackOperation(op);
        if (state < 0) {   // invalid attack
          displayer.showErrorMsg(state);
        } else {
          displayer.attackUnits(op);
        }
        
      }  // if/else
    }  // while
    // send action to server
    sendObject(validator.getAction());
  }

  // TODO: change later
  private void playGame() {
    while (true) {   // start each turn
      ArrayList<Territory> map = (ArrayList<Territory>) recvObject();
      if (map == null) {  // server's down
        break;
      }
      displayer.showCurrentMap(map);  // display the map
      int myTCount = getMyNumOfT(map);
      if (myTCount == map.size()) {        // win the game
        displayer.winnerAnnouncement();  
        break;
      } else if (myTCount == 0) {          // lose the game
        int winner = checkWinner(map);
        if (winner >= 0) {
          displayer.gameOverAnnouncement(winner);
          closeSocket();
          System.exit(0);
        }
        if (isActive) {    // just lose the game in this turn
          isActive = false;
          displayer.loseGameAnnouncement();  // lose game message
        }
        displayer.askForExit();
        boolean exit = inTaker.readYorN(scanner);
        if (exit) {   // exit
          closeSocket();
          System.exit(0);
        }
        // let server check inactive player and don't receive action from them
        continue;    // watch the game, continue
      } else {
        // regular game process
        promptForAction(map);
      }

    }  // while
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
      closeSocket();
      System.exit(0);   // exit program if server's down
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
      closeSocket();
      System.exit(0);  // exit program if server's down
    }
  }

  private int recvPosInt() {  // receive player number, which should be no less than 0
    try {
      int num = inStream.readInt();
      return num;
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Fail to recv int");
      closeSocket();
      System.exit(0);  // exit program if server's down
    }
    return -1;
  }

  private Object recvObject() {
    try {
      return inStream.readObject();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Fail to recv object");
      closeSocket();
      System.exit(0);  // exit program if server's down
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      System.out.println("Class not found");
      closeSocket();
      System.exit(0);  // exit program if server's down
    }
    return null;
  }

  private void sendObject(Object ob) {
    try {
      outStream.writeObject(ob);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Fail to send object");
      closeSocket();
      System.exit(0);  // exit program if server's down
    }

  }

  private void receivePlayerNum() {
    playerNum = recvPosInt();
    if (playerNum < 0){
      closeSocket();
      System.exit(0);  // exit program if server's down
    }
    displayer.setNumOfPlayer(playerNum);
    // debug
    System.out.println("Number of players: " + playerNum);
  }

  private void sendTNamesToIntaker(ArrayList<Territory> map) {
    HashSet<String> set = new HashSet<String>();
    for (Territory t : map) {
      set.add(t.getName());
    }
    inTaker.setTNameSet(set);
  }

  // Receive initial map from server and set up units in each territory belonged to the player
  private void initializeUnits() {
    int totalUnit = recvPosInt();
    ArrayList<Territory> gameMap = (ArrayList<Territory>)recvObject();
    // set hashmap to inputtaker
    sendTNamesToIntaker(gameMap);
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
    connectToServer();
    receiveID();
    if (id == 0) {
      promptAndSendPlayerNum();
    } else {
      receivePlayerNum();
    }
    // initialize units
    initializeUnits();
  }

  private void closeSocket() {
    try {
      outStream.close();
    } catch (IOException e) {
      System.out.println("Failed to close outputstream");
    }
  }
  
  public void runGame() {
    initializeGame();
    playGame();
    closeSocket();
  }

  public static void main(String[] args) {
    Gameclient game = new Gameclient(new Scanner(System.in));
    game.runGame();
  }
}
