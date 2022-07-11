package com.azurice.polywar.network.packet;

import com.azurice.polywar.world.WorldMap;

import java.util.List;

public class MapPacket extends AbstractObjectPacket {

    public MapPacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public MapPacket(byte[] data) {
        super(data);
    }

    public static MapPacket of(WorldMap map) {
        return new MapPacket(bytesOf(map));
    }

    public WorldMap getMap() {
        return (WorldMap) getObject();
    }

    @Override
    public Type getType() {
        return Type.MAP_PACKET;
    }

    @Override
    public String toString() {
        return getMap().toString();
    }
}