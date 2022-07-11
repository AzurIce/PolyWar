package com.azurice.polywar.network.data;

import com.azurice.polywar.util.math.Vec2d;

import java.io.Serializable;

public class MissileData implements Serializable {
    public Vec2d coord;
    public Vec2d speed;
    public int ownerId;

    public MissileData(Vec2d coord, Vec2d speed, int ownerId) {
        this.coord = coord;
        this.speed = speed;
        this.ownerId = ownerId;
    }
}
