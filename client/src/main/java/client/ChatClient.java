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
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import shared.*;

public class ChatClient extends Thread {

    SocketChannel chatChannel = null;
    String clientName;

    // constructor
    public ChatClient(String username, SocketChannel chatChannel) {
        this.clientName = username;
        this.chatChannel = chatChannel;
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
        // this.chatChannel = SocketChannel.open();
        // this.chatChannel.configureBlocking(false);
        // Config config = new Config("config.properties"); 
        // String host = config.readProperty("hostname"); 
        // String chat_port = config.readProperty("chat_port");
        // this.chatChannel.connect(new InetSocketAddress(host, Integer.parseInt(chat_port)));
        // debug
        System.out.println("Started a chatClient");       
        System.out.println("chatClient Address: " + this.chatChannel.socket().getLocalAddress());
        System.out.println("chatClient Port num: " + this.chatChannel.socket().getLocalPort());
    }

    public void process() throws IOException {
        // debug: send chatmessage
        // ChatMessage msg = new ChatMessage("user1", "user2", "test message");
        
        // ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        // byte[] chatBytes = SerializationUtils.serialize(msg);
        // String str = new String(chatBytes, StandardCharsets.UTF_8);
        // System.out.println(str);
        // String newStr = String.format("%040x", new BigInteger(1, str.getBytes(StandardCharsets.UTF_8)));
        // Hex.encodeHexString(str.getBytes(StandardCharsets.UTF_8));
        // System.out.println(newStr);
        // ByteBuffer writeBuffer = ByteBuffer.wrap(chatBytes);
        // this.chatChannel.write(writeBuffer);
        // str = new String(chatBytes, StandardCharsets.UTF_8);
        // System.out.println(new String(chatBytes, StandardCharsets.UTF_8));
        // newStr = String.format("%040x", new BigInteger(1, str.getBytes(StandardCharsets.UTF_8)));
        // Hex.encodeHexString(str.getBytes(StandardCharsets.UTF_8));
        // System.out.println(newStr);
        // writeBuffer.clear();
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
            
            // while (readBytes > 0) {
            //     // debug
            //     System.out.println("received message, length " + readBytes);
            //     readBuffer.flip();
            //     while (readBuffer.hasRemaining()) {
            //         // System.out.print((char) readBuffer.get());
            //         System.out.println("has remaining");
            //         readBuffer.get();
            //     }
            //     System.out.println("out of has remaining loop");                                                          
            //     readBuffer.clear();
            //     System.out.println("clear read buffer");
            //     readBytes = this.chatChannel.read(readBuffer);
            //     System.out.println(readBytes);
            // }
            if (readBytes == -1) {
                // key.cancel();
                this.chatChannel.close();
                System.out.println("close channel");
            }
            System.out.println(readBuffer.array().length);
            ChatMessage chatMsgRecv = (ChatMessage)SerializationUtils.deserialize(readBuffer.array());
            readBuffer.clear();
            // String recv = new String(readBuffer.array()).trim();
            // debug
            System.out.println("The chat message is from: " + chatMsgRecv.getSrcPlayerName());
            System.out.println("To: " + chatMsgRecv.getDestPlayerName());
            System.out.println("Saying: " + chatMsgRecv.getMessage());
        }

    }

    // public static void main(String[] args) {
    //     // run the game
    //     ChatClient chatClient = new ChatClient("Player");
    //     chatClient.run();
    // }

}
