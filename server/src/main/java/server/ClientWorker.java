package server;

import shared.*;
import java.net.Socket;

public class ClientWorker extends Thread {

    private Socket socket;
    private Player player;
    private Gameserver boss;

    public ClientWorker(Socket s, Gameserver server) {
        socket = s;
        boss = server;
    }

    // run the thread
    public void run() {
        acceptUser();  // accept user info from client
    }

    private void acceptUser(){
        player = new Player(socket);
        player.setUpInputStream();
        boolean success = false;
        while (!success) {
            UserMessage userMsg = (UserMessage) player.recvObject();
            String name = userMsg.getUsername();
            String password = userMsg.getPassword();
            RoomMessage msg = new RoomMessage(false); // default to false (not succeed)
            if (userMsg.isLogin()) {   // log in
                // validate login info
                if (boss.isValidUser(name, password)) { 
                    // find available rooms
                    msg = new RoomMessage(boss.gatherRooms(name));
                    success = true;
                }
            } else {
                // register
                if (!boss.hasUser(name)) { 
                    // successfully registered
                    player.setUpUserInfo(name, password);
                    // add user info to list
                    boss.addUser(player); // synchronized
                    // send success message
                    msg = new RoomMessage(true);
                    success = true;
                }
            } // login or register
            player.sendObject(msg);
        } // while
    }

}