package com.azurice.polywar.client;

import com.azurice.polywar.client.ui.MainWindow;

public class PolyWarClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 7777;
    public static int TICK_RATE = 20;
    public static int FRAME_RATE = 144;


    // Singleton
    private static final PolyWarClient instance = new PolyWarClient();
    public double fps;

    public static PolyWarClient getInstance() {
        return instance;
    }

    private MainWindow window;

    private PolyWarClient() {
    }

    private boolean running = true;
//    private boolean stopped = true;

    // Runtime control
    public void run() {
        window = new MainWindow(getInstance());
        window.display();

        while (running) {
            // TODO: connect to server
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
//        stopped = true;
        onClose();
    }

    public void onClose() {
        System.exit(0);
    }
}
