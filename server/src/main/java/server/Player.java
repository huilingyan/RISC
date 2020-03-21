package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/****
A class to represent a player on game server side
******/
public class Player {
  private int pid;//player id suppose 1-5
  private String name; //player name
  private Socket clientSocket; // connected socket
  private boolean active;//still has territory
  private boolean connected;//socket connection status
  private ObjectOutputStream out;
  private ObjectInputStream in;

  /*****
  Initialize a player object with pid, name and connected socket
  Also initialize an outputstream from the socket
  active and connected are default to be true  
   ****/
  public Player(int p_id, String p_name, Socket socket) {
    pid = p_id;
    name = p_name;
    clientSocket = socket;
    active = true;
    connected = true;
    // initialize ObjectOutputStream
    try{
    out = new ObjectOutputStream(clientSocket.getOutputStream());
    out.flush();
    } catch (IOException e) {
      e.printStackTrace();
      connected = false;
    }

  }

  /****
  Initialize an inputstream from the socket 
  Usually is called in gameserver right before recv from socket  
   ****/
  public void setUpInputStream() {
    if (connected){
    try{
    in  = new ObjectInputStream(clientSocket.getInputStream());
    } catch(IOException e){
      e.printStackTrace();
      connected = false;
    }
    }
  }

  /****
  Close all opened outputstream and inputstream and the socket  
   ****/
  public void closeSocket() {
    try {
      out.close();   // close outputstream will also close the socket and inputstream
    } catch (IOException e) {
      System.out.println("IOException when closing outputstream");
    }
  }

  /****
  Send the object from server side to the player if is connected  
   ****/
  public void sendObject(Object obj) {//send object to this player
    if (connected) {
      try {
        out.writeObject(obj);
      } catch (IOException e) {
        System.out.println("IOException: send object failed\n");
        connected = false;
      }
    }
  }

  /***
  Recv an object from player if is connected
  if not connected, return null  
  ****/
  public Object recvObject() {//receive object from this player
    if (connected) {
      try {
        return in.readObject();
      } catch (IOException e) {
        //IOException - Any of the usual Input/Output related exceptions.
        System.out.println("IOException: recv object failed\n");
        connected = false;
      } catch (ClassNotFoundException e) {
        //ClassNotFoundException - Class of a serialized object cannot be found.
        System.out.println("ClassNotFoundException: recv object failed\n");
      }
    }
    return null;//may need to change to other type
  }

  /****
  Send an int to the player if connected  
   ****/
  public void sendInt(int val) {//send int to this player
    if (connected) {
      try {
        out.writeInt(val);
        out.flush();
      } catch (IOException e) {
        System.out.println("IOException: send int failed\n");
        connected = false;
      }
    }
  }

  /***
  Recv a non-negative int from the player if connected
  if not connected, return -1  
  ****/
  public int recvPosInt() {//receive int (>=0) from this player
    if (connected) {
      try {
        return in.readInt();
      } catch (EOFException e) {
        //EOFException - If end of file is reached.
        System.out.println("EOFException: recv int failed\n");
        connected = false;
      }

      catch (IOException e) {
        //IOException - If other I/O error has occurred.
        System.out.println("IOException: recv int failed\n");
        connected = false;
      }
    }
    return -1;
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


  public void setActive(boolean bool) {
    active = bool;
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
