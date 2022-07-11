package com.azurice.polywar.network.packet;

import com.azurice.polywar.server.Room;

import java.util.List;

import static com.azurice.polywar.network.packet.Type.ROOM_LIST_PACKET;

public class RoomListPacket extends AbstractObjectPacket {

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
        return new RoomListPacket(bytesOf(roomList.toArray(new Room[0])));
    }

    @Override
    public List<Room> getData() {
        return List.of((Room[]) super.getData());
    }
}
