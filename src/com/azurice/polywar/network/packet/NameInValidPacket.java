package com.azurice.polywar.network.packet;

public class NameInValidPacket extends Packet {
    @Override
    public Type getType() {
        return Type.NAME_INVALID_PACKET;
    }
}
