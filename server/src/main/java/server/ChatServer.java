package server;

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

/****
 * A class that implements the chat between players(server side)
 ***/
public class ChatServer extends Thread {

    // private static final int CHAT_PORT = 6666;

    private ServerSocketChannel ssc = null;
    private Selector selector = null;

    // constructor
    public ChatServer() {}
 
    @Override
    public void run() {
        try {
            this.init();
            this.process();

        } catch (IOException e) {    
            e.printStackTrace();  

        } finally {
            try {
                if (this.selector != null) {
                    this.selector.close();
                }
                if (this.ssc != null) {
                    this.ssc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void init() throws IOException {  

        this.ssc = ServerSocketChannel.open(); // open channel
        Config config = new Config("config.properties");
        String chat_port = config.readProperty("chat_port");
        this.ssc.socket().bind(new InetSocketAddress(Integer.parseInt(chat_port))); // bind to socket
        this.ssc.configureBlocking(false); // set non-blocking mode

        this.selector = Selector.open(); // open selector
        this.ssc.register(selector, SelectionKey.OP_ACCEPT); // register channel with the selector
    }

    public void process() throws IOException {   

        while (true) {           
            int nRead = this.selector.selectNow();
            if (nRead == 0) {
                continue;
            }       
            Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        // ServerSocketChannel ssChannel = (ServerSocketChannel)key.channel();
                        SocketChannel clientChannel = this.ssc.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        //debug
                        System.out.println("Accepted a connection!");
    
                    } else if (key.isReadable()) {
                        SocketChannel clientChannel = (SocketChannel)key.channel();
                        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                        int readBytes = clientChannel.read(readBuffer);
                        while (readBytes > 0) {
                            readBuffer.flip();
                            while (readBuffer.hasRemaining()) {
                                // System.out.print((char) readBuffer.get());
                                readBuffer.get();
                            }                                                          
                            readBuffer.clear();
                            readBytes = clientChannel.read(readBuffer);
                        }
                        if (readBytes == -1) {
                            // key.cancel();
                            clientChannel.close();
                        }
                        ChatMessage chatMsgRecv = (ChatMessage)SerializationUtils.deserialize(readBuffer.array());
                        // String recv = new String(readBuffer.array()).trim();
                        // debug
                        System.out.println("The chat message is from: " + chatMsgRecv.getFromPid());
                        System.out.println("To: " + chatMsgRecv.getToPid());
                        System.out.println("Saying: " + chatMsgRecv.getMessage());
                        // System.out.println("Server received: " + recv);
                        HandleChatMsg(clientChannel, chatMsgRecv);
                        key.cancel();
    
                    }
                    else {
                        System.out.println("Wrong type of channel!"); // if received connectable channel
                    }
                }                
                keyIterator.remove(); // remove from sets as long as no data to process
            }
        }
    }

    private void HandleChatMsg(SocketChannel clientChannel, ChatMessage chatMsgRecv) throws IOException {
        String msgString = "received from serverChannel";
        byte [] msg = msgString.getBytes();  
        ByteBuffer writebuffer = ByteBuffer.wrap(msg);  
        clientChannel.write(writebuffer); 
        //debug
        System.out.println("BackMsg Sent");

    }


    // public static void main(String[] args) {
    //     // run the game
    //     ChatServer charServer = new ChatServer();
    //     charServer.run();
    //   }
}
