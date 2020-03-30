package server;

import shared.*;

public class GameWorker extends Thread {

    private Game game;
    private Gameserver boss;

    public GameWorker(Game g, Gameserver server) {
        game = g;
        boss = server;
    }

    // run the thread
    public void run() {
        // keep running when game is not over
        while (game.getStage() < GameMessage.GAME_OVER) {
            switch (game.getStage()) {
                case (GameMessage.ERROR):
                    System.out.println("Error: Game " + game.getGid() + " is on error state");
                    break;
                case (GameMessage.WAIT_FOR_PLAYERS): // wait for players
                    // TODO
                    // wait until all players join
                    while (!game.isFull()) {}
                    // set player stats
                    game.setPlayerStats();
                    // change stage to initialize units
                    game.setStage(GameMessage.INITIALIZE_UNITS);
                    break;
                case (GameMessage.INITIALIZE_UNITS): // initialize units
                    // TODO
                    break;
                case (GameMessage.GAME_PLAY): // play game
                    // TODO
                    break;
                default:
                    System.out.println("Game state: " + game.getStage());
            } // switch
            game.notifyAll();  // notify clientworkers
        } // while
        // gameover, gameworker exits
    }

}