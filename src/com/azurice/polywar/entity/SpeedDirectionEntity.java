package com.azurice.polywar.entity;

import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public class SpeedDirectionEntity extends DrawableEntity {
    private double angle;


    public SpeedDirectionEntity(Polygon model) {
        super(model);
    }

    public SpeedDirectionEntity(Vec2d coord, Polygon model) {
        super(coord, model);
    }

    public SpeedDirectionEntity(Vec2d coord, Polygon model, Color color) {
        super(coord, model, color);
    }

    // Override it for the direction
    @Override
    public Polygon getPolygon() {
        if (speed != Vec2d.ZERO) {
            setAngle(speed.getAngle());
        }
        return getModel().rotate(angle).add(coord);
    }

    ////// Getters & Setters
    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
