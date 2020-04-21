package client.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import shared.ChatMessage;

public class ReadCMThread extends Thread {
  private PrintMessage pm;
  private Socket skt;

  private ObjectInputStream in;

  public ReadCMThread(Socket s, PrintMessage p) {
    this.skt = s;
    this.pm = p;
    try{
      this.in=new ObjectInputStream( skt.getInputStream()); 
    } catch (IOException ioe) {
      System.out.println("failed to create object input stream in readcmthread.");
    }
  }

  public void run() {
    while (true) {
      try{
        ChatMessage cm=(ChatMessage) in.readObject();
        pm.printMsg(cm);
      } catch (ClassNotFoundException cnfe) {
        System.out.println("fialed to read object from server.");
      } catch (IOException ioe) {
        System.out.println("fialed to read object from server.");
      }
    }
  }
}
