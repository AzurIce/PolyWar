package com.azurice.polywar.network.packet;

import com.azurice.polywar.network.data.GameOverData;

import java.util.List;

public class GameOverDataListPacket extends AbstractObjectPacket {
    public GameOverDataListPacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public GameOverDataListPacket(byte[] data) {
        super(data);
    }

    public static GameOverDataListPacket of(List<GameOverData> gameOverDataList) {
        return new GameOverDataListPacket(bytesOf(gameOverDataList.toArray(new GameOverData[0])));
    }

    @Override
    public Type getType() {
        return Type.GAME_OVER_DATA_LIST_PACKET;
    }

    @Override
    public List<GameOverData> getData() {
        return List.of((GameOverData[]) super.getData());
    }
}
