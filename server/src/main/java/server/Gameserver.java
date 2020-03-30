package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import shared.*;

/****
A class that acts like a game server for RISC
 ***/
public class Gameserver {

  public static final int UNIT_PER_PLAYER = 50;
  
  private ServerSocket mySocket;         // server socket
  private ArrayList<Player> userList;  // list of Player
  private ArrayList<Game> gameList;  // list of games

  
  public Gameserver() {
    userList = new ArrayList<Player>();
    gameList = new ArrayList<Game>();
  }

  public boolean hasUser(String name){
    for (Player p: userList){
      if (p.getUsername().equals(name)){
        return true;
      }
    }
    return false;
  }

  public boolean isValidUser(String name, String password){
    if (!hasUser(name)){
      return false;
    }
    for (Player p: userList){
      if (p.getUsername().equals(name) && p.getPassword().equals(password)){
        return true;
      }
    }
    return false;
  }

  public ArrayList<Room> gatherRooms(String name){
    ArrayList<Room> rooms = new ArrayList<Room>();
    for (Game g: gameList){
      if (g.hasPlayer(name)){
        rooms.add(new Room(g.getGid(), g.getPlayerNum(), g.isFull()));
      }
    }
    return rooms;
  }

  public synchronized void addUser(Player p){
    userList.add(p);
  }

  public synchronized void addGame(Game g){
    gameList.add(g);
  }

  
  
 
}
