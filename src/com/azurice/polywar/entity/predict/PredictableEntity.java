package com.azurice.polywar.entity.predict;

import com.azurice.polywar.client.PolyWarClient;
import com.azurice.polywar.entity.DrawableEntity;
import com.azurice.polywar.util.Util;
import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public class PredictableEntity extends DrawableEntity {
    private long lastTickTime = Util.getMeasuringTimeNano();
    private Vec2d lastTickCoord;
    private Vec2d lastTickSpeed;


    ////// Constructors //////
    public PredictableEntity(Polygon model) {
        super(model);
        lastTickCoord = new Vec2d(coord);
        lastTickSpeed = new Vec2d(speed);
    }

    public PredictableEntity(Vec2d coord, Polygon model) {
        super(coord, model);
        lastTickCoord = new Vec2d(coord);
        lastTickSpeed = new Vec2d(speed);
    }

    public PredictableEntity(Vec2d coord, Polygon model, Color color) {
        super(coord, model, color);
        lastTickCoord = new Vec2d(coord);
        lastTickSpeed = new Vec2d(speed);
    }


    ////// Tick //////
    @Override
    public void tick() {
        super.tick();
        lastTickCoord = new Vec2d(coord);
        lastTickSpeed = new Vec2d(speed);
        updateLastTickTime();
    }

    ////// Core part //////
    // Calculate the predicted coordinate based on status of lastTick and the time passed after lastTick.
    public Vec2d getPredictedCoord() {
        long curTime = Util.getMeasuringTimeNano();
        return lastTickCoord.add(
                lastTickSpeed.multiply((curTime - getLastTickTime()) / (1E9 / PolyWarClient.TICK_RATE))
        );
    }

    public Polygon getPredictedPolygon() {
        return getModel().add(getPredictedCoord());
    }

    // To let the renderer use the predicted polygon but not the logic polygon
    @Override
    public Polygon getRenderPolygon() {
        return getPredictedPolygon();
    }


    ////// Getters & Setters //////
    public long getLastTickTime() {
        return lastTickTime;
    }

    public void updateLastTickTime() {
        lastTickTime = Util.getMeasuringTimeNano();
    }

}
