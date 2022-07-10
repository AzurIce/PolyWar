package com.azurice.polywar.network.packet;

import com.azurice.polywar.server.Room;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.List;

public class RoomPacket extends Packet {
    private static final Logger LOGGER = LogManager.getLogger();

    public RoomPacket(List<PacketBlock> blocks) {
        super(Packet.Type.ROOM_PACKET, blocks);
    }

    public RoomPacket(byte[] data) {
        super(Packet.Type.ROOM_PACKET, data);
    }

    public static RoomPacket of(Room room) {
        return new RoomPacket(bytesOf(room));
    }

    private static byte[] bytesOf(Room room) {
        byte[] data;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(room);
            data = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        LOGGER.info("Bytes of " + room + ": " + Arrays.toString(data));
        return data;
    }

    public Room getRoom() {
        Room room;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            try {
                room = (Room) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return room;
    }

    @Override
    public String toString() {
        return getRoom().toString();
    }
}
