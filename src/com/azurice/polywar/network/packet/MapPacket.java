package com.azurice.polywar.network.packet;

import com.azurice.polywar.world.WorldMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.List;

public class MapPacket extends Packet {
    private static final Logger LOGGER = LogManager.getLogger();

    public MapPacket(List<PacketBlock> blocks) {
        super(Type.MAP_PACKET, blocks);
    }

    public MapPacket(byte[] data) {
        super(Packet.Type.MAP_PACKET, data);
    }

    public static MapPacket of(WorldMap map) {
        return new MapPacket(bytesOf(map));
    }

    private static byte[] bytesOf(WorldMap map) {
        byte[] data;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(map);
            data = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        LOGGER.info("Bytes of " + map + ": " + Arrays.toString(data));
        return data;
    }

    public WorldMap getMap() {
        WorldMap map;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            try {
                map = (WorldMap) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    @Override
    public String toString() {
        return getMap().toString();
    }
}