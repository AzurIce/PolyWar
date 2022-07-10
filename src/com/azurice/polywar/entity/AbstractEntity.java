package com.azurice.polywar.entity;

import com.azurice.polywar.util.math.Vec2d;

import java.io.Serializable;

public abstract class AbstractEntity implements Serializable {

    protected Vec2d coord;
    protected Vec2d speed = Vec2d.ZERO;


    public AbstractEntity(Vec2d coord) {
        this.coord = coord;
    }

    // Ticking
    public void tick() {
        coord = coord.add(speed);
    }


    ////// Getters and Setters //////
    public Vec2d getCoord() {
        return coord;
    }

    public void setCoord(Vec2d coord) {
        this.coord = coord;
    }

    public Vec2d getSpeed() {
        return speed;
    }

    public void setSpeed(Vec2d speed) {
        this.speed = speed;
    }
}
