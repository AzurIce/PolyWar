package com.azurice.polywar.network.packet;

public class MissileSoundPacket extends Packet {
    @Override
    public Type getType() {
        return Type.MISSILE_SOUND_PACKET;
    }
}
