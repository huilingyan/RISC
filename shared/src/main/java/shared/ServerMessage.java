package shared;

/***
 * A class that represent a game message sent from server. Contains a game map
 */
public class ServerMessage extends GameMessage {

    private Map gameMap;

    public ServerMessage(int gid, int s, Map map){
        gameId = gid;
        stage = s;
        gameMap = map;
    }

    public Map getMap(){
        return gameMap;
    }
}