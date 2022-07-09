package com.azurice.polywar.network.packet;

import static com.azurice.polywar.network.packet.Packet.Type.CREATE_ROOM_PACKET;

public class CreateRoomPacket extends Packet {
    public CreateRoomPacket() {
        super(CREATE_ROOM_PACKET);
    }

    @Override
    public String toString() {
        return "CreateRoom";
    }
}
