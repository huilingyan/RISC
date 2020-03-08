package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import shared.Territory;

public class Player {
  private int pid;//player id suppose 1-5
  private String name; //player name
  private Socket clientSocket; //not sure if this is the right type
  private ArrayList<Territory> playerTerritories;//can store tid instead
  private boolean active;//still has territory
  private boolean connected;//socket connection status
  private ObjectOutputStream out;
  private ObjectInputStream in;

  public Player() {
    playerTerritories = new ArrayList<Territory>();
    
  };

  public Player(int p_id, String p_name, Socket socket) {
    pid = p_id;
    name = p_name;
    clientSocket = socket;
    playerTerritories = new ArrayList<Territory>();
    active = true;
    connected = true;
    try{
    out = new ObjectOutputStream(clientSocket.getOutputStream());
    } catch (IOException e) {
    }
    try{
    in  = new ObjectInputStream(clientSocket.getInputStream());
    } catch(IOException e){}
    //rest info should be added after socket setup (playerTerritories)
  }

  public Player(Player rhs){
    pid = rhs.pid;
    name = rhs.name;
    clientSocket = rhs.clientSocket;
    playerTerritories = rhs.playerTerritories;
    active = rhs.active;
    connected = rhs.connected;
  }

  public void sendObject(Object obj) {//send object to this player
    try {
      out.writeObject(obj);
    } catch (IOException e) {
      System.out.println("send object failed\n");
    }
  }

  public Object recvObject() {//receive object from this player
    try{
      return in.readObject();
    } catch (Exception e) {//IO and ClassNotFound
      System.out.println("recv object failed\n");
    }
    return null;//may need to change to other type
  }
  
  public void setPid(int p_id) {
    pid = p_id;
  }

  public int getPid() {
    return pid;
  }

  public void setName(String p_name) {
    name = p_name;
  }

  public String getName() {
    return name;
  }

  public void setSocket(Socket client) {
    clientSocket= client;
  }

  public Socket getSocket() {
    return clientSocket;
  }

  public void setPlayerTerritory(ArrayList<Territory> territories) {
     playerTerritories = territories;
  }

  public ArrayList<Territory> getPlayerTerritory() {
    return playerTerritories;
  }

  public void addTerritory(Territory t){
    for (int i = 0; i < playerTerritories.size(); i++) {
      if (t.getTid() == playerTerritories.get(i).getTid()) {
        //adding duplicate territories
        //cancel adding and exit
        return;
      }
    }
    playerTerritories.add(t);
  }

  public void deleteTerritory(Territory t) {
    for (int i = 0; i < playerTerritories.size(); i++) {
      if (t.getTid() == playerTerritories.get(i).getTid()) {
        playerTerritories.remove(i);
        return;
      }
    }
    //may need to throw exception here if no territory matches t
  }

  public void setActive(boolean bool) {
    active = bool;
  }

  public boolean checkActive() {
    if (playerTerritories.isEmpty()) {
      active = false;
    }
    return active;
  }
  
  public boolean isActive() {
    return active;
  }

  public void setConnected(boolean bool) {
    connected = bool;
  }

  public boolean isConnected() {
    return connected;
  }
}