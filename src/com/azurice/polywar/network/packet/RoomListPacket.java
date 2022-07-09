package com.azurice.polywar.network.packet;

import com.azurice.polywar.server.Room;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class RoomListPacket extends Packet {

    public RoomListPacket(byte[] packetBytes) {
        super(packetBytes);
    }

    public RoomListPacket(List<Room> roomList) {
        super(PacketType.ROOM_LIST_PACKET, bytesOf(roomList));
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
        return data;
    }

    public List<Room> getRoomList() {
        Room[] roomList;
        try {
            System.out.println(Arrays.toString(getData()));
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

//    @Override
//    public String toString() {
//        return getRoomList().toString();
//    }
}
