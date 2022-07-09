package com.azurice.polywar.network;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * A Packet has a total length less or equals to 256 Bytes
 */
public class Packet {
    PacketType type;
    byte[] data;

    public Packet(byte[] packetBytes) {
        this(
                PacketType.values()[packetBytes[0]],
                (packetBytes.length > 1) ?
                        Arrays.copyOfRange(packetBytes, 1, Math.min(256, packetBytes.length))
                        : null
        );
    }

    public Packet(PacketType type, byte[] data) {
        this.type = type;
        this.data = data == null ? null : Arrays.copyOfRange(data, 0, Math.min(255, data.length));
    }

    public byte[] getData() {
        return data.clone();
    }

    public byte[] pack() {
        // Type + Length +
        ByteBuffer buf = ByteBuffer.allocate(256);
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
