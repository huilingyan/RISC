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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Iterator;
import java.lang.String;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import client.controller.MainController;

import client.model.*;
import shared.*;

public class ChatClient extends Thread {

    SocketChannel chatChannel = null;
    String clientName;
    public MainController mc;
    // boolean exit = false;
    private final AtomicBoolean exit = new AtomicBoolean(false);

    // constructor
    public ChatClient(String username, MainController mc) {
        this.clientName = username;
        this.mc = mc;
        this.chatChannel = this.mc.getChatChannel();        
    }

    public void exit(){
        exit.set(true);
        System.out.println("set exit to true in chatclient");
    }

    // public boolean getExit(){
    //     return exit;
    // }
 
    @Override
    public void run() {
        try {
            this.init();
            this.process();
            System.out.println("chatclient exits");

        } catch (IOException e) {    
            e.printStackTrace();       
        } finally {

        } 
    }

    public void init() throws IOException {      

        // debug
        System.out.println("Started a chatClient");       
        System.out.println("chatClient Address: " + this.chatChannel.socket().getLocalAddress());
        System.out.println("chatClient Port num: " + this.chatChannel.socket().getLocalPort());
    }

    public void process() throws IOException {
 
        while (!exit.get()) {
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int readBytes = this.chatChannel.read(readBuffer);
            // System.out.println(readBytes);
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
            // System.out.println(readBuffer.array().length);
            ChatMessage chatMsgRecv = (ChatMessage)SerializationUtils.deserialize(readBuffer.array());
            readBuffer.clear();
            this.mc.appendToTextArea(chatMsgRecv.getSrcPlayerName(), chatMsgRecv.getMessage()); // show on text area
            // debug
            System.out.println("The chat message is from: " + chatMsgRecv.getSrcPlayerName());
            System.out.println("To: " + chatMsgRecv.getDestPlayerName());
            System.out.println("Saying: " + chatMsgRecv.getMessage());
        }
    }
}
