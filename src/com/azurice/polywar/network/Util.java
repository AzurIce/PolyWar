package com.azurice.polywar.network;

import com.azurice.polywar.network.packet.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.azurice.polywar.network.packet.Packet.BLOCK_LEN;

public class Util {
    private static final Logger LOGGER = LogManager.getLogger();

    private static byte readByte(SocketChannel socketChannel) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1);
        if (socketChannel.read(buf) == -1) {
            throw new IOException("Remote Closed");
        }
        return buf.array()[0];
    }

    private static byte[] readBytes(SocketChannel socketChannel, int len) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(len);
        if (socketChannel.read(buf) == -1) {
            throw new IOException("Remote Closed");
        }
        return buf.array();
    }

    private static byte[] readBlock(SocketChannel socketChannel) throws IOException {
        return readBytes(socketChannel, BLOCK_LEN);
    }

    public static Packet.Type readPacketType(SocketChannel socketChannel) throws IOException {
        return Packet.Type.values()[readByte(socketChannel)];
    }

    public static Packet readPacket(SocketChannel socketChannel) throws IOException {
//        LOGGER.info("Reading PacketType...");
        Packet.Type type = readPacketType(socketChannel);
//        LOGGER.info("Type: {}", type.toString());

        List<PacketBlock> blocks = new ArrayList<>();
        if (type.hasContent) {
//            LOGGER.info("Reading Block...");
            byte[] bytes = readBlock(socketChannel);
            blocks.add(new PacketBlock(bytes));
//            LOGGER.info("Block bytes: {}", bytes);
            while (bytes[BLOCK_LEN - 1] != 0) {
//                LOGGER.info("Reading Block...");
                bytes = readBlock(socketChannel);
//                LOGGER.info("Block bytes: {}", bytes);
                blocks.add(new PacketBlock(bytes));
            }
//            LOGGER.info("Blocks End");
        }

        return switch (type) {
            case PING_PACKET -> new PingPacket();

            case GET_ROOM_LIST_PACKET -> new GetRoomListPacket();
            case ROOM_LIST_PACKET -> new RoomListPacket(blocks);

            case CREATE_ROOM_PACKET -> new CreateRoomPacket();
            case JOIN_ROOM_PACKET -> null;
            case EXIT_ROOM_PACKET -> null;
        };


    }

    public static void sendPacket(SocketChannel socketChannel, Packet packet) throws IOException {
        ByteBuffer buffer = packet.toByteBuffer();
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }
}
