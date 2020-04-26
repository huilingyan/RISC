package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.commons.lang3.SerializationUtils;

import shared.Action;
import shared.ClientMessage;
import shared.Config;
import shared.ChatMessage;
import java.nio.charset.StandardCharsets;
import java.math.BigInteger;

public class GameClient {

    Socket serverSocket; // set up when connect
    ObjectInputStream inStream; // set up right before first recv
    ObjectOutputStream outStream; // set up when connect
    SocketChannel chatChannel;

    /****
     * Connect to the server host provided in config file
     ***/
    public void connectToServer() {
        Config config = new Config("config.properties");
        String host = config.readProperty("hostname");
        String port = config.readProperty("port");
        try {
            Socket newSocket = new Socket(host, Integer.parseInt(port));
            this.serverSocket = newSocket;
        } catch (IOException e) {
            System.out.println("Cannot connect to server at " + host + ": " + port);
        }
        // open outputstream
        try {
            this.outStream = new ObjectOutputStream(this.serverSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("fail to set up ObjectOutputStream");
        }
    }

    /****
     * Connect to the ChatServer
     ***/
    public void connectToChatServer() {
        Config config = new Config("config.properties");
        String host = config.readProperty("hostname");
        String chat_port = config.readProperty("chat_port");
        try {
            this.chatChannel = SocketChannel.open();
            this.chatChannel.connect(new InetSocketAddress(host, Integer.parseInt(chat_port)));
            chatChannel.configureBlocking(false);
            // debug
            System.out.println("Address: " + this.chatChannel.socket().getLocalAddress());
            System.out.println("Port num: " + this.chatChannel.socket().getLocalPort());

        } catch (IOException e) {
            System.out.println("IOException when connects to chat server");
            e.printStackTrace();
            System.exit(0);
        }

    }

    /*****
     * Initialize an inputstream from the socket. Call it right before receiving
     * RoomMessage from server
     ****/
    public void setUpInputStream() {
        try {
            this.inStream = new ObjectInputStream(this.serverSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /****
     * Recv an object from server, return null on error
     ****/
    public Object recvObject() {
        try {
            return this.inStream.readObject();
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
     * Send an object to server
     *****/
    public void sendObject(Object ob) {
        try {
            this.outStream.writeObject(ob);
        } catch (IOException e) {
            closeSocket();
            System.exit(0); // exit program if server's down }
        }
    }


    public void sendChatMsg(ChatMessage chatMsg) {
        byte[] chatBytes = SerializationUtils.serialize(chatMsg);
        ByteBuffer writeBuffer = ByteBuffer.wrap(chatBytes);
        try {
            this.chatChannel.write(writeBuffer);
            System.out.println("Send chat message to chatServer!");
        } catch (IOException e) {
            System.out.println("IOException when send message to chat server");
            e.printStackTrace();
            System.exit(0);
        }
        writeBuffer.clear();
    }


    /***
     * Send switch out message to server
     ***/
    public void sendSwitchOutMsg() {
        sendObject(new ClientMessage(0, 3, new Action()));
    }

    /****
     * Close the socket and any open output/input stream
     ****/
    public void closeSocket() {
        try {
            // chatOutStream.close();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to close");
        }
    }

    public SocketChannel getChatChannel() {
        return this.chatChannel;
    }

}