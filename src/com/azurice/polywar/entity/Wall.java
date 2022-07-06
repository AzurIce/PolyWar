package com.azurice.polywar.entity;

import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public class Wall extends BaseDrawableEntity {
    //    public Wall(MapView mapView, Polygon model) {
//        super(mapView, model);
//        setColor(new Color(176, 196, 222));
//    }
    public Wall(Polygon model) {
        super(model);
        setColor(new Color(176, 196, 222));
    }

    public Wall(Vec2d coord, Polygon model) {
        super(coord, model);
        setColor(new Color(176, 196, 222));
    }
}
