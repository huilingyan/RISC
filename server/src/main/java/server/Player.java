package server;

import java.net.Socket;
import java.util.ArrayList;

import shared.Territory;

public class Player {
  private int pid;//player id suppose 1-5
  private String name; //player name
  private Socket client_socket; //not sure if this is the right type
  private ArrayList<Territory> player_territory;//can store tid instead
  private boolean active;//still has territory
  private boolean connected;//socket connection status

  public Player() {
  };

  public Player(int p_id, String p_name) {
    pid = p_id;
    name = p_name;
    player_territory = new ArrayList<Territory>();
    //rest info should be added after socket setup
  }

  public Player(Player rhs){
    pid = rhs.pid;
    name = rhs.name;
    client_socket = rhs.client_socket;
    player_territory = rhs.player_territory;
    active = rhs.active;
    connected = rhs.connected;
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
    client_socket= client;
  }

  public Socket getSocket() {
    return client_socket;
  }

  public void setPlayerTerritory(ArrayList<Territory> territories) {
     player_territory = territories;
  }

  public ArrayList<Territory> getPlayerTerritory() {
    return player_territory;
  }

  public void addTerritory(Territory t){
    for (int i = 0; i < player_territory.size(); i++) {
      if (t.getTid() == player_territory.get(i).getTid()) {
        //adding duplicate territories
        //cancel adding and exit
        return;
      }
    }
    player_territory.add(t);
  }

  public void deleteTerritory(Territory t) {
    for (int i = 0; i < player_territory.size(); i++) {
      if (t.getTid() == player_territory.get(i).getTid()) {
        player_territory.remove(i);
        return;
      }
    }
    //may need to throw exception here if no territory matches t
  }

  public void setActive(boolean t_or_f) {
    active = t_or_f;
  }

  public boolean checkActive() {
    if (player_territory.isEmpty()) {
      active = false;
    }
    return active;
  }
  
  public boolean isActive() {
    return active;
  }

  public void setConnected(boolean t_or_f) {
    connected = t_or_f;
  }

  public boolean isConnected() {
    return connected;
  }
}
