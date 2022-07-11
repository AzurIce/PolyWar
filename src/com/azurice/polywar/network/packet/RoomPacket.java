package com.azurice.polywar.network.packet;

import com.azurice.polywar.server.Room;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.azurice.polywar.network.packet.Type.ROOM_PACKET;

public class RoomPacket extends AbstractObjectPacket {
    private static final Logger LOGGER = LogManager.getLogger();

    public RoomPacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public RoomPacket(byte[] data) {
        super(data);
    }

    @Override
    public Type getType() {
        return ROOM_PACKET;
    }

    public static RoomPacket of(Room room) {
        return new RoomPacket(bytesOf(room));
    }

    public Room getRoom() {
        return (Room) getObject();
    }

//    @Override
//    public String toString() {
//        return getRoom().toString();
//    }
}
