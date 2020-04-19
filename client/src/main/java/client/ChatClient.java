package client;

import org.apache.commons.lang3.SerializationUtils;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.util.Set;
import java.util.Iterator;
import java.lang.String;

import shared.*;

public class ChatClient extends Thread {

    SocketChannel chatChannel = null;
    String clientName;

    // constructor
    public ChatClient(String username) {
        this.clientName = username;
    }
 
    @Override
    public void run() {
        try {
            this.init();
            this.process();

        } catch (IOException e) {    
            e.printStackTrace();       
        } finally {
            try {
                if (this.chatChannel != null) {
                    this.chatChannel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } 
    }

    public void init() throws IOException {      
        this.chatChannel = SocketChannel.open();
        // this.chatChannel.configureBlocking(false);
        Config config = new Config("config.properties"); 
        String host = config.readProperty("hostname"); 
        String chat_port = config.readProperty("chat_port");
        this.chatChannel.connect(new InetSocketAddress(host, Integer.parseInt(chat_port)));
    }

    public void process() throws IOException {
        // ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        // ByteBuffer writeBuffer = ByteBuffer.wrap("Hey Dude!".getBytes());
               
        // readBuffer.clear();
        // this.chatChannel.read(readBuffer);
        // String recv = new String(readBuffer.array()).trim();
        // // debug
        // System.out.println("Received back from server: " + recv);
        while (true) {
            int readBytes = this.chatChannel.read(readBuffer);
            if (readBytes == 0) {
                continue;
            }
            while (readBytes > 0) {
                readBuffer.flip();
                while (readBuffer.hasRemaining()) {
                    // System.out.print((char) readBuffer.get());
                    readBuffer.get();
                }                                                          
                readBuffer.clear();
                readBytes = this.chatChannel.read(readBuffer);
            }
            if (readBytes == -1) {
                // key.cancel();
                this.chatChannel.close();
            }
            ChatMessage chatMsgRecv = (ChatMessage)SerializationUtils.deserialize(readBuffer.array());
            // String recv = new String(readBuffer.array()).trim();
            // debug
            System.out.println("The chat message is from: " + chatMsgRecv.getSrcPlayerName());
            System.out.println("To: " + chatMsgRecv.getDestPlayerName());
            System.out.println("Saying: " + chatMsgRecv.getMessage());
        }

    }

    public static void main(String[] args) {
        // run the game
        ChatClient chatClient = new ChatClient("Player");
        chatClient.run();
    }

}
