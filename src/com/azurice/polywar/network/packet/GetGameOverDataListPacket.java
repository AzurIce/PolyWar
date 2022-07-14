package com.azurice.polywar.network.packet;

public class GetGameOverDataListPacket extends Packet {
    @Override
    public Type getType() {
        return Type.GET_GAME_OVER_RECORD_PACKET;
    }
}
