package com.azurice.polywar.util.math;

public class Util {
    public static double clip(double value, double l, double r) {
        return value < l ? l : Math.min(value, r);
    }

    /**
     * Map the value from [0, originBound) to [0, targetBound)
     *
     * @param value       The value
     * @param originBound Original bound
     * @param targetBound Target bound
     * @return Mapped value
     */
    public static double mapValue(int value, int originBound, int targetBound) {
        return (value + 1.0) / originBound * targetBound;
    }
}
