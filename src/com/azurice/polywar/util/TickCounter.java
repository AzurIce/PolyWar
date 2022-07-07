package com.azurice.polywar.util;

public class TickCounter {
    private final double tickTime; // Ms in 1 tick
    public double tickDelta;
    public double lastTickDuration;
    private long prevTimeMillis;

    public TickCounter(double tps, long timeMillis) {
        this.tickTime = 1000d / tps;
        this.prevTimeMillis = timeMillis;
    }

    public int beginRenderTick(long timeMillis) {
        // Since last render
        // How many ticks have passed?
        this.lastTickDuration = (double) (timeMillis - this.prevTimeMillis) / this.tickTime;
        this.prevTimeMillis = timeMillis;

        // Similar to InStream reading buf for chars not yet reached '\n'
        this.tickDelta += this.lastTickDuration;
        int i = (int) this.tickDelta;
        this.tickDelta -= i;

        // How many tick passed in last frame
        return i;
    }
}