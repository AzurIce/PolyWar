package com.azurice.polywar.network.packet;

import com.azurice.polywar.network.data.GamePlayerData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.List;

public class GamePlayerDataPacket extends Packet {
    private static final Logger LOGGER = LogManager.getLogger();

    public GamePlayerDataPacket(List<PacketBlock> blocks) {
        super(Type.GAME_PLAYER_DATA_PACKET, blocks);
    }

    public GamePlayerDataPacket(byte[] data) {
        super(Type.GAME_PLAYER_DATA_PACKET, data);
    }

    public static GamePlayerDataPacket of(GamePlayerData gamePlayerData) {
        return new GamePlayerDataPacket(bytesOf(gamePlayerData));
    }

    private static byte[] bytesOf(GamePlayerData gamePlayerData) {
        byte[] data;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(gamePlayerData);
            data = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        LOGGER.info("Bytes of " + gamePlayer + ": " + Arrays.toString(data));
        return data;
    }

    public GamePlayerData getGamePlayerData() {
        GamePlayerData gamePlayerData;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            try {
                gamePlayerData = (GamePlayerData) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return gamePlayerData;
    }

    @Override
    public String toString() {
        return getGamePlayerData().toString();
    }
}