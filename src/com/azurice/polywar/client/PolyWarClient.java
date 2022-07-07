package com.azurice.polywar.client;

import com.azurice.polywar.client.ui.MainWindow;

public class PolyWarClient {
    public static int TICK_RATE = 20;
    public static int FRAME_RATE = 144;


    // Singleton
    private static final PolyWarClient instance = new PolyWarClient();
    private MainWindow window = new MainWindow(getInstance());

    public static PolyWarClient getInstance() {
        return instance;
    }

    private boolean running = true;
    private boolean stopped = true;

    private PolyWarClient() {
    }

    // Runtime control
    public void run() {
        window.display();

        while (running) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        onStopped();
    }


    private void stop() {
        onStop();
    }

    public void onStop() {
        running = false;
    }

    public void onStopped() {
        stopped = true;
    }

    public void onClose() {
        System.exit(0);
    }
}
