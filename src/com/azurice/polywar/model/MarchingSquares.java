package com.azurice.polywar.model;

import com.azurice.polywar.util.math.Polygon;

import static com.azurice.polywar.util.math.Vec2d.*;


public class MarchingSquares {
    public static final Polygon MARCHING_SQUARE_0 = new Polygon();
    public static final Polygon MARCHING_SQUARE_1 = new Polygon(DL, D, L);
    public static final Polygon MARCHING_SQUARE_2 = MARCHING_SQUARE_1.rotate(Math.PI / 2);
    public static final Polygon MARCHING_SQUARE_4 = MARCHING_SQUARE_2.rotate(Math.PI / 2);
    public static final Polygon MARCHING_SQUARE_8 = MARCHING_SQUARE_4.rotate(Math.PI / 2);

    public static final Polygon MARCHING_SQUARE_14 = new Polygon(UL, L, D, DR, UR);
    public static final Polygon MARCHING_SQUARE_13 = MARCHING_SQUARE_14.rotate(Math.PI / 2);
    public static final Polygon MARCHING_SQUARE_11 = MARCHING_SQUARE_13.rotate(Math.PI / 2);
    public static final Polygon MARCHING_SQUARE_7 = MARCHING_SQUARE_11.rotate(Math.PI / 2);


    public static final Polygon MARCHING_SQUARE_3 = new Polygon(U, UL, DL, D);
    public static final Polygon MARCHING_SQUARE_6 = MARCHING_SQUARE_3.rotate(Math.PI / 2);
    public static final Polygon MARCHING_SQUARE_12 = MARCHING_SQUARE_6.rotate(Math.PI / 2);
    public static final Polygon MARCHING_SQUARE_9 = MARCHING_SQUARE_12.rotate(Math.PI / 2);

    public static final Polygon MARCHING_SQUARE_5 = new Polygon(U, L, DL, D, R, UR);
    public static final Polygon MARCHING_SQUARE_10 = MARCHING_SQUARE_5.rotate(Math.PI / 2);


    public static final Polygon MARCHING_SQUARE_15 = new Polygon(UL, DL, DR, UR);
}