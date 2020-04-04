package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/****
 * A class to represent a player (user) on game server side
 ******/
public class Player {

  private String username; // user name
  private String password; // user passwors
  private Socket clientSocket; // connected socket
  private ObjectOutputStream out;
  private ObjectInputStream in;
  private boolean connected; // socket connection status
  private int activeGid;  // gameID of active game, 0 if in not on name
  private boolean loggedin;   // TODO: mark if user's loggedin

  /***
   * Initialize a Player object with a connected socket, and initialize its
   * outputstream
   * 
   * @param socket
   */
  public Player(Socket socket) {
    setSocketandOutputStream(socket);
    activeGid = 0;  // initialize active game to 0 (not at game)
    loggedin = false;  // user not logged in
  }

  // copy constructor
  public Player(Player rhs) {
    username = rhs.getUsername();
    password = rhs.getPassword();
    clientSocket = rhs.getSocket();
    out = rhs.getOutputStream();
    in = rhs.getInputStream();
    connected = rhs.isConnected();
    activeGid = rhs.getActiveGid();
    loggedin = rhs.isLoggedin();
  }

  
  /****
   * Initialize an inputstream from the socket. Usually is called in gameserver
   * right before recv UserMessage from socket.
   ****/
  public void setUpInputStream() {
    if (connected) {
      try {
        in = new ObjectInputStream(clientSocket.getInputStream());
      } catch (IOException e) {
        connected = false;
      }
    }
  }

  /***
   * Set up the player's username and password
   * @param name
   * @param pass_word
   */
  public void setUpUserInfo(String name, String pass_word){
    username = name;
    password = pass_word;
  }

  public void setSocketandOutputStream(Socket socket){
    clientSocket = socket;
    connected = true;
    // initialize ObjectOutputStream
    try {
      out = new ObjectOutputStream(clientSocket.getOutputStream());
      out.flush();
    } catch (IOException e) {
      connected = false;
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
    if (connected) {
      try {
        out.writeObject(obj);
      } catch (IOException e) {
        connected = false;
      }
    }
  }

  /***
   * Recv an object from player if is connected if not connected, return null
   ****/
  public Object recvObject() {// receive object from this player
    if (connected) {
      try {
        Object ob = in.readObject();
        System.out.println(ob.toString());
        return ob;
      } catch (IOException e) {
        // IOException - Any of the usual Input/Output related exceptions.
        System.out.println("IOException when recv");
        connected = false;
      } catch (ClassNotFoundException e) {
        // ClassNotFoundException - Class of a serialized object cannot be found.
        System.out.println("ClassNotFoundException when recv");
      }
    }
    return null;// may need to change to other type
  }

  /****
   * Send an int to the player if connected
   ****/
  public void sendInt(int val) {// send int to this player
    if (connected) {
      try {
        out.writeInt(val);
        out.flush();
      } catch (IOException e) {
        connected = false;
      }
    }
  }

  /***
   * Recv a non-negative int from the player if connected if not connected, return
   * -1
   ****/
  public int recvPosInt() {// receive int (>=0) from this player
    if (connected) {
      try {
        return in.readInt();
      } catch (EOFException e) {
        // EOFException - If end of file is reached.
        connected = false;
      }

      catch (IOException e) {
        // IOException - If other I/O error has occurred.
        connected = false;
      }
    }
    return -1;
  }

  public void updateSocketandStreams(Player p){
    clientSocket = p.getSocket();
    in = p.getInputStream();
    out = p.getOutputStream();
    connected = true;   // is connected
    
  }

  public void setUsername(String p_name) {
    username = p_name;
  }

  public String getUsername() {
    return username;
  }

  public void setPassword(String pass_word){
    password = pass_word;
  }

  public String getPassword(){
    return password;
  }

  public void setActiveGid(int gid){
    activeGid = gid;
  }

  public int getActiveGid(){
    return activeGid;
  }

  public void switchOut(){
    activeGid = 0;
  }

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
    connected = bool;
  }

  public boolean isConnected() {
    return connected;
  }

  public void setLoggedin(boolean bool){
    loggedin = bool;
  }

  public boolean isLoggedin() {
    return loggedin;
  }

}
