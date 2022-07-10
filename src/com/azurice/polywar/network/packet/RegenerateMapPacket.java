package com.azurice.polywar.network.packet;

public class RegenerateMapPacket extends Packet {
    public RegenerateMapPacket() {
        super(Type.REGENERATE_MAP_PACKET);
    }

    @Override
    public String toString() {
        return "RegenerateMapPacket";
    }
}
