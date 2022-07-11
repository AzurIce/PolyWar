package com.azurice.polywar.entity.predict;

import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;
import java.io.Serializable;

public class SpeedDirectionEntity extends PredictableEntity implements Serializable {
    protected double angle;


    ////// Constructors //////
    public SpeedDirectionEntity(Polygon model) {
        super(model);
    }

    public SpeedDirectionEntity(Vec2d coord, Polygon model) {
        super(coord, model);
    }

    public SpeedDirectionEntity(Vec2d coord, Polygon model, Color color) {
        super(coord, model, color);
    }

    ////// Core part //////
    // Now the model has a rotation based on the original model.
    @Override
    public Polygon getModel() {
        if (speed != Vec2d.ZERO) {
            setAngle(speed.getAngle());
        }
        return super.getModel().rotate(angle);
    }


    ////// Getters & Setters //////
    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
