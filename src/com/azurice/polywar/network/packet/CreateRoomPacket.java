package com.azurice.polywar.network.packet;

public class CreateRoomPacket extends Packet {
    public CreateRoomPacket() {
        super(PacketType.CREATE_ROOM_PACKET, null);
    }

    @Override
    public String toString() {
        return "CreateRoom";
    }
}
