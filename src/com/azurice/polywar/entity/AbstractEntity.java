package com.azurice.polywar.entity;

import com.azurice.polywar.util.math.Vec2d;

public abstract class AbstractEntity {
    protected Vec2d speed = Vec2d.ZERO;
    protected Vec2d coord;
//    protected MapView mapView;

//    public AbstractEntity(Vec2d coord, MapView mapView) {
//        this.coord = coord;
//        this.mapView = mapView;
//    }


    public Vec2d getCoord() {
        return coord;
    }

    public void setCoord(Vec2d coord) {
        this.coord = coord;
    }

    public AbstractEntity(Vec2d coord) {
        this.coord = coord;
//        this.mapView = mapView;
    }

    public void tick() {
        coord = coord.add(speed);
    }
}
