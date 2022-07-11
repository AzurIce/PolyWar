package com.azurice.polywar.network.packet;

import com.azurice.polywar.network.data.GamePlayerData;

import java.util.List;

import static com.azurice.polywar.network.packet.Type.GAME_PLAYER_DATA_PACKET;

public class GamePlayerDataPacket extends AbstractObjectPacket {

    public GamePlayerDataPacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public GamePlayerDataPacket(byte[] data) {
        super(data);
    }

    @Override
    public Type getType() {
        return GAME_PLAYER_DATA_PACKET;
    }

    public static GamePlayerDataPacket of(GamePlayerData gamePlayerData) {
        return new GamePlayerDataPacket(bytesOf(gamePlayerData));
    }


    public GamePlayerData getGamePlayerData() {
        return (GamePlayerData) getObject();
    }

    @Override
    public String toString() {
        return getGamePlayerData().toString();
    }
}