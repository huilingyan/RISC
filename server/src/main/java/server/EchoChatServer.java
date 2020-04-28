package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import shared.ChatMessage;

public class EchoChatServer {
  public static void main(String[] args) throws IOException {
    int portNumber = 2019;

    try (
        ServerSocket serverSkt = new ServerSocket(portNumber);
        Socket clientSkt = serverSkt.accept();
        ObjectOutputStream out = new ObjectOutputStream(clientSkt.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(clientSkt.getInputStream());
         ){
           while (true) {
             try {
               ChatMessage cm = (ChatMessage) in.readObject();
               out.writeObject(cm);
             } catch (ClassNotFoundException cnfe) {
               cnfe.printStackTrace();
             }
               
           }
    }catch (IOException ioe){
      ioe.printStackTrace();
    }
  }
}
