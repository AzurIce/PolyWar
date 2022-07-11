package com.azurice.polywar.network.packet;

import com.azurice.polywar.network.data.GamePlayerControlData;

import java.util.List;

import static com.azurice.polywar.network.packet.Type.GAME_PLAYER_CONTROL_DATA;

public class GamePlayerControlDataPacket extends AbstractObjectPacket {

    public GamePlayerControlDataPacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public GamePlayerControlDataPacket(byte[] data) {
        super(data);
    }

    @Override
    public Type getType() {
        return GAME_PLAYER_CONTROL_DATA;
    }

    public static GamePlayerControlDataPacket of(GamePlayerControlData gamePlayerControlData) {
        return new GamePlayerControlDataPacket(bytesOf(gamePlayerControlData));
    }

    @Override
    public GamePlayerControlData getData() {
        return (GamePlayerControlData) super.getData();
    }
}