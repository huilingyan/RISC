package shared;

import java.io.Serializable;

/***
 * An abstract class that represents GameMessage that contains gameID and stage
 * info
 */
public abstract class GameMessage implements Serializable {

    protected int gameId; // when start a new game, ClientMessage stores num of players in gid(2-5)
    protected int stage;  // game stage

    // stages
    public static final int ERROR = -1;
    public static final int WAIT_FOR_PLAYERS = 0;
    public static final int INITIALIZE_UNITS = 1;
    public static final int GAME_PLAY = 2;
    public static final int GAME_OVER = 3;

    public int getGameID() {
        return gameId;
    }

    public int getStage() {
        return stage;
    }

}