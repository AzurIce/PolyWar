package com.azurice.polywar.model;

import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;
import java.util.List;

public class Model extends Polygon {
    public static final List<Polygon> MARCHING_SQUARE = List.of(
            MarchingSquares.MARCHING_SQUARE_0,
            MarchingSquares.MARCHING_SQUARE_1,
            MarchingSquares.MARCHING_SQUARE_2,
            MarchingSquares.MARCHING_SQUARE_3,
            MarchingSquares.MARCHING_SQUARE_4,
            MarchingSquares.MARCHING_SQUARE_5,
            MarchingSquares.MARCHING_SQUARE_6,
            MarchingSquares.MARCHING_SQUARE_7,
            MarchingSquares.MARCHING_SQUARE_8,
            MarchingSquares.MARCHING_SQUARE_9,
            MarchingSquares.MARCHING_SQUARE_10,
            MarchingSquares.MARCHING_SQUARE_11,
            MarchingSquares.MARCHING_SQUARE_12,
            MarchingSquares.MARCHING_SQUARE_13,
            MarchingSquares.MARCHING_SQUARE_14,
            MarchingSquares.MARCHING_SQUARE_15
    );

    public static final Polygon PLAYER = new Polygon(
            new Vec2d(0, 15), new Vec2d(-10, -10), new Vec2d(0, -5), new Vec2d(10, -10)
    );

    public static final Polygon MISSILE = new Polygon(
            new Vec2d(0, 15), new Vec2d(-10, -10), new Vec2d(0, -5), new Vec2d(10, -10)
    );
    private Color color;

    public Model(Color color, Vec2d... points) {
        super(points);
        this.color = color;
    }
}
