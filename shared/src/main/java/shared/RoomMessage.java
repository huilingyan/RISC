package shared;

import java.io.Serializable;
import java.util.ArrayList;

/***
 * A RoomMessage class that contains an arraylist of room objects, and if the
 * user login/register is valid
 */
public class RoomMessage implements Serializable {

    private ArrayList<Room> roomList;
    private boolean valid;

    // initialize the RoomMessage with an empty room list, and the specified user
    // validation result
    public RoomMessage(boolean isValid) {
        roomList = new ArrayList<Room>();
        valid = isValid;
    }

    // initialize the RoomMessage with a list, default user login/register is valid
    public RoomMessage(ArrayList<Room> room_list) {
        roomList = room_list;
        valid = true;
    }

    public boolean isValid() {
        return valid;
    }

    public void addRoom(Room r) {
        roomList.add(r);
    }

    public ArrayList<Room> getRooms() {
        return roomList;
    }

    public void setRoomList(ArrayList<Room> rooms) {
        roomList = rooms;
    }

    public boolean ifIsValidRoom(int gid) {
        for (Room room : this.getRooms()) {
            if (room.getGid() == gid) { // if contains
                return true;
            }
        }
        return false;
    }

}