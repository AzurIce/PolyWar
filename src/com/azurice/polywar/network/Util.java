package com.azurice.polywar.network;

import com.azurice.polywar.network.packet.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static com.azurice.polywar.network.packet.Packet.BLOCK_LEN;

public class Util {
//    private static final Logger LOGGER = LogManager.getLogger();

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
        while (buf.hasRemaining()) {
            socketChannel.read(buf);
        }
        return buf.array();
    }

    private static byte[] readBlock(SocketChannel socketChannel) throws IOException {
        return readBytes(socketChannel, BLOCK_LEN);
    }

    public static Type readPacketType(SocketChannel socketChannel) throws IOException {
        return Type.values()[readByte(socketChannel)];
    }

    public static Packet readPacket(SocketChannel socketChannel) throws IOException {
//        LOGGER.info("");
//        LOGGER.info("======Read Packet======");
        Type type = readPacketType(socketChannel);
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
//            LOGGER.info("Finished reading Blocks");
        }

//        LOGGER.info("======Read Packet End======");
//        LOGGER.info("");
        return switch (type) {
            case PING_PACKET -> new PingPacket();

            case GET_ROOM_LIST_PACKET -> new GetRoomListPacket();
            case ROOM_LIST_PACKET -> new RoomListPacket(blocks);

            case CREATE_ROOM_PACKET -> new CreateRoomPacket();
            case ROOM_PACKET -> new RoomPacket(blocks);
            case EXIT_ROOM_PACKET -> new ExitRoomPacket();

            case PLAYER_LIST_PACKET -> new PlayerListPacket(blocks);
            case REGENERATE_MAP_PACKET -> new RegenerateMapPacket();
            case MAP_PACKET -> new MapPacket(blocks);

            case START_GAME_PACKET -> new StartGamePacket();
            case GAME_PLAYER_DATA_PACKET -> new GamePlayerDataPacket(blocks);
            case GAME_PLAYER_CONTROL_DATA -> new GamePlayerControlDataPacket(blocks);
            case GAME_PLAYER_DATA_LIST_PACKET -> new GamePlayerDataListPacket(blocks);
            case MISSILE_DATA_LIST_PACKET -> new MissileDataListPacket(blocks);
            case NAME_PACKET -> new NamePacket(blocks);
            case NAME_VALID_PACKET -> new NameValidPacket();
            case NAME_INVALID_PACKET -> new NameInValidPacket();
            case GAME_OVER_PACKET -> new GameOverPacket(blocks);
            case PLAYER_PACKET -> new PlayerPacket(blocks);
            case ROOM_FINISHED_PLAYING_PACKET -> new RoomFinishPlayingPacket();
            case MAP_RADIUS_PACKET -> new MapRadiusPacket(blocks);
            case GET_GAME_OVER_RECORD_PACKET -> new GetGameOverDataListPacket();
            case GAME_OVER_DATA_LIST_PACKET -> new GameOverDataListPacket(blocks);
        };
    }

    public static void sendPacket(SocketChannel socketChannel, Packet packet) throws IOException {
        ByteBuffer buffer = packet.toByteBuffer();
        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }
}
