package com.azurice.polywar.util;

public class Util {
    public static long getMeasuringTimeMs() {
        return Util.getMeasuringTimeNano() / 1000000L;
    }

    public static long getMeasuringTimeNano() {
        return System.nanoTime();
    }
}
