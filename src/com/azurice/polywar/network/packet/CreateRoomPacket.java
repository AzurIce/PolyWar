package com.azurice.polywar.network.packet;

import static com.azurice.polywar.network.packet.Type.CREATE_ROOM_PACKET;

public class CreateRoomPacket extends Packet {
    @Override
    public Type getType() {
        return CREATE_ROOM_PACKET;
    }
}
