package com.azurice.polywar.network.packet;

import com.azurice.polywar.network.data.GamePlayerControlData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static com.azurice.polywar.network.packet.Type.GAME_PLAYER_CONTROL_DATA;

public class GamePlayerControlDataPacket extends AbstractObjectPacket {
    private static final Logger LOGGER = LogManager.getLogger();

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

    public GamePlayerControlData getGamePlayerControlData() {
        return (GamePlayerControlData) getObject();
    }

//    @Override
//    public String toString() {
//        return getGamePlayerControlData().toString();
//    }
}