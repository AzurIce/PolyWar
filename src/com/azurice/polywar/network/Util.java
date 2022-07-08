package com.azurice.polywar.network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Util {
    public static Packet getPacket(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        int len = socketChannel.read(buffer);
        if (len == -1) {
//                LOGGER.info("SocketChannel closing...");
            socketChannel.close();
            return null;
        }
        Packet packet = new Packet(buffer.array());

        byte[] bytes = buffer.array();
        return switch (PacketType.values()[bytes[0]]) {
            case PING_PACKET -> new PingPacket();
        };
    }
}
