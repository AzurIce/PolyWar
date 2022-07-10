package com.azurice.polywar.network.packet;

public class ExitRoomPacket extends Packet {
    public ExitRoomPacket() {
        super(Type.EXIT_ROOM_PACKET);
    }

    @Override
    public String toString() {
        return "ExitRoomPacket";
    }
}
