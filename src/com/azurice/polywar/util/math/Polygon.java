package com.azurice.polywar.util.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon {
    public List<Vec2d> pointList = new ArrayList<>();
    private double precision = 0.1;

    /**
     * !!!IMPORTANT
     * The points have to be permuted in counterclockwise
     *
     * @param points
     */
    public Polygon(Vec2d... points) {
        pointList.addAll(Arrays.asList(points));
    }

    public void move(Vec2d vec) {
        for (int i = 0; i < pointList.size(); i++) {
            pointList.set(i, pointList.get(i).add(vec));
        }
    }

    public Polygon add(Vec2d vec) {
        List<Vec2d> newPointList = new ArrayList<>();
        for (Vec2d point : pointList) {
            newPointList.add(point.add(vec));
        }
        return new Polygon(newPointList.toArray(new Vec2d[0]));
    }

    public boolean contains(Vec2d p) {
        for (int i = 0; i < pointList.size(); i++) {
            Vec2d cur = pointList.get(i);
            Vec2d next = pointList.get((i + 1) % pointList.size());
            Vec2d edge = next.add(cur.negate());
            Vec2d vec = p.minus(cur);
            if (edge.multiply(vec).z < 0) {
                return false;
            }
        }
        return true;
    }

    public boolean intersect(Polygon poly) {
        for (int i = 0; i < pointList.size(); i++) {
            Vec2d cur = pointList.get(i);
            Vec2d next = pointList.get((i + 1) % pointList.size());
            Vec2d edge = next.add(cur.negate());

            Vec2d samplePoint = new Vec2d(cur);
            for (int j = 0; j < edge.length() / precision; j++) {
                if (poly.contains(samplePoint)) {
                    return true;
                }
                samplePoint = samplePoint.add(precision);
            }
        }
        return false;
    }


    public java.awt.Polygon toAwtPolygon() {
        int[] xPoints = new int[pointList.size()];
        int[] yPoints = new int[pointList.size()];
        for (int i = 0; i < pointList.size(); i++) {
            xPoints[i] = (int) pointList.get(i).x;
            yPoints[i] = (int) pointList.get(i).y;
//            System.out.print("(" + xPoints[i] + ", " + yPoints[i] + ") ");
        }
        System.out.println();
        return new java.awt.Polygon(xPoints, yPoints, pointList.size());
    }
}
