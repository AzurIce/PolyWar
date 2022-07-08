package com.azurice.polywar.network;

import static com.azurice.polywar.network.PacketType.PING_PACKET;

public class PingPacket extends Packet {
    public PingPacket() {
        super(PING_PACKET, null);
    }
}
