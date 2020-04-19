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

    // constructor
    public ChatClient() {}
 
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
               
        ChatMessage chatMsgSent = new ChatMessage(0, 1, "Msg from player");
        ByteBuffer writeBuffer = ByteBuffer.wrap(SerializationUtils.serialize(chatMsgSent));
        this.chatChannel.write(writeBuffer);
        writeBuffer.clear();

        readBuffer.clear();
        this.chatChannel.read(readBuffer);
        String recv = new String(readBuffer.array()).trim();
        // debug
        System.out.println("Received back from server: " + recv);

    }


    public static void main(String[] args) {
        // run the game
        ChatClient chatClient = new ChatClient();
        chatClient.run();
    }

}
