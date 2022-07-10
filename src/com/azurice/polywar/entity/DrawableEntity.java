package com.azurice.polywar.entity;

import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;
import java.io.Serializable;

public class DrawableEntity extends AbstractEntity implements Serializable {
    protected final Polygon MODEL;
    protected Color color;

    ////// Constructors //////
    public DrawableEntity(Polygon model) {
        this(Vec2d.ZERO, model);
    }

    public DrawableEntity(Polygon model, Color color) {
        this(Vec2d.ZERO, model, color);
    }

    public DrawableEntity(Vec2d coord, Polygon model) {
        this(coord, model, new Color(255, 0, 0));
    }

    public DrawableEntity(Vec2d coord, Polygon model, Color color) {
        super(coord);
        this.MODEL = model;
        this.color = color;
    }

    ////// Getters & Setters //////
    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor(Color color) {
        return color;
    }


    // Painting
    public void paint(Graphics g) {
        this.paint(g, Vec2d.ZERO);
    }

    public void paint(Graphics g, Vec2d offset) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(color);
        g2d.fillPolygon(getRenderPolygon().add(offset).toAwtPolygon());
    }

    /**
     * Get the Polygon of the current entity model.
     *
     * @return Polygon
     */

    public Polygon getModel() {
        return MODEL;
    }

    /**
     * Get the Polygon of the current entity model in world.
     *
     * @return Polygon in world
     */
    public Polygon getPolygon() {
        return getModel().add(coord);
    }

    /**
     * Get the Polygon for rendering
     *
     * @return Polygon for rendering
     */
    public Polygon getRenderPolygon() {
        return getPolygon();
    }
}
