package com.azurice.polywar.network.packet;

import com.azurice.polywar.server.Room;

import java.io.*;
import java.util.List;

import static com.azurice.polywar.network.packet.Type.ROOM_LIST_PACKET;

;

public class RoomListPacket extends Packet {

    public RoomListPacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public RoomListPacket(byte[] data) {
        super(data);
    }

    @Override
    public Type getType() {
        return ROOM_LIST_PACKET;
    }

    public static RoomListPacket of(List<Room> roomList) {
        return new RoomListPacket(bytesOf(roomList));
    }

    private static byte[] bytesOf(List<Room> roomList) {
        byte[] data;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(roomList.toArray(new Room[0]));
            data = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        LOGGER.info("Bytes of " + roomList + ": " + Arrays.toString(data));
        return data;
    }

    public List<Room> getRoomList() {
        Room[] roomList;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            try {
                roomList = (Room[]) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return List.of(roomList);
    }

    @Override
    public String toString() {
        return getRoomList().toString();
    }
}
