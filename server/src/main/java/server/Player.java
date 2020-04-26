package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import lombok.*;

/****
 * A class to represent a player (user) on game server side
 ******/
@Getter
@Setter
public class Player {

  private UserInfo userInfo;  
  private Socket clientSocket; // connected socket
  private ObjectOutputStream out;
  private ObjectInputStream in;


  /***
   * Initialize a Player object with a connected socket, and initialize its
   * outputstream
   * 
   * @param socket
   */
  public Player(Socket socket) {
    userInfo = new UserInfo();
    setSocketandOutputStream(socket);  // put at the end to set connect to true
  }

  // copy constructor
  public Player(Player rhs) {
    
    userInfo = new UserInfo(rhs.getUserInfo());
    clientSocket = rhs.getSocket();
    out = rhs.getOutputStream();
    in = rhs.getInputStream();
  }

  // default constructor that has no binding socket
  public Player(String name, String pass_word){
    userInfo = new UserInfo(name, pass_word);
  }

  // constructor that recover Player from saved UserInfo
  // DO NOT deep copy 
  public Player(UserInfo user){
    userInfo = user;
  }
  
  /****
   * Initialize an inputstream from the socket. Usually is called in gameserver
   * right before recv UserMessage from socket.
   ****/
  public void setUpInputStream() {
    if (userInfo.isConnected()) {
      try {
        in = new ObjectInputStream(clientSocket.getInputStream());
      } catch (IOException e) {
        userInfo.setConnected(false);
      }
    }
  }

  /***
   * Set up the player's username and password
   * @param name
   * @param pass_word
   */
  public void setUpUserInfo(String name, String pass_word){
    userInfo.setUsername(name);
    userInfo.setPassword(pass_word);
  }

  public void setSocketandOutputStream(Socket socket){
    clientSocket = socket;
    userInfo.setConnected(true);
    // initialize ObjectOutputStream
    try {
      out = new ObjectOutputStream(clientSocket.getOutputStream());
      out.flush();
    } catch (IOException e) {
      userInfo.setConnected(false);
    }
  }


  /****
   * Close all opened outputstream and inputstream and the socket
   ****/
  public void closeSocket() {
    try {
      out.close(); // close outputstream will also close the socket and inputstream
    } catch (IOException e) {
    }
  }

  /****
   * Send the object from server side to the player if is connected
   ****/
  public void sendObject(Object obj) {// send object to this player
    if (userInfo.isConnected()) {
      try {
        out.writeObject(obj);
      } catch (IOException e) {
        userInfo.setConnected(false);
      }
    }
  }

  /***
   * Recv an object from player if is connected if not connected, return null
   ****/
  public Object recvObject() {// receive object from this player
    if (userInfo.isConnected()) {
      try {
        Object ob = in.readObject();
        return ob;
      } catch (IOException e) {
        // IOException - Any of the usual Input/Output related exceptions.
        System.out.println("IOException when recv");
        userInfo.setConnected(false);
      } catch (ClassNotFoundException e) {
        // ClassNotFoundException - Class of a serialized object cannot be found.
        System.out.println("ClassNotFoundException when recv");
      }
    }
    return null;
  }

  public void updateSocketandStreams(Player p){
    clientSocket = p.getSocket();
    in = p.getInputStream();
    out = p.getOutputStream();
    userInfo.setConnected(true);   // is connected
    
  }

  public void setUsername(String p_name) {
    userInfo.setUsername(p_name);
  }

  public String getUsername() {
    return userInfo.getUsername();
  }

  public void setPassword(String pass_word){
    userInfo.setPassword(pass_word);
  }

  public String getPassword(){
    return userInfo.getPassword();
  }

  public void setActiveGid(int gid){
    userInfo.setActiveGid(gid);
  }

  public int getActiveGid(){
    return userInfo.getActiveGid();
  }

  // public void switchOut(){
  //   userInfo.setActiveGid(0);
  // }

  public Socket getSocket() {
    return clientSocket;
  }

  public ObjectInputStream getInputStream(){
    return in;
  }

  public ObjectOutputStream getOutputStream(){
    return out;
  }

  public void setConnected(boolean bool) {
    userInfo.setConnected(bool);
  }

  public boolean isConnected() {
    return userInfo.isConnected();
  }

  public void setLoggedin(boolean bool){
    userInfo.setLoggedin(bool);
  }

  public boolean isLoggedin() {
    return userInfo.isLoggedin();
  }

}
