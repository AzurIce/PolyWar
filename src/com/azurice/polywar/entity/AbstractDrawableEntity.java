package com.azurice.polywar.entity;

import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public abstract class AbstractDrawableEntity extends AbstractEntity {
    protected final Polygon MODEL;
    private Color color;

//    public AbstractDrawableEntity(MapView mapView, Polygon model) {
//        this(Vec2d.ZERO, mapView, model);
//    }
//
//    public AbstractDrawableEntity(Vec2d coord, MapView mapView, Polygon model) {
//        this(coord, mapView, model, new Color(255, 0, 0));
//    }
//
//    public AbstractDrawableEntity(Vec2d coord, MapView mapView, Polygon model, Color color) {
//        super(coord, mapView);
//        this.MODEL = model;
//        this.color = color;
//    }

    public AbstractDrawableEntity(Polygon model) {
        this(Vec2d.ZERO, model);
    }

    public AbstractDrawableEntity(Vec2d coord, Polygon model) {
        this(coord, model, new Color(255, 0, 0));
    }

    public AbstractDrawableEntity(Vec2d coord, Polygon model, Color color) {
        super(coord);
        this.MODEL = model;
        this.color = color;
    }


    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor(Color color) {
        return color;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(color);
        g2d.fillPolygon(getAwtPolygon());
    }

    public Polygon getModel() {
        return MODEL;
    }

    public Polygon getPolygon() {
        return MODEL.add(coord);
    }

    /**
     * Rewrite this method to control rendering of the model
     *
     * @return The AwtPolygon for rendering the model.
     */
    public abstract java.awt.Polygon getAwtPolygon();
}
