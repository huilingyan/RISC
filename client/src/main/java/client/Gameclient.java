package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import shared.Territory;

public class Gameclient {

  Socket serverSocket;
  ObjectInputStream inStream;
  ObjectOutputStream outStream;
  int id; 

  // Connect to the server at localhost and the given port
  private void connectToServer(int port) {
    try {
      Socket newSocket = new Socket("127.0.0.1", port);
      serverSocket = newSocket;
    } catch (IOException e){
      System.out.println("Cannnot connect to server at localhost, port " + port);
    }
  }

  private void playGame() {
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
    while (true) {
    }
  }
  
  public void runGame() {
    connectToServer(4444);
    playGame();
  }

  public static void main(String[] args) {
    Gameclient game = new Gameclient();
    game.runGame();
  }
}
