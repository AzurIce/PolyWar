package com.azurice.polywar.entity;

import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public class BaseDrawableEntity extends AbstractDrawableEntity {

//    public BaseDrawableEntity(MapView mapView, Polygon model) {
//        super(mapView, model);
//    }
//
//    public BaseDrawableEntity(Vec2d coord, MapView mapView, Polygon model) {
//        super(coord, mapView, model);
//    }
//
//    public BaseDrawableEntity(Vec2d coord, MapView mapView, Polygon model, Color color) {
//        super(coord, mapView, model, color);
//    }

    public BaseDrawableEntity(Polygon model) {
        super(model);
    }

    public BaseDrawableEntity(Vec2d coord, Polygon model) {
        super(coord, model);
    }

    public BaseDrawableEntity(Vec2d coord, Polygon model, Color color) {
        super(coord, model, color);
    }

}
