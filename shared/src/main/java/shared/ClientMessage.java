package shared;

/***
 * A class that represent a game message sent from client. Contains an Action
 */
public class ClientMessage extends GameMessage {

    private Action action;

    // gid 2-5 means to start a new game, with player number of gid value
    // gid 0 means switch out
    public ClientMessage(int gid, int s, Action ac){
        gameId = gid;
        stage = s;
        action = ac;
    }

    public Action getAction(){
        return action;
    }
}
