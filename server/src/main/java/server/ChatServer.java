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
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.Iterator;
import java.lang.String;
import java.lang.Integer;
import java.util.HashMap; 
import java.util.Map;
import java.net.Socket;
import java.math.BigInteger;
import shared.*;

/****
 * A class that implements the chat between players(server side)
 ***/
public class ChatServer extends Thread {

    // private static final int CHAT_PORT = 6666;

    private ServerSocketChannel ssc = null;
    private Selector selector = null;
    private Gameserver boss;
    private HashMap<String, SocketChannel> socketMap = new HashMap<> ();

    // constructor
    public ChatServer(Gameserver gServer) {
        this.boss = gServer;
    }
 
    @Override
    public void run() {
        try {
            this.init();
            this.process();

        } catch (IOException e) {    
            e.printStackTrace();
        } catch (InterruptedException e) {    
            e.printStackTrace();
        } 
        finally {
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

    public void init() throws IOException, InterruptedException {  

        this.ssc = ServerSocketChannel.open(); // open channel
        Config config = new Config("config.properties");
        String chat_port = config.readProperty("chat_port");
        this.ssc.socket().bind(new InetSocketAddress(Integer.parseInt(chat_port))); // bind to socket
        this.ssc.configureBlocking(false); // set non-blocking mode

        this.selector = Selector.open(); // open selector
        this.ssc.register(selector, SelectionKey.OP_ACCEPT); // register channel with the selector
    }

    public void process() throws IOException, InterruptedException {   

        while (true) {           
            int nRead = this.selector.select();
            // if (nRead == 0) {
            //     continue;
            // }       
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
                        sleep(100);
                        ByteBuffer readBuffer = ByteBuffer.allocate(256);
                        int readBytes = clientChannel.read(readBuffer);
                        readBuffer.clear();
                        //debug
                        System.out.println("Accepted a connection!");
    
                    } else if (key.isReadable()) {
                        System.out.println("Enter readable if clause");
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
                        // debug
                        String str = new String(readBuffer.array(), StandardCharsets.UTF_8);
                        String newStr = String.format("%040x", new BigInteger(1, str.getBytes(StandardCharsets.UTF_8)));
                        System.out.println(newStr);
                        System.out.println(new String(readBuffer.array(), StandardCharsets.UTF_8));


                        // String recv = new String(readBuffer.array()).trim();
                        // String recv = new String(readBuffer.array(), StandardCharsets.UTF_8).trim();
                        // System.out.println("RECEIVED: " + recv);
                        ChatMessage chatMsgRecv = (ChatMessage)SerializationUtils.deserialize(readBuffer.array());
                        // debug
                        System.out.println("The chat message is from: " + chatMsgRecv.getSrcPlayerName());
                        System.out.println("To: " + chatMsgRecv.getDestPlayerName());
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

    /***
     * handle the chat messages among players
     */
    private void HandleChatMsg(SocketChannel clientChannel, ChatMessage chatMsgRecv) throws IOException {
        // String msgString = "user message stored";
        // byte [] msg = msgString.getBytes();  
        // ByteBuffer writebuffer = ByteBuffer.wrap(msg);  
        // clientChannel.write(writebuffer); 
        // //debug
        // System.out.println("Confirm message sent");

        String destPlayerName = chatMsgRecv.getDestPlayerName();
        if (destPlayerName.isEmpty()) { // init message from players
            this.socketMap.put(chatMsgRecv.getSrcPlayerName(), clientChannel);
            // send confirm msg back
            sendConfirmMsg(clientChannel, chatMsgRecv.getSrcPlayerName());
            
        }
        else { // process normal chat msg from players
            SocketChannel playerToSend = findChannelByPlayername(destPlayerName);
            if (playerToSend == null) { // if no such player
                noSuchPlayer(clientChannel, chatMsgRecv.getSrcPlayerName());

            }
            else {
                if (isActiveGame(chatMsgRecv)) { // if dest player is in the same game
                    ByteBuffer writeBuffer = ByteBuffer.wrap(SerializationUtils.serialize(chatMsgRecv));
                    playerToSend.write(writeBuffer);
                    writeBuffer.clear();
                }
                else { // if dest player is currently out of the game
                    inactiveGame(clientChannel, chatMsgRecv.getSrcPlayerName());
                }
            }
        }
    }

    /*********** helper functions ************/

    private SocketChannel findChannelByPlayername(String playerName) {
        return this.socketMap.get(playerName);
    }

    private int getActiveGidByUserName(String playerName) {
        return this.boss.getActiveGidByName(playerName);
    }

    private boolean isActiveGame(ChatMessage chatMsg) {
        int srcActiveGid = this.getActiveGidByUserName(chatMsg.getSrcPlayerName());
        int destActiveGid = this.getActiveGidByUserName(chatMsg.getDestPlayerName());
        if (srcActiveGid == destActiveGid) {
            return true;
        }
        return false;
    }

    /*
      Confirm msg sent back to src player
    */
    private void sendConfirmMsg(SocketChannel clientChannel, String srcPlayerName) throws IOException {
        ChatMessage confirmMsg = new ChatMessage("ChatServer", srcPlayerName, "User message received");
        ByteBuffer writeBuffer = ByteBuffer.wrap(SerializationUtils.serialize(confirmMsg));
        clientChannel.write(writeBuffer);
        writeBuffer.clear();        
    }
    /*
      Error msg sent back to src player that the dest player does not exist
    */
    private void noSuchPlayer(SocketChannel clientChannel, String srcPlayerName) throws IOException {
        ChatMessage noSuchPlayerMsg = new ChatMessage("ChatServer", srcPlayerName, "No such player exists!");
        ByteBuffer writeBuffer = ByteBuffer.wrap(SerializationUtils.serialize(noSuchPlayerMsg));
        clientChannel.write(writeBuffer);
        writeBuffer.clear();
    }

    /*
      Warning msg sent back to src player that the dest player is offline
    */
    private void inactiveGame(SocketChannel clientChannel, String srcPlayerName) throws IOException {
        ChatMessage outOfGameMsg = new ChatMessage("ChatServer", srcPlayerName, "The player you're looking for is currently offline");
        ByteBuffer writeBuffer = ByteBuffer.wrap(SerializationUtils.serialize(outOfGameMsg));
        clientChannel.write(writeBuffer);
        writeBuffer.clear();
    }

    // public static void main(String[] args) {
    //     // run the game
    //     ChatServer charServer = new ChatServer();
    //     charServer.run();
    //   }
}
