package com.azurice.polywar.server;

import com.azurice.polywar.util.Util;

public class PolyWarServer {
    private static PolyWarServer instance = new PolyWarServer();
    private boolean running = true;

    private PolyWarServer() {
    }

    public PolyWarServer getInstance() {
        return instance;
    }

    public void run() {
        while (this.running) {
            long tickStartTime = Util.getMeasuringTimeMs();
            tick();
            try {
                Thread.sleep(1000 / 20 - Util.getMeasuringTimeMs() + tickStartTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void tick() {

    }
}
