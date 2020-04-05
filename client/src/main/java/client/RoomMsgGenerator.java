package client;

import shared.*;

public class RoomMsgGenerator {
    public static RoomMessage generateRooms() {
        RoomMessage roomMsg = new RoomMessage(true);
        roomMsg.addRoom(new Room(100, 2, false));
        roomMsg.addRoom(new Room(101, 5, true));
        roomMsg.addRoom(new Room(102, 3, false));
        roomMsg.addRoom(new Room(103, 2, true));
        roomMsg.addRoom(new Room(104, 4, false));

        return roomMsg;
    }
}