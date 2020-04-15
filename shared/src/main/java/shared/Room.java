package shared;
import java.io.Serializable;

/***
 * A Room class that contains gameID, player number, and whether the room is full
 */
public class Room implements Serializable {

    private int gid;
    private int playerNum;
    private boolean full;

    public Room(int g_id, int player_num, boolean is_full){
        gid = g_id;
        playerNum = player_num;
        full = is_full;
    }

    public void setfull(boolean bool){
        full = bool;
    }

    public int getGid(){
        return gid;
    }

    public int getPlayerNum(){
        return playerNum;
    }

    public boolean isFull(){
        return full;
    }

}