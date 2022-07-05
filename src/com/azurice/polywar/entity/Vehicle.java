package com.azurice.polywar.entity;

import com.azurice.polywar.client.ui.component.MapView;
import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public class Vehicle extends Entity {
    private static final Polygon model = new Polygon(
            new Vec2d(0, -15), new Vec2d(-10, 10), new Vec2d(0, 5), new Vec2d(10, 10)
    );
    private Color color = new Color(255, 0, 0);

    public Vehicle(Vec2d coord, MapView mapView) {
        super(coord, mapView);
    }

    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(color);
        g2d.fillPolygon(model.add(coord).toAwtPolygon());
    }

    @Override
    public Polygon getPolygon() {
        return model;
    }
}
