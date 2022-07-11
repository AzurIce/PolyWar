package com.azurice.polywar.network.packet;

import com.azurice.polywar.network.data.GamePlayerData;

import java.util.List;

import static com.azurice.polywar.network.packet.Type.GAME_PLAYER_DATA_LIST_PACKET;

public class GamePlayerDataListPacket extends AbstractObjectPacket {

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
        return new GamePlayerDataListPacket(bytesOf(gamePlayersData.toArray(new GamePlayerData[0])));
    }

    @Override
    public List<GamePlayerData> getData() {
        return List.of((GamePlayerData[]) super.getData());
    }
}
