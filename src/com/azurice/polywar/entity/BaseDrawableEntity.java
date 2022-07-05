package com.azurice.polywar.entity;

import com.azurice.polywar.client.ui.GamePage;
import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public class BaseDrawableEntity extends AbstractDrawableEntity {

    public BaseDrawableEntity(GamePage gamePage, Polygon model) {
        super(gamePage, model);
    }

    public BaseDrawableEntity(Vec2d coord, GamePage gamePage, Polygon model) {
        super(coord, gamePage, model);
    }

    public BaseDrawableEntity(Vec2d coord, GamePage gamePage, Polygon model, Color color) {
        super(coord, gamePage, model, color);
    }

    @Override
    public java.awt.Polygon getAwtPolygon() {
        return MODEL.add(coord).toAwtPolygon();
    }
}
