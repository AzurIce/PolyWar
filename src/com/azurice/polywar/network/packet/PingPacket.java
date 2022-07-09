package com.azurice.polywar.network.packet;


import static com.azurice.polywar.network.packet.Packet.Type.PING_PACKET;

public class PingPacket extends Packet {
    public PingPacket() {
        super(PING_PACKET);
    }

    @Override
    public String toString() {
        return "Ping";
    }
}
