package server;

import shared.*;

public class GameWorker extends Thread{

    private Game game;
    private Gameserver boss;

    public GameWorker(Game g, Gameserver server){
        game = g;
        boss = server;
    }

    // run the thread
    public void run(){
        // keep running when game is not over
        while (game.getStage() < GameMessage.GAME_OVER){
            // wait for players

            // initialize units

            // play game

        }
        // gameover
    }

}