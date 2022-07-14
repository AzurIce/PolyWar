package com.azurice.polywar.client.ui;

import com.azurice.polywar.Renderable;
import com.azurice.polywar.Tickable;
import com.azurice.polywar.client.PolyWarClient;
import com.azurice.polywar.client.ui.page.*;
import com.azurice.polywar.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.azurice.polywar.client.PolyWarClient.TICK_RATE;

public class MainWindow extends JFrame implements Tickable, Renderable {
    private static final Logger LOGGER = LogManager.getLogger(MainWindow.class);
    private AbstractPage curPage;

    public PolyWarClient client;
    private CardLayout cardLayout = new CardLayout();
    private JPanel pageContainer = new JPanel();

    public RoomListPage roomListPage;

    // Pages
    public MainPage mainPage;
    public RoomPage roomPage;
    public GamePage gamePage;
    public GameOverRecordListPage gameOverDataListPage;

    public void initViews() {
        setTitle("PolyWar");
        // Pages
        mainPage = new MainPage(client, this);
        gamePage = new GamePage(client, this);
        roomListPage = new RoomListPage(this);
        roomPage = new RoomPage(client, this);
        gameOverDataListPage = new GameOverRecordListPage(this);

        pageContainer.setLayout(cardLayout);
        pageContainer.add(Page.MAIN_PAGE.name(), mainPage);
        pageContainer.add(Page.GAME_PAGE.name(), gamePage);
        pageContainer.add(Page.ROOM_LIST_PAGE.name(), roomListPage);
        pageContainer.add(Page.ROOM_PAGE.name(), roomPage);
        pageContainer.add(Page.GAME_OVER_DATA_LIST_PAGE.name(), gameOverDataListPage);
//        pageContainer.setBorder(new LineBorder(new Color(0x00ff00)));

        pageContainer.setLocation(0, 0);

        setContentPane(pageContainer);
    }
    private boolean stopped = false;
    private boolean running = true;

    public MainWindow(PolyWarClient client) {
        super();
        this.client = client;
        initViews();
        initListeners();
    }

    public void render() {
        curPage.render();
    }

    public void tick() {
        curPage.tick();
    }

    public void setPage(Page page) {
        AbstractPage targetPage = switch (page) {
            case MAIN_PAGE -> mainPage;
            case GAME_PAGE -> gamePage;
            case ROOM_LIST_PAGE -> roomListPage;
            case ROOM_PAGE -> roomPage;
            case GAME_OVER_DATA_LIST_PAGE -> gameOverDataListPage;
        };
        if (curPage == targetPage) return;
        if (curPage != null) curPage.onExit();
        cardLayout.show(pageContainer, page.name());
        curPage = targetPage;
        curPage.requestFocusInWindow();
        curPage.onShow();
        LOGGER.info("SetPage: " + page.name());
    }

    public void display() {
        setPage(Page.MAIN_PAGE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        start();
    }


    public void initListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                System.out.println(e);
                onClosing();
                super.windowClosing(e);
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e);
                super.keyPressed(e);
                curPage.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println(e);
                super.keyReleased(e);
                curPage.keyReleased(e);
            }
        });
    }

    /**
     * Lifecycle - onClosing
     */
    public void onClosing() {
        stopAndWaitUntilStopped();
        client.stop();
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
                } catch (IllegalArgumentException ignored) {
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        logicThread.start();

        while (running) {
            long timeFrameStart = Util.getMeasuringTimeMs();
            render();
//            try {
//                Thread.sleep(1);
//                Thread.sleep(1000 / FRAME_RATE - Util.getMeasuringTimeMs() + timeFrameStart);
//            } catch (IllegalArgumentException ignored) {
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            long timeFrameEnd = Util.getMeasuringTimeMs();
            client.fps = 1E3 / (timeFrameEnd - timeFrameStart);
        }
        onStopped();
    }


    // Runtime control
    public void start() {
        new Thread(this::run).start();
    }

    public final void stop() {
        onStop();
    }

    public void stopAndWaitUntilStopped() {
        LOGGER.info("Waiting for window to stop...");
        stop();
        while (!isStopped()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        LOGGER.info("Window stopped");
    }

    private boolean isStopped() {
        return stopped;
    }

    public enum Page {
        MAIN_PAGE,
        GAME_PAGE,
        ROOM_LIST_PAGE,
        ROOM_PAGE,
        GAME_OVER_DATA_LIST_PAGE
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

}
