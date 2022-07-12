package com.azurice.polywar.network.packet;

import com.azurice.polywar.network.data.GameOverData;

import java.util.List;

public class GameOverPacket extends AbstractObjectPacket {

    public GameOverPacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public GameOverPacket(byte[] data) {
        super(data);
    }

    public static GameOverPacket of(GameOverData gameOverPacket) {
        return new GameOverPacket(bytesOf(gameOverPacket));
    }

    @Override
    public Type getType() {
        return Type.GAME_OVER_PACKET;
    }

    @Override
    public GameOverData getData() {
        return (GameOverData) super.getData();
    }
}
