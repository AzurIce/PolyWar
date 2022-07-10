package com.azurice.polywar.entity;

import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;
import java.io.Serializable;

public class Wall extends DrawableEntity implements Serializable {
    public Wall(Polygon model) {
        super(model, new Color(176, 196, 222));
    }

    public Wall(Vec2d coord, Polygon model) {
        super(coord, model, new Color(176, 196, 222));
    }
}
