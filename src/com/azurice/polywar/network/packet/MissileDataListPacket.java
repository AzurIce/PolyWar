package com.azurice.polywar.network.packet;

import com.azurice.polywar.network.data.MissileData;

import java.util.List;

import static com.azurice.polywar.network.packet.Type.MISSILE_DATA_LIST_PACKET;

public class MissileDataListPacket extends AbstractObjectPacket {
    public MissileDataListPacket(List<PacketBlock> blocks) {
        super(blocks);
    }

    public MissileDataListPacket(byte[] data) {
        super(data);
    }

    public static MissileDataListPacket of(List<MissileData> missileDataList) {
        return new MissileDataListPacket(bytesOf(missileDataList.toArray(new MissileData[0])));
    }

    @Override
    public List<MissileData> getData() {
        return List.of((MissileData[]) super.getData());
    }

    @Override
    public Type getType() {
        return MISSILE_DATA_LIST_PACKET;
    }
}
