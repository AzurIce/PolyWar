package com.azurice.polywar.entity;

import com.azurice.polywar.model.Model;
import com.azurice.polywar.util.math.Vec2d;

public class Missile extends SpeedDirectionEntity {
    private static final int SPEED = 10;

    ////// Constructors //////
    public Missile(Vec2d coord, double angle) {
        this(coord, Vec2d.U.multiply(SPEED).rotate(angle));
    }

    public Missile(Vec2d coord, Vec2d speed) {
        super(coord, Model.MISSILE);
        this.speed = speed;
    }

    @Override
    public void tick() {
        super.tick();

    }
}
