package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.client.ui.component.MapView;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePage extends AbstractPage {
    // Views
    private MapView mapView;

    public GamePage(MainWindow parent) {
        super(parent);
    }

    @Override
    void render() {
        mapView.render();
    }

    @Override
    void tick() {
        mapView.tick();
    }


    // Overrides of AbstractView
    @Override
    public void initViews() {
        setPreferredSize(new Dimension(800, 800));
        setFocusable(true);

        mapView = new MapView();

        add(mapView);
    }


    ////// KeyListener //////
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            parent.setPage(MainWindow.MAIN_PAGE);
        } else {
            mapView.keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
        mapView.keyReleased(e);
    }
}
