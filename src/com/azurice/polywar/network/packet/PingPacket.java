package com.azurice.polywar.network.packet;


import static com.azurice.polywar.network.packet.Type.PING_PACKET;

public class PingPacket extends Packet {
    @Override
    public Type getType() {
        return PING_PACKET;
    }
}
