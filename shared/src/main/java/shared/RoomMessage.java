package shared;
import java.io.Serializable;
import java.util.ArrayList;

/***
 * A RoomMessage class that contains an arraylist of room objects
 */
public class RoomMessage implements Serializable {

    private ArrayList<Room> roomList;

    public RoomMessage(){
        roomList = new ArrayList<Room>();
    }

    public void addRoom(Room r){
        roomList.add(r);
    }

    public ArrayList<Room> getRooms(){
        return roomList;
    }

    public void setRoomList(ArrayList<Room> rooms){
        roomList = rooms;
    }

}