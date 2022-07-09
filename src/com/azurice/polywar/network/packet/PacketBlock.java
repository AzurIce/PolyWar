package com.azurice.polywar.network.packet;

import java.util.Arrays;

import static com.azurice.polywar.network.packet.Packet.BLOCK_LEN;

public class PacketBlock {
    private final byte[] bytes;

    public PacketBlock(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte[] getBytes() {
        return Arrays.copyOfRange(bytes, 0, BLOCK_LEN);
    }
}
