package com.azurice.polywar.util.math;

public class Vec3d {
    public static final Vec3d ZERO = new Vec3d(0, 0, 0);
    public final double x;
    public final double y;
    public final double z;

    public Vec3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3d(Vec3d v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public Vec3d multiply(float value) {
        return new Vec3d(x * value, y * value, z * value);
    }

    public double dot(Vec3d vec) {
        return x * vec.x + y * vec.y;
    }

    public Vec3d add(Vec3d vec) {
        return new Vec3d(x + vec.x, y + vec.y, z + vec.z);
    }

    public Vec3d add(double value) {
        return new Vec3d(x + value, y + value, z + value);
    }

    public boolean equals(Vec2d other) {
        return this.x == other.x && this.y == other.y;
    }

    public Vec3d normalize() {
        double f = length();
        return f < 1.0E-4f ? ZERO : new Vec3d(x / f, y / f, z / f);
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public double lengthSquared() {
        return x * x + y * y + z * z;
    }

    public double distanceSquared(Vec3d vec) {
        double f = vec.x - x;
        double g = vec.y - y;
        double h = vec.z - z;
        return f * f + g * g + z * z;
    }

    public Vec3d negate() {
        return new Vec3d(-x, -y, -z);
    }
}
