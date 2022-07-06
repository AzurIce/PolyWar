package com.azurice.polywar.util.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon {
    public List<Vec2d> pointList = new ArrayList<>();
    private double precision = 1;

    /**
     * !!!IMPORTANT
     * The points have to be permuted in counterclockwise<br>
     * NOTE:
     * The coordinate system in the game is like this:<br>
     * +-----> x<br>
     * |<br>
     * |<br>
     * v<br>
     * y<br>
     * The word "counterclockwise" here is the counterclockwise in this coordinate system,
     * but not the normal coordinate system.
     *
     * @param points The points for composing a polygon
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

    public Polygon multiply(double value) {
        List<Vec2d> newPointList = new ArrayList<>();
        for (Vec2d point : pointList) {
            newPointList.add(point.multiply(value));
        }
        return new Polygon(newPointList.toArray(new Vec2d[0]));
    }

    public Polygon rotate(double angle) {
//        System.out.println("Rotate " + angle + ": ");
//        System.out.println(this);
        List<Vec2d> newPointList = new ArrayList<>();
        for (Vec2d point : pointList) {
            newPointList.add(point.rotate(angle));
        }
//        System.out.println(new Polygon(newPointList.toArray(new Vec2d[0])));
        return new Polygon(newPointList.toArray(new Vec2d[0]));
    }

    public boolean contains(Vec2d p) {
        for (int i = 0; i < pointList.size(); i++) {
            Vec2d cur = pointList.get(i);
            Vec2d next = pointList.get((i + 1) % pointList.size());
            Vec2d edge = next.add(cur.negate());
            Vec2d vec = p.minus(cur);

            if (edge.multiply(vec).z > 0) {
                return false;
            }
        }
//        System.out.println("Contains: " + this + " " + p);
        return true;
    }

    public Vec2d intersect(Polygon poly) {
        for (int i = 0; i < pointList.size(); i++) {
            Vec2d cur = pointList.get(i);
            Vec2d next = pointList.get((i + 1) % pointList.size());
            Vec2d edge = next.minus(cur);

            Vec2d samplePoint = new Vec2d(cur);
            for (int j = 0; j < edge.length() / precision; j++) {
//                System.out.println(samplePoint);
                if (poly.contains(samplePoint)) {
//                    System.out.println("Intersect detection: " + this + " " + poly);
//                    System.out.println("Edge: " + edge);
//                    System.out.println("SamplePoint: " + samplePoint);
                    return samplePoint;
                }
                samplePoint = samplePoint.add(edge.normalize().multiply(precision));
            }
        }
        return Vec2d.ZERO;
    }


    public java.awt.Polygon toAwtPolygon() {
        int[] xPoints = new int[pointList.size()];
        int[] yPoints = new int[pointList.size()];
        for (int i = 0; i < pointList.size(); i++) {
            xPoints[i] = (int) pointList.get(i).x;
            yPoints[i] = (int) pointList.get(i).y;
//            System.out.print("(" + xPoints[i] + ", " + yPoints[i] + ") ");
        }
//        System.out.println();
        return new java.awt.Polygon(xPoints, yPoints, pointList.size());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Polygon[");
        for (int i = 0; i < pointList.size() - 1; i++) {
            sb.append(pointList.get(i));
            sb.append(", ");
        }
        if (pointList.size() > 0) {
            sb.append(pointList.get(pointList.size() - 1));
        }
        sb.append("]");
        return sb.toString();
    }
}
