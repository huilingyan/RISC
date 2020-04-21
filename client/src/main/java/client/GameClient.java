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
    // The socket and stream for chatserver
    // Socket chatSocket; // socket for chatserver
    // ObjectOutputStream chatOutStream; // stream for chatserver
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
    // public void connectToChatServer() {
    // Config config = new Config("config.properties");
    // String host = config.readProperty("hostname");
    // String chat_port = config.readProperty("chat_port");
    // try {
    // Socket newChatSocket = new Socket(host, Integer.parseInt(chat_port));
    // this.chatSocket = newChatSocket;
    // } catch (IOException e) {
    // e.printStackTrace();
    // System.out.println("Cannot connect to chatServer at " + host + ": " +
    // chat_port);
    // }
    // // open outputstream
    // try {
    // this.chatOutStream = new
    // ObjectOutputStream(this.chatSocket.getOutputStream());
    // } catch (IOException e) {
    // e.printStackTrace();
    // System.out.println("fail to set up ObjectOutputStream");
    // }
    // }

    public void connectToChatServer() {
        Config config = new Config("config.properties");
        String host = config.readProperty("hostname");
        String chat_port = config.readProperty("chat_port");
        try {
            this.chatChannel = SocketChannel.open();
            this.chatChannel.connect(new InetSocketAddress(host, Integer.parseInt(chat_port)));
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

    /****
     * Send chatMessage to chatServer
     *****/
    // public void sendChatMsg(ChatMessage chatMsg) {
    // try {
    // this.chatOutStream.writeObject(chatMsg);
    // } catch (IOException e) {
    // closeSocket();
    // System.exit(0); // exit program if server's down }
    // }
    // }

    public void sendChatMsg(ChatMessage chatMsg) {
        byte[] chatBytes = SerializationUtils.serialize(chatMsg);
        ByteBuffer writeBuffer = ByteBuffer.wrap(chatBytes);
        try {
            this.chatChannel.write(writeBuffer);
        } catch (IOException e) {
            System.out.println("IOException when send message to chat server");
            e.printStackTrace();
            System.exit(0);
        }
        writeBuffer.clear();
    }

    /**
     * Only return a ChatMessage when receives one
     * @return
     */
    public ChatMessage recvChatMsg() {
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        while (true) {
            int readBytes = 0;
            try {
                readBytes = this.chatChannel.read(readBuffer);
            } catch (IOException e) {
                System.out.println("IOException when recv message from chat server");
                e.printStackTrace();
                System.exit(0);
            }
            if (readBytes == 0) {
                continue;  // no message, continue
            }
            // while (readBytes > 0) {
            // // debug
            // System.out.println("received message, length " + readBytes);
            // readBuffer.flip();
            // while (readBuffer.hasRemaining()) {
            // // System.out.print((char) readBuffer.get());
            // System.out.println("has remaining");
            // readBuffer.get();
            // }
            // System.out.println("out of has remaining loop");
            // readBuffer.clear();
            // System.out.println("clear read buffer");
            // readBytes = this.chatChannel.read(readBuffer);
            // System.out.println(readBytes);
            // }
            if (readBytes == -1) {
                // this.chatChannel.close();
                // System.out.println("close channel");
                System.out.println("readBytes = -1");
                System.exit(0);
            }
            // System.out.println(readBuffer.array().length);
            ChatMessage chatMsgRecv = (ChatMessage) SerializationUtils.deserialize(readBuffer.array());
            readBuffer.clear();
            // String recv = new String(readBuffer.array()).trim();
            // debug
            System.out.println("The chat message is from: " + chatMsgRecv.getSrcPlayerName());
            System.out.println("To: " + chatMsgRecv.getDestPlayerName());
            System.out.println("Saying: " + chatMsgRecv.getMessage());
            return chatMsgRecv;
        }

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

}