package com.azurice.polywar.network.packet;

import java.util.List;

import static com.azurice.polywar.network.packet.Type.MAP_RADIUS_PACKET;

public class MapRadiusPacket extends AbstractObjectPacket {

    public MapRadiusPacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public MapRadiusPacket(byte[] data) {
        super(data);
    }

    public static MapRadiusPacket of(Double r) {
        return new MapRadiusPacket(bytesOf(r));
    }

    @Override
    public Type getType() {
        return MAP_RADIUS_PACKET;
    }

    @Override
    public Double getData() {
        return (Double) super.getData();
    }
}
