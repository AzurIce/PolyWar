package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.PolyWarClient;
import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.client.ui.component.GameView;
import com.azurice.polywar.network.data.GameOverData;
import com.azurice.polywar.world.WorldMap;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePage extends PerformanceOverlayedPage {
    // Views
    public GameView gameView;
    public boolean gameOver = false;
    public GameOverData gameOverData;

    private static Player bgm;

    private void initBGM() throws JavaLayerException {
        bgm = new Player(GamePage.class.getResourceAsStream("/sounds/Goldmund - The Wind Wings.mp3"));
    }


    public GamePage(PolyWarClient client, MainWindow parent) {
        super(client, parent);
    }

    public void updateMap(WorldMap worldMap) {
        gameView.setMap(worldMap);
    }


    @Override
    public void onShow() {
        super.onShow();
        new Thread(() -> {
            try {
                initBGM();
                bgm.play();
            } catch (JavaLayerException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public void onExit() {
        super.onExit();
        bgm.close();
    }


    @Override
    public void render() {
        gameView.render();
        super.render();
    }

    @Override
    public void paintOverlay(Graphics g) {
        super.paintOverlay(g);
        if (gameOver) {
            g.drawString("You died, press Esc to exit", 400, 400);
            g.drawString(gameOverData.toString(), 400, 414);
        }
    }

    @Override
    public void tick() {
        if (!gameOver) {
            gameView.tick();
        }
    }


    // Overrides of AbstractView
    @Override
    public void initViews() {
        setPreferredSize(new Dimension(800, 800));
        setFocusable(true);

        gameView = new GameView();

        add(gameView);
    }


    ////// KeyListener //////
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            parent.setPage(MainWindow.Page.ROOM_PAGE);
            gameOver = false;
        } else {
            gameView.keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        gameView.keyReleased(e);
    }
}
