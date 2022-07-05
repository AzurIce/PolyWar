package com.azurice.polywar.util.math;


/**
 * An immutable vector composed of 2 floats.
 */
public class Vec2d {
    public static final Vec2d ZERO = new Vec2d(0, 0);
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

    public Vec2d multiply(float value) {
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

    public Vec2d minus(Vec2d vec) {
        return add(vec.negate());
    }

    public boolean equals(Vec2d other) {
        return x == other.x && y == other.y;
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
}