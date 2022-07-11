package com.azurice.polywar.network.packet;

import static com.azurice.polywar.network.packet.Type.EXIT_ROOM_PACKET;

public class ExitRoomPacket extends Packet {
    @Override
    public Type getType() {
        return EXIT_ROOM_PACKET;
    }
}
