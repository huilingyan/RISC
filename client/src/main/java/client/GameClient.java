package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import shared.Action;
import shared.ClientMessage;
import shared.Config;

public class GameClient {

    
    Socket serverSocket; // set up when connect
    ObjectInputStream inStream; // set up right before first recv
    ObjectOutputStream outStream; // set up when connect
    /****
    Connect to the server host provided in config file
    ***/
    private void connectToServer() {
        Config config = new Config("config.properties"); 
        String host = config.readProperty("hostname"); 
        String port = config.readProperty("port");
        try {
            Socket newSocket = new Socket(host, Integer.parseInt(port)); serverSocket = newSocket;
        } catch (IOException e) {
            System.out.println("Cannnot connect to server at " + host + ": " + port); 
        }
        // open outputstream
        try {
            outStream = new ObjectOutputStream(serverSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fail to set up ObjectOutputStream");
        } 
    }
    /****
     * Initialize an inputstream from the socket. Call it right before
    receiving RoomMessage from server
        ****/
    public void setUpInputStream() {
        if (connected) {
            try {
                inStream = new ObjectInputStream(clientSocket.getInputStream()); 
            } catch (IOException e) {
                e.printStackTrace();
            } 
        }
    }
        /****
        Recv an object from servre, return null on error
        ****/
    private Object recvObject() {
        try {
            return inStream.readObject();
        } catch (IOException e) {
            closeSocket();
            System.exit(0); // exit program if server's down
        } catch (ClassNotFoundException e) {
            closeSocket();
            System.exit(0); // exit program if server's down
        }
        return null;
    }
        
    /****
        Send an object to server
        *****/
    private void sendObject(Object ob) {
        try {
            outStream.writeObject(ob); 
        } catch (IOException e) {
            closeSocket();
            System.exit(0); // exit program if server's down }
        }
    }
    /***
     Send switch out message to server
    ***/
    public void sendSwitchOutMsg() {
        sendObject(new ClientMessage(0, 3, new Action())); 
    }
        /****
        Close the socket and any open output/input stream
        ****/
    private void closeSocket() {
        try { 
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to close");
        } 
    }
    
}