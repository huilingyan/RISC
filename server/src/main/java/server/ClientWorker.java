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
        while (player.isConnected()){
            // receive ClientMessage and process
        }
    }

    // accept a just connected user, validate login/register and modify the userlist in server
    private void acceptUser(){
        player = new Player(socket);
        player.setUpInputStream();   // ready to receive from client
        boolean success = false;
        while (!success) {
            UserMessage userMsg = (UserMessage) player.recvObject();  // recv UserMessage
            String name = userMsg.getUsername();
            String password = userMsg.getPassword();
            RoomMessage msg = new RoomMessage(false); // default to false (not succeed)
            if (userMsg.isLogin()) {   // log in
                // validate login info
                if (boss.isValidUser(name, password)) { 
                    // find available rooms and update msg
                    msg = new RoomMessage(boss.gatherRooms(name));
                    // update the old player
                    player = boss.updateUser(name, player);
                    success = true;
                }   
            } else {
                // register
                if (!boss.hasUser(name)) { 
                    // successfully registered
                    player.setUpUserInfo(name, password);
                    boss.addUser(player); // add to list, synchronized
                    // success message
                    msg = new RoomMessage(true);  // empty room list for new user
                    success = true;
                }
            } // login or register
            player.sendObject(msg);
            if (!player.isConnected()){
                return;
            }
        } // while
    }

}