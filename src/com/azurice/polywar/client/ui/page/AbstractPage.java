package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.util.Util;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * A JPanel with render and logic cycle
 */
public abstract class AbstractPage extends JPanel {
    public static final int TICK_RATE = 20;
    public static final int FRAME_RATE = 144;

    MainWindow parent;
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

    private void run() {
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
        onStopped();
    }

    /**
     * Call this method to stop the cycle
     */
    public final void scheduleStop() {
        running = false;
    }


    /**
     * Lifecycle - onStop
     */
    public void onStop() {
        scheduleStop();
    }

    /**
     * Lifecycle - onStopped
     */
    public void onStopped() {
        stopped = false;
    }


    // Render & tick
    // You have to impl these methods
    abstract void render();

    abstract void tick();

    ;


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
