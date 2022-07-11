package com.azurice.polywar.network.packet;

import com.azurice.polywar.server.Player;

import java.util.List;

import static com.azurice.polywar.network.packet.Type.PLAYER_LIST_PACKET;

public class PlayerListPacket extends AbstractObjectPacket {

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
        return new PlayerListPacket(bytesOf(playerList.toArray(new Player[0])));
    }

    @Override
    public List<Player> getData() {
        return List.of((Player[]) super.getData());
    }
}
