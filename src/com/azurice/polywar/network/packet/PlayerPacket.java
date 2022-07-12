package com.azurice.polywar.network.packet;

import com.azurice.polywar.server.Player;

import java.util.List;

import static com.azurice.polywar.network.packet.Type.PLAYER_PACKET;

public class PlayerPacket extends AbstractObjectPacket {
    public PlayerPacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public PlayerPacket(byte[] data) {
        super(data);
    }

    public static PlayerPacket of(Player player) {
        return new PlayerPacket(bytesOf(player));
    }

    @Override
    public Type getType() {
        return PLAYER_PACKET;
    }

    @Override
    public Player getData() {
        return (Player) super.getData();
    }
}
