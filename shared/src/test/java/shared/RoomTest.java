package shared;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class RoomTest {
  @Test
  public void test_Room() {
    Room r101 = new Room(101, 3, false);
    r101.setfull(true);
    assert (r101.getGid() == 101);
    assert (r101.getPlayerNum() == 3);
    assert (r101.isFull() == true);

    RoomMessage m1 = new RoomMessage(false);
    ArrayList<Room> room_list = new ArrayList<Room>();
    
    RoomMessage m2 = new RoomMessage(room_list);
    assert (m2.isValid() == true);
    m2.addRoom(r101);
    m2.getRooms();
    m2.ifIsValidRoom(101);
    m2.ifIsValidRoom(0);
    m2.setRoomList(room_list);
    
  }

}
