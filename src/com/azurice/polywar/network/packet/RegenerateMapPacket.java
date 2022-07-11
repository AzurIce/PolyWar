package com.azurice.polywar.network.packet;

import static com.azurice.polywar.network.packet.Type.REGENERATE_MAP_PACKET;

public class RegenerateMapPacket extends Packet {
    @Override
    public Type getType() {
        return REGENERATE_MAP_PACKET;
    }

}
