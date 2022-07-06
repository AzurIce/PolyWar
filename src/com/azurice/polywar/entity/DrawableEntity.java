package com.azurice.polywar.entity;

import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public class DrawableEntity extends AbstractEntity {
    protected final Polygon MODEL;
    private Color color;

    ////// Constructors //////
    public DrawableEntity(Polygon model) {
        this(Vec2d.ZERO, model);
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
        g2d.fillPolygon(getPolygon().add(offset).toAwtPolygon());
    }

    public Polygon getModel() {
        return MODEL;
    }

    /**
     * Rewrite this method to control rendering of the model
     *
     * @return The Polygon for rendering the model.
     */
    public Polygon getPolygon() {
        return MODEL.add(coord);
    }
}
