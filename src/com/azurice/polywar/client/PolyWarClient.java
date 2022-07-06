package com.azurice.polywar.client;

import com.azurice.polywar.client.ui.BasicWindow;
import com.azurice.polywar.client.ui.component.MapView;
import com.azurice.polywar.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class PolyWarClient extends BasicWindow {
    private static final PolyWarClient instance = new PolyWarClient();
    private static final Logger LOGGER = LogManager.getLogger();
    //    private final RenderTickCounter renderTickCounter = new RenderTickCounter(20.0f, 0L);
    private Thread thread;
    private boolean running = true;

    private MapView mapView = new MapView();
    //    private int fpsCounter;
    private JLabel fps = new JLabel();

    private PolyWarClient() {
    }

    public static PolyWarClient getInstance() {
        return instance;
    }

    public void run() {
        thread = Thread.currentThread();
        thread.setPriority(Thread.MAX_PRIORITY);
        display();
        while (running) {
            render();
        }
        stop();
    }

    private void render() {
        long timeFrameStart = Util.getMeasuringTimeMs();
        mapView.render();
        tick();
//        System.out.println(timeFrameStart + " " + Util.getMeasuringTimeMs());
        try {
            Thread.sleep(1000 / 60 - Util.getMeasuringTimeMs() + timeFrameStart);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        int i = this.renderTickCounter.beginRenderTick(System.nanoTime()/1000);
////        System.out.println(i);
//        for (int j = 0; j < Math.min(10, i); ++j) { // Why Math.min(10, i)?
//            tick();
//        }
//        mapView.render();
//        System.out.println("Render frame");

//        ++fpsCounter;
//        System.out.println(fpsCounter);
//        if (System.nanoTime()/1000 % 1000 == 0) {
//            fps.setText(String.valueOf(fpsCounter));
//            System.out.println(System.nanoTime()/1000 + " " + fpsCounter);
//            fpsCounter = 0;
//        }
    }

    private void tick() {
        mapView.tick();
        // TODO: InGameHUD
    }

    public void scheduleStop() {
        running = false;
    }

    private void stop() {
//        try {
        LOGGER.info("Stopping!");
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

    // Overrides of BasicWindow
    @Override
    public void initViews() {
        add(fps);
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
