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
        acceptUser(); // accept user info from client
        while (player.isConnected()) {
            // receive ClientMessage and process
            ClientMessage clientMsg = (ClientMessage) player.recvObject();
            int gid = clientMsg.getGameID();
            if (gid == 0) {  // switch out, send back RoomMessage, no wait
                // debug
                System.out.println("player " + player.getUsername() + " switch out of the game");
                player.setActiveGid(0);  // change active gid to 0
                player.sendObject(new RoomMessage(boss.gatherRooms(player.getUsername())));
            } else { // need to send back ServerMessage after gameworker notify
                Game g = new Game(); // default game object with error state
                // new game
                if (gid >= 2 && gid <= 5) {
                    g = createNewGame(gid);  // here gid is the num of players
                    waitOnGame(g);
                } else if (boss.hasActiveGame(gid)) {  // game exists
                    g = boss.getGame(gid);
                    updateOnGame(g, clientMsg);      
                    waitOnGame(g);
                } else { // game doesn't exist
                    System.out.println("Error: received gid (" + gid + ") doesn't exist");
                }
                // send ServerMessage after gameworker notify
                player.sendObject(new ServerMessage(g.getGid(), g.getStage(), g.getMap()));
            }

        }  // while connected
        // thread exits if player disconnected
    }

    private void updateOnGame(Game g, ClientMessage msg){
        int gid = g.getGid();
        if (player.getActiveGid() == 0) { // switch in or join game
            // join game: add player to game
            if (!g.hasPlayer(player.getUsername()) && g.getStage() == GameMessage.WAIT_FOR_PLAYERS) {
                // debug
                System.out.println("player " + player.getUsername() + " joins game " + gid);
                g.addPlayer(player);
            }
            player.setActiveGid(gid);
            // debug
            System.out.println("player " + player.getUsername() + " sets active gid to " + gid);
        } else {  // player is in game
            if (g.hasPlayer(player.getUsername()) && player.getActiveGid() == gid) { 
                // normal game play, has action
                // TODO: let gameworker validate actions
                int pid = g.getPidByName(player.getUsername());
                g.addTempAction(pid, msg.getAction());
                // debug
                System.out.println("player " + player.getUsername() + " sends action to game " + gid);
            } else {
                System.out.println("Error: received game (" + gid + ") doesn't have player "
                        + player.getUsername() + ", or player has wrong activeGid");
            }
        }
    }

    // tell server to create a new game and start a gameworker
    private Game createNewGame(int playerNum) {
        // debug
        System.out.println("player " + player.getUsername() + " starts a new game");
        Game g = boss.startNewGame(playerNum, player); // new game
        player.setActiveGid(g.getGid()); // set active gid to player
        GameWorker gWorker = new GameWorker(g, boss); // start game worker
        // TODO: implement gameworker.run()
        gWorker.start();
        return g;
    }

    private void waitOnGame(Game g) {
        try {
            g.wait(); // wait for gameworker to notify
        } catch (InterruptedException e) {
            System.out.println("InterruptedException while wait()");
            e.printStackTrace();
        }
    }

    // accept a just connected user, validate login/register and modify the userlist
    // in server
    private void acceptUser() {
        // debug
        System.out.println("ClientWorker accepts a user");
        player = new Player(socket);
        player.setUpInputStream(); // ready to receive from client
        boolean success = false;
        while (!success) {
            UserMessage userMsg = (UserMessage) player.recvObject(); // recv UserMessage
            String name = userMsg.getUsername();
            String password = userMsg.getPassword();
            RoomMessage msg = new RoomMessage(false); // default to false (not succeed)
            if (userMsg.isLogin()) { // log in
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
                    msg = new RoomMessage(true); // empty room list for new user
                    success = true;
                }
            } // login or register
            player.sendObject(msg);
            if (!player.isConnected()) {
                return;
            }
        } // while
        // debug
        System.out.println("player " + player.getUsername() + " successfully logged in");
    }

}