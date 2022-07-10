package com.azurice.polywar.network.packet;

public class StartGamePacket extends Packet {
    public StartGamePacket() {
        super(Type.START_GAME_PACKET);
    }

    @Override
    public String toString() {
        return "StartGamePacket";
    }
}