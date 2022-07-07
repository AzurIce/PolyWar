package com.azurice.polywar.client;

import com.azurice.polywar.client.ui.BasicWindow;
import com.azurice.polywar.client.ui.component.MapView;
import com.azurice.polywar.util.Util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PolyWarClient extends BasicWindow {
    public static final int TICK_RATE = 20;
    public static final int FRAME_RATE = 144;

    // Singleton
    private static final PolyWarClient instance = new PolyWarClient();
    // Views
    private MapView mapView = new MapView();

    private PolyWarClient() {
    }


    private Thread thread;
    private boolean running = true;

    public static PolyWarClient getInstance() {
        return instance;
    }

    // Runtime control
    public void run() {
        display();

        Thread logicThread = new Thread(() -> {
            while (running) {
                long timeTickStart = Util.getMeasuringTimeMs();
                tick();
                try {
                    Thread.sleep(1000 / TICK_RATE - Util.getMeasuringTimeMs() + timeTickStart);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        logicThread.setPriority(Thread.MAX_PRIORITY);
        logicThread.start();

        while (running) {
            long timeFrameStart = Util.getMeasuringTimeMs();
            render();
            try {
                Thread.sleep(1000 / FRAME_RATE - Util.getMeasuringTimeMs() + timeFrameStart);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        stop();
    }

    public void scheduleStop() {
        running = false;
    }

    private void stop() {
//        try {
//            try {
//                if (this.world != null) {
//                    this.world.disconnect();
//                }
//                this.disconnect();
//            }
//            catch (Throwable throwable) {
//                // empty catch block
//            }
//            if (this.currentScreen != null) {
//                this.currentScreen.removed();
//            }
//            this.close();
//        }
//        finally {
//            Util.nanoTimeSupplier = System::nanoTime;
//            if (this.crashReportSupplier == null) {
        System.exit(0);
//            }
//        }
    }


    // Render & tick
    private void render() {
//        System.out.println(tickDelta);
        mapView.render();
//        tick();
    }

    private void tick() {
        mapView.tick();
        // TODO: InGameHUD
    }

    // Overrides of BasicWindow
    @Override
    public void initViews() {
        setContentPane(mapView);
    }

    @Override
    public void initListeners() {
        super.initListeners();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
//                System.out.println(e);
                super.keyPressed(e);
                mapView.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
//                System.out.println(e);
                super.keyReleased(e);
                mapView.keyReleased(e);
            }
        });
    }

    @Override
    public void onClose() {
        scheduleStop();
    }
}
