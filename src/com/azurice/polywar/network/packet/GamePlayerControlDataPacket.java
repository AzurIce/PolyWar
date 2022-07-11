package com.azurice.polywar.network.packet;

import com.azurice.polywar.network.data.GamePlayerControlData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.List;

public class GamePlayerControlDataPacket extends Packet {
    private static final Logger LOGGER = LogManager.getLogger();

    public GamePlayerControlDataPacket(List<PacketBlock> blocks) {
        super(Type.GAME_PLAYER_CONTROL_DATA, blocks);
    }

    public GamePlayerControlDataPacket(byte[] data) {
        super(Type.GAME_PLAYER_CONTROL_DATA, data);
    }

    public static GamePlayerControlDataPacket of(GamePlayerControlData gamePlayerControlData) {
        return new GamePlayerControlDataPacket(bytesOf(gamePlayerControlData));
    }

    private static byte[] bytesOf(GamePlayerControlData gamePlayerControlData) {
        byte[] data;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(gamePlayerControlData);
            data = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        LOGGER.info("Bytes of " + gamePlayer + ": " + Arrays.toString(data));
        return data;
    }

    public GamePlayerControlData getGamePlayerControlData() {
        GamePlayerControlData gamePlayerControlData;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            try {
                gamePlayerControlData = (GamePlayerControlData) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return gamePlayerControlData;
    }

    @Override
    public String toString() {
        return getGamePlayerControlData().toString();
    }
}