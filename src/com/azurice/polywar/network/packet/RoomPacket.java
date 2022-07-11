package com.azurice.polywar.network.packet;

import com.azurice.polywar.server.Room;

import java.util.List;

import static com.azurice.polywar.network.packet.Type.ROOM_PACKET;

public class RoomPacket extends AbstractObjectPacket {

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

    @Override
    public Room getData() {
        return (Room) super.getData();
    }
}
