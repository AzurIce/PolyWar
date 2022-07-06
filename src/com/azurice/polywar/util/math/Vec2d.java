package com.azurice.polywar.util.math;


import static java.lang.Math.acos;

/**
 * An immutable vector composed of 2 floats.
 */
public class Vec2d implements Comparable<Vec2d> {
    public static final Vec2d ZERO = new Vec2d(0, 0);

    public static final Vec2d U = new Vec2d(0, -1);
    public static final Vec2d UL = new Vec2d(-1, -1);
    public static final Vec2d L = new Vec2d(-1, 0);
    public static final Vec2d DL = new Vec2d(-1, 1);
    public static final Vec2d D = new Vec2d(0, 1);
    public static final Vec2d DR = new Vec2d(1, 1);
    public static final Vec2d R = new Vec2d(1, 0);
    public static final Vec2d UR = new Vec2d(1, -1);

    public final double x;
    public final double y;

    public Vec2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2d(Vec2d vec) {
        x = vec.x;
        y = vec.y;
    }

    public double getAngle() {
//        System.out.println("GetAngle " + this + ": " + dot(new Vec2d(0, 1)) / length() + " " + acos(dot(new Vec2d(0, 1)) / length()));
        if (x == 0 && y == 0) {
            return 0;
        } else if (y == 0) {
            return x > 0 ? 3 * Math.PI / 2 : Math.PI / 2;
        } else if (x == 0) {
            return y > 0 ? 0 : Math.PI;
        }
        return (x > 0 ? -1 : 1) * acos(dot(new Vec2d(0, 1)) / length());
    }

    public Vec2d rotate(double angle) {
//        System.out.println("Rotate " + angle + ": ");
//        System.out.println(this);
//        System.out.println(new Vec2d(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle)));
        return new Vec2d(x * Math.cos(angle) - y * Math.sin(angle), x * Math.sin(angle) + y * Math.cos(angle));
    }

    public Vec2d multiply(double value) {
        return new Vec2d(x * value, y * value);
    }

    public Vec3d multiply(Vec2d vec) {
        return new Vec3d(0, 0, x * vec.y - y * vec.x);
    }

    public double dot(Vec2d vec) {
        return x * vec.x + y * vec.y;
    }

    public Vec2d add(Vec2d vec) {
        return new Vec2d(x + vec.x, y + vec.y);
    }

    public Vec2d add(double value) {
        return new Vec2d(x + value, y + value);
    }

    public Vec2d minus(double value) {
        return add(-value);
    }

    public Vec2d minus(Vec2d vec) {
        return add(vec.negate());
    }


    public Vec2d normalize() {
        double f = length();
        return f < 1.0E-4f ? ZERO : new Vec2d(x / f, y / f);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double lengthSquared() {
        return x * x + y * y;
    }

    public double distanceSquared(Vec2d vec) {
        double f = vec.x - x;
        double g = vec.y - y;
        return f * f + g * g;
    }

    public Vec2d negate() {
        return new Vec2d(-x, -y);
    }

    @Override
    public String toString() {
        return "Vec2d(" + x + ", " + y + ")";
    }

    public boolean equals(Vec2d other) {
        return x == other.x && y == other.y;
    }

    @Override
    public int compareTo(Vec2d o) {
        return (int) (length() - o.length());
    }
}