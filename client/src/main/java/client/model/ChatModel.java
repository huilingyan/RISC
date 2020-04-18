package client.model;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import shared.ChatMessage;

public class ChatModel {
  private Socket chatSkt;
  private ObjectOutputStream out;

  public ChatModel(String hostname, int port) {
    try{
      this.chatSkt=new Socket(hostname, port);
      this.out=new ObjectOutputStream(chatSkt.getOutputStream());
    }catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O Error: " + ex.getMessage());
        }
  }

  public void sendonemessage(ChatMessage cm) {
    try {
      out.writeObject(cm);
    } catch (IOException ex) {
      System.out.println("I/O Error in sending msg to server: " + ex.getMessage());
    }
  }

  public void startreadthread(PrintMessage pm) {
    new ReadCMThread(chatSkt, pm).start();
  }
}
