package com.azurice.polywar.entity;

import com.azurice.polywar.client.ui.component.MapView;
import com.azurice.polywar.util.math.Vec2d;

public abstract class AbstractEntity {
    protected Vec2d speed = Vec2d.ZERO;
    protected Vec2d coord;
    protected MapView mapView;

    public AbstractEntity(Vec2d coord, MapView mapView) {
        this.coord = coord;
        this.mapView = mapView;
    }

    public void tick() {
        coord = coord.add(speed);
    }
}
