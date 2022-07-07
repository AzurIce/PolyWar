package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.util.Util;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static com.azurice.polywar.client.PolyWarClient.FRAME_RATE;
import static com.azurice.polywar.client.PolyWarClient.TICK_RATE;

/**
 * A JPanel with render and logic cycle
 */
public abstract class AbstractPage extends JPanel {
    protected MainWindow parent;
    private boolean running = true;
    private boolean stopped = false;

    public AbstractPage(MainWindow parent) {
        this.parent = parent;
        initViews();
        initListeners();
    }

    // Runtime control
    public void start() {
        new Thread(this::run).start();
    }

    public final void stop() {
        onStop();
    }

    public void stopAndWaitUntilStopped() {
        onStop();
        while (!isStopped()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        ;
    }

    private void run() {
        running = true;
        stopped = false;
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
        onStopped();
    }


    ////// Lifecycles //////
    /**
     * Lifecycle - onStop
     */
    public void onStop() {
        running = false;
    }

    /**
     * Lifecycle - onStopped
     */
    public void onStopped() {
        stopped = true;
    }


    // Render & tick
    // You have to impl these methods
    abstract void render();

    abstract void tick();



    public void initViews() {

    }

    public void initListeners() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                AbstractPage.this.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                AbstractPage.this.keyReleased(e);
            }
        });
    }

    ////// Getters & Setters //////

    public boolean isStopped() {
        return stopped;
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
