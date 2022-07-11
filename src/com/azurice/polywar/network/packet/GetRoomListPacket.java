package com.azurice.polywar.network.packet;

import static com.azurice.polywar.network.packet.Type.GET_ROOM_LIST_PACKET;

public class GetRoomListPacket extends Packet {
    @Override
    public Type getType() {
        return GET_ROOM_LIST_PACKET;
    }
}
