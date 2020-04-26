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
        player = null; // initialize to null
    }

    public Player getPlayer() {
        return player;
    }

    public Socket getSocket() {
        return socket;
    }

    // run the thread
    public void run() {
        acceptUser(); // accept user info from client
        while (player.isConnected()) {
            // receive ClientMessage and process
            ClientMessage clientMsg = (ClientMessage) player.recvObject();
            if (clientMsg == null) {
                break;
            }
            int gid = clientMsg.getGameID();
            if (gid == 0) { // switch out, send back RoomMessage, no wait
                // debug
                System.out.println("player " + player.getUsername() + " switch out of the game");
                player.setActiveGid(0); // change active gid to 0
                player.sendObject(new RoomMessage(boss.gatherRooms(player.getUsername())));
            } else { // need to send back ServerMessage after gameworker notify
                Game g = new Game(); // default game object with error state
                // new game
                if (gid >= 2 && gid <= 5) {
                    g = createNewGame(gid); // here gid is the num of players
                    synchronized (g) {
                        waitOnGame(g);
                    }
                    // debug
                    // System.out.println("Finish waiting on gameworker");
                } else if (boss.hasActiveGame(gid)) { // game exists
                    g = boss.getGame(gid);
                    synchronized (g) {
                        updateOnGame(g, clientMsg);
                        waitOnGame(g);
                    }
                } else { // game doesn't exist
                    System.out.println("Error: received gid (" + gid + ") doesn't exist");
                }
                // send ServerMessage after gameworker notify
                // debug
                System.out.println("Notified by game worker after one turn");
                player.sendObject(new ServerMessage(g.getGid(), g.getStage(), g.getMap()));
            }

        } // while connected
          // thread exits if player disconnected
        System.out.println("Player " + player.getUsername() + " is disconnected");
        player.setLoggedin(false); // login set to false
        player.closeSocket();
    }

    private void updateOnGame(Game g, ClientMessage msg) {
        int gid = g.getGid();
        if (player.getActiveGid() == 0) { // switch in or join game
            // join game: add player to game
            if (!g.hasPlayer(player.getUsername()) && g.getStage() == GameMessage.WAIT_FOR_PLAYERS) {
                // debug
                System.out.println("player " + player.getUsername() + " joins game " + gid);
                g.addPlayer(player.getUsername());
            } else { // switch in: should add temp action to game, otherwise it stucks
                System.out.println("player " + player.getUsername() + " switch in game " + gid);
                int pid = g.getPidByName(player.getUsername());
                g.addTempAction(pid, new Action());
            }
            player.setActiveGid(gid);
            // debug
            System.out.println("player " + player.getUsername() + " sets active gid to " + gid);
        } else { // player is in game
            if (g.hasPlayer(player.getUsername()) && player.getActiveGid() == gid) {
                // normal game play, has action
                int pid = g.getPidByName(player.getUsername());
                g.addTempAction(pid, msg.getAction());
                // debug
                System.out.println("player " + player.getUsername() + " sends action to game " + gid);
            } else {
                System.out.println("Error: received game (" + gid + ") doesn't have player " + player.getUsername()
                        + ", or player has wrong activeGid");
            }
        }

    }

    // tell server to create a new game and start a gameworker
    private Game createNewGame(int playerNum) {
        // debug
        System.out.println("player " + player.getUsername() + " starts a new game");
        Game g = boss.startNewGame(playerNum, player.getUserInfo()); // new game
        player.setActiveGid(g.getGid()); // set active gid to player
        GameWorker gWorker = new GameWorker(g, boss); // start game worker
        gWorker.start();
        return g;
    }

    private void waitOnGame(Game g) {
        try {
            System.out.println("Player " + player.getUsername() + " wait on game " + g.getGid());
            // synchronized (g) {
            g.wait(); // wait for gameworker to notify
            // }
        } catch (InterruptedException e) {
            System.out.println("InterruptedException while wait()");
            e.printStackTrace();
        }
    }

    // validate login/register and modify the userlist in server
    private void userLogin() {
        boolean success = false;
        while (!success) {
            UserMessage userMsg = (UserMessage) player.recvObject(); // recv UserMessage
            if (userMsg == null) {
                return; // jump out of loop
            }
            String name = userMsg.getUsername();
            String password = userMsg.getPassword();
            // debug
            // System.out.println("Received username: " + name);
            // System.out.println("Received password: " + password);
            // System.out.println("Login: " + userMsg.isLogin());
            RoomMessage msg = new RoomMessage(false); // default to false (not succeed)
            if (userMsg.isLogin()) { // log in
                // validate login info
                // username exists, password matches, and the user is not logged in
                if (boss.isValidUser(name, password)) {
                    // find available rooms and update msg
                    msg = new RoomMessage(boss.gatherRooms(name));
                    // update the old player's socket and set as player field
                    player = boss.updateSocketForUser(name, player);
                    player.setLoggedin(true); // successfully logged in
                    success = true;
                }
            } else {
                // register
                if (!boss.hasUser(name)) {
                    // successfully registered
                    player.setUpUserInfo(name, password);
                    // save userinfo to database
                    HibernateUtil.addUserInfo(player.getUserInfo());
                    boss.addUserCopyToList(player); // add to list, synchronized, player is copied and set disconnected and not
                                          // logged in
                    // success message
                    msg = new RoomMessage(true); // empty room list for new user
                    // success = true; // success is still false to let the loop run
                }
            } // login or register
              // debug
            System.out.println("Send room message to player " + player.getUsername());
            System.out.println("isValid: " + msg.isValid());
            player.sendObject(msg);
            if (!player.isConnected()) {
                return;
            }
        } // while
          // debug
        System.out.println("loggedin is " + player.isLoggedin());
        System.out.println("player " + player.getUsername() + " successfully logged in");
    }

    // accept a just connected user, validate login/register and modify the userlist
    // in server
    private void acceptUser() {
        // debug
        System.out.println("ClientWorker accepts a user");
        player = new Player(socket);
        player.setUpInputStream(); // ready to receive from client
        userLogin();

    }

}