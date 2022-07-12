package com.azurice.polywar.network.packet;

import java.util.List;

public class NamePacket extends AbstractObjectPacket {
    public NamePacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public NamePacket(byte[] data) {
        super(data);
    }

    public static NamePacket of(String name) {
        return new NamePacket(bytesOf(name));
    }

    @Override
    public Type getType() {
        return Type.NAME_PACKET;
    }

    @Override
    public String getData() {
        return (String) super.getData();
    }
}
