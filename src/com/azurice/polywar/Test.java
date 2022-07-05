package com.azurice.polywar;

import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

public class Test {
    public static void main(String[] args) {
        Polygon poly1 = new Polygon(
                new Vec2d(0, 15), new Vec2d(-10, -10), new Vec2d(0, -5), new Vec2d(10, -10)
        );
        Polygon poly2 = new Polygon(
                new Vec2d(-5, 13), new Vec2d(-5, 16), new Vec2d(5, 16), new Vec2d(5, 13)
        );
        System.out.println(poly1.intersect(poly2));
    }
}
