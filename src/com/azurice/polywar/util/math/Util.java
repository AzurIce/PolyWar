package com.azurice.polywar.util.math;

public class Util {
    public static double clip(double value, double l, double r) {
        return value < l ? l : Math.min(value, r);
    }
}
