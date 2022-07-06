package com.azurice.polywar.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) {
        Thread.currentThread().setName("Thread-Renderer");
        PolyWarClient polyWarClient = PolyWarClient.getInstance();

        try {
            polyWarClient.run();
        } catch (Throwable throwable) {
            LOGGER.error("Exception in client thread", throwable);
        }
    }
}
