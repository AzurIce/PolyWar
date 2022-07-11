package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.PolyWarClient;
import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.client.ui.component.GameView;
import com.azurice.polywar.world.WorldMap;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePage extends PerformanceOverlayedPage {
    // Views
    public GameView gameView;

    public GamePage(PolyWarClient client, MainWindow parent) {
        super(client, parent);
    }

    public void updateMap(WorldMap worldMap) {
        gameView.setMap(worldMap);
    }


    @Override
    public void render() {
        gameView.render();
        super.render();
    }

    @Override
    public void tick() {
        gameView.tick();
        super.tick();
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
            parent.setPage(MainWindow.Page.MAIN_PAGE);
        } else {
            gameView.keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        gameView.keyReleased(e);
    }
}
