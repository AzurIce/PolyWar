package com.azurice.polywar.network.packet;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Packet has a total length less or equals to 256 Bytes
 */
public abstract class Packet {
    public static final int BLOCK_LEN = 128;
//    private static final Logger LOGGER = LogManager.getLogger(Packet.class);
    private List<PacketBlock> blocks = new ArrayList<>();

    public Packet() {
    }

    public Packet(List<PacketBlock> blocks) {
        this.blocks = blocks;
    }

    public Packet(byte[] dataBytes) {
//        LOGGER.info("");
//        LOGGER.info("======Constructor======");
        for (int i = 0; i * (BLOCK_LEN - 1) < dataBytes.length; i++) {
            byte[] dataBlock = Arrays.copyOfRange(dataBytes, i * (BLOCK_LEN - 1), (i + 1) * (BLOCK_LEN - 1));
//            LOGGER.info("DataBlock from {} to {}: {}", i * (BLOCK_LEN - 1), (i + 1) * BLOCK_LEN - 1, dataBlock);
            blocks.add(new PacketBlock(dataBlock, (i + 1) * (BLOCK_LEN - 1) > dataBytes.length));
        }
//        LOGGER.info("======Constructor End======");
//        LOGGER.info("");
    }

    public byte[] getDataBytes() {
//        LOGGER.info("");
//        LOGGER.info("======Get Data======");
//        LOGGER.info("Type: {}, Blocks number: {}", getTypeString(), blocks.size());
        byte[] data = new byte[blocks.size() * (BLOCK_LEN - 1)];
        for (int i = 0; i < blocks.size(); i++) {
            System.arraycopy(blocks.get(i).getBytes(), 0, data, i * (BLOCK_LEN - 1), BLOCK_LEN - 1);
        }
//        LOGGER.info("Blocks: {}", blocks);
//        LOGGER.info("Data: {}", data);
//        LOGGER.info("======GetData End======");
//        LOGGER.info("");
        return data;
    }

    public Object getData() {
        return getDataBytes();
    }

    public abstract Type getType();

    public String getTypeString() {
        return getType().toString();
    }

    public ByteBuffer toByteBuffer() {
        ByteBuffer buf = ByteBuffer.allocate(1 + blocks.size() * BLOCK_LEN);
        buf.put((byte) getType().ordinal());
        for (PacketBlock block : blocks) {
            buf.put(block.getBytes());
        }
        buf.flip();
        return buf;
    }

    @Override
    public String toString() {
        return getTypeString();
    }
}
