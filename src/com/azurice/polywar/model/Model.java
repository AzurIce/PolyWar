package com.azurice.polywar.model;

import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public class Model extends Polygon {
    private Color color;

    public Model(Color color, Vec2d... points) {
        super(points);
        this.color = color;
    }
}
