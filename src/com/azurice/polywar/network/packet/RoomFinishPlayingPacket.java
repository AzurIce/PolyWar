package com.azurice.polywar.network.packet;

import static com.azurice.polywar.network.packet.Type.ROOM_FINISHED_PLAYING_PACKET;

public class RoomFinishPlayingPacket extends Packet {
    @Override
    public Type getType() {
        return ROOM_FINISHED_PLAYING_PACKET;
    }
}
