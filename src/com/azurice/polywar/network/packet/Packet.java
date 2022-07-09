package com.azurice.polywar.network.packet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Packet has a total length less or equals to 256 Bytes
 */
public class Packet {
    public static final int BLOCK_LEN = 128;
    private static final Logger LOGGER = LogManager.getLogger();
    private final Type type;
    private List<PacketBlock> blocks = new ArrayList<>();

    public Packet(Type type) {
        this.type = type;
    }

    public Packet(Type type, List<PacketBlock> blocks) {
        this.type = type;
        this.blocks = blocks;
    }

    public Packet(Type type, byte[] data) {
        this.type = type;
        for (int i = 0; i * (BLOCK_LEN - 1) < data.length; i++) {
            blocks.add(new PacketBlock(Arrays.copyOfRange(data, i * (BLOCK_LEN - 1), (i + 1) * BLOCK_LEN)));
        }
    }

    public byte[] getData() {
        LOGGER.info("Type: {}, Blocks number: {}", getTypeString(), blocks.size());
        byte[] data = new byte[blocks.size() * (BLOCK_LEN - 1)];
        for (int i = 0; i < blocks.size(); i++) {
            System.arraycopy(blocks.get(i).getBytes(), 0, data, i * (BLOCK_LEN - 1), BLOCK_LEN - 1);
        }
        LOGGER.info("Blocks: {}", blocks);
        LOGGER.info("Data: {}", data);
        return data;
    }

    public Type getType() {
        return type;
    }

    public String getTypeString() {
        return type.toString();
    }

    public ByteBuffer toByteBuffer() {
        ByteBuffer buf = ByteBuffer.allocate(1 + blocks.size() * BLOCK_LEN);
        buf.put((byte) type.ordinal());
        for (PacketBlock block : blocks) {
            buf.put(block.getBytes());
        }
        buf.flip();
        return buf;
    }

    @Override
    public String toString() {
        return Arrays.toString(getData());
    }

    public enum Type {
        PING_PACKET(false),
        ROOM_LIST_PACKET(true), JOIN_ROOM_PACKET(false), EXIT_ROOM_PACKET(false),
        GET_ROOM_LIST_PACKET(false),
        CREATE_ROOM_PACKET(false);

        public final boolean hasContent;

        Type(boolean hasContent) {
            this.hasContent = hasContent;
        }
    }
}
