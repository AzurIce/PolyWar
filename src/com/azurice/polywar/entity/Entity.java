package com.azurice.polywar.entity;

import com.azurice.polywar.client.ui.component.MapView;
import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public abstract class Entity {
    protected Vec2d coord;
    protected Vec2d speed;

    protected MapView mapView;

    public Entity(Vec2d coord, MapView mapView) {
        this.coord = coord;
        this.mapView = mapView;
    }

    public abstract void paint(Graphics g);

    public abstract Polygon getPolygon();
}
