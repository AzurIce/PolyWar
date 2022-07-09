package com.azurice.polywar.network;

public class GetRoomListPacket extends Packet {
    public GetRoomListPacket() {
        super(PacketType.GET_ROOM_LIST_PACKET, null);
    }

    @Override
    public String toString() {
        return "GetRoomList";
    }
}
