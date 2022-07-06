package com.azurice.polywar.client.ui;

import com.azurice.polywar.client.ui.component.MapView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainPage extends BasicWindow {

    private MapView mapView = new MapView();

    public MainPage() {
        setContentPane(mapView);
//        mapView.display(this);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                mapView.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                mapView.keyReleased(e);
            }
        });
//        GamePage gamePage = new GamePage(MAP_SIZE);
//        add(gamePage);
//        Thread t = new Thread(gamePage::tick);
//        t.start();
    }

}
