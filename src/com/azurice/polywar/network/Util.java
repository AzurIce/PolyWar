package com.azurice.polywar.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Util {
    public static Packet getPacket(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        if (socketChannel.read(buffer) == -1) {
            return null;
        }

        byte[] bytes = buffer.array();
        return switch (PacketType.values()[bytes[0]]) {
            case PING_PACKET -> new PingPacket();
            case GET_ROOM_LIST_PACKET -> new GetRoomListPacket();
            case ROOM_LIST_PACKET -> new RoomListPacket(bytes);
        };
    }

    public static void sendPacket(SocketChannel socketChannel, Packet packet) throws IOException {
        ByteBuffer buffer = packet.toByteBuffer();
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }
}
