package com.azurice.polywar.entity;

import com.azurice.polywar.client.ui.component.MapView;
import com.azurice.polywar.util.math.Polygon;

import java.awt.*;

public class Wall extends BaseDrawableEntity {
    public Wall(MapView mapView, Polygon model) {
        super(mapView, model);
        setColor(new Color(176, 196, 222));
    }
}
