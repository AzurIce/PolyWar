package com.azurice.polywar.network.packet;

public class NameValidPacket extends Packet {
    @Override
    public Type getType() {
        return Type.NAME_VALID_PACKET;
    }
}
