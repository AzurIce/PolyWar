package com.azurice.polywar.network.packet;

import com.azurice.polywar.network.data.GamePlayerData;

import java.io.*;
import java.util.List;

import static com.azurice.polywar.network.packet.Type.GAME_PLAYER_DATA_LIST_PACKET;

public class GamePlayerDataListPacket extends Packet {
    public GamePlayerDataListPacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public GamePlayerDataListPacket(byte[] data) {
        super(data);
    }

    @Override
    public Type getType() {
        return GAME_PLAYER_DATA_LIST_PACKET;
    }

    public static GamePlayerDataListPacket of(List<GamePlayerData> gamePlayersData) {
        return new GamePlayerDataListPacket(bytesOf(gamePlayersData));
    }

    private static byte[] bytesOf(List<GamePlayerData> gamePlayerDataList) {
        byte[] data;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(gamePlayerDataList.toArray(new GamePlayerData[0]));
            data = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        LOGGER.info("Bytes of " + roomList + ": " + Arrays.toString(data));
        return data;
    }

    public List<GamePlayerData> getGamePlayerDataList() {
        GamePlayerData[] gamePlayerDataList;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            try {
                gamePlayerDataList = (GamePlayerData[]) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return List.of(gamePlayerDataList);
    }

    @Override
    public String toString() {
        return getGamePlayerDataList().toString();
    }
}
