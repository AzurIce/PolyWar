package com.azurice.polywar.network.packet;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * A Packet has a total length less or equals to 256 Bytes
 */
public class Packet {
    public static final int MAX_LEN = 1024;

    PacketType type;
    byte[] data;

    public Packet(byte[] packetBytes) {
        this(
                PacketType.values()[packetBytes[0]],
                (packetBytes.length > 1) ?
                        Arrays.copyOfRange(packetBytes, 1, Math.min(MAX_LEN, packetBytes.length))
                        : null
        );
    }

    public Packet(PacketType type, byte[] data) {
        this.type = type;
        this.data = data == null ? null : Arrays.copyOfRange(data, 0, Math.min(MAX_LEN - 1, data.length));
    }

    public byte[] getData() {
        return data.clone();
    }

    public byte[] pack() {
        // Type + Length +
        ByteBuffer buf = ByteBuffer.allocate(MAX_LEN);
        buf.put((byte) type.ordinal());
        if (data != null) {
            buf.put(data);
        }
        return buf.array();
    }

    public ByteBuffer toByteBuffer() {
        return ByteBuffer.wrap(pack());
    }

    public byte[] unpack() {
        return data;
    }

    public PacketType getType() {
        return type;
    }

    public String getTypeString() {
        return getType().toString();
    }

    @Override
    public String toString() {
        return Arrays.toString(unpack());
    }
}
