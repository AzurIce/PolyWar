package com.azurice.polywar.network.packet;

import static com.azurice.polywar.network.packet.Type.START_GAME_PACKET;

public class StartGamePacket extends Packet {
    @Override
    public Type getType() {
        return START_GAME_PACKET;
    }
}