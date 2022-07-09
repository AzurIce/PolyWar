package com.azurice.polywar.network.packet;

import static com.azurice.polywar.network.packet.Packet.Type.GET_ROOM_LIST_PACKET;

public class GetRoomListPacket extends Packet {
    public GetRoomListPacket() {
        super(GET_ROOM_LIST_PACKET);
    }

    @Override
    public String toString() {
        return "GetRoomList";
    }
}
