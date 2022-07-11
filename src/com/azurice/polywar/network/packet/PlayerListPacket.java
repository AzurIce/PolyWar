package com.azurice.polywar.network.packet;

import com.azurice.polywar.server.Player;

import java.io.*;
import java.util.List;

import static com.azurice.polywar.network.packet.Type.PLAYER_LIST_PACKET;

public class PlayerListPacket extends Packet {
    public PlayerListPacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public PlayerListPacket(byte[] data) {
        super(data);
    }

    @Override
    public Type getType() {
        return PLAYER_LIST_PACKET;
    }

    public static PlayerListPacket of(List<Player> playerList) {
        return new PlayerListPacket(bytesOf(playerList));
    }

    private static byte[] bytesOf(List<Player> playerList) {
        byte[] data;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(playerList.toArray(new Player[0]));
            data = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        LOGGER.info("Bytes of " + roomList + ": " + Arrays.toString(data));
        return data;
    }

    public List<Player> getPlayerList() {
        Player[] playerList;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(getData());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            try {
                playerList = (Player[]) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return List.of(playerList);
    }

    @Override
    public String toString() {
        return getPlayerList().toString();
    }
}
