package com.azurice.polywar.network.packet;

import java.util.Arrays;

import static com.azurice.polywar.network.packet.Packet.BLOCK_LEN;

public class PacketBlock {
    private final byte[] bytes;

    public PacketBlock(byte[] bytes) {
        this.bytes = Arrays.copyOfRange(bytes, 0, BLOCK_LEN);
    }

    public PacketBlock(byte[] bytes, boolean isFinished) {
        this.bytes = Arrays.copyOfRange(bytes, 0, BLOCK_LEN);
        this.bytes[BLOCK_LEN - 1] = (byte) (isFinished ? 0 : 1);
    }

    public byte[] getBytes() {
        return Arrays.copyOfRange(bytes, 0, BLOCK_LEN);
    }
}
