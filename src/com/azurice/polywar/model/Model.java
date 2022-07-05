package com.azurice.polywar.model;

import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public class Model extends Polygon {
    public static final Polygon VEHICLE_MODEL = new Polygon(
            new Vec2d(0, 15), new Vec2d(-10, -10), new Vec2d(0, -5), new Vec2d(10, -10)
    );
    private Color color;

    public Model(Color color, Vec2d... points) {
        super(points);
        this.color = color;
    }
}
