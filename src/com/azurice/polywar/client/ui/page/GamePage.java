package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.client.ui.component.MapView;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePage extends AbstractPage {
//    private static PolyWarClient client;
//    public GamePage(PolyWarClient client) {
//        this.client = client;
//    }


    // Views
    private MapView mapView;

    public GamePage(MainWindow parent) {
        super(parent);
    }

    @Override
    void render() {
//        System.out.println("GamePage - render");
        mapView.render();
    }

    @Override
    void tick() {
//        System.out.println("GamePage - tick");
        mapView.tick();
    }


    // Overrides of AbstractView
    @Override
    public void initViews() {
        System.out.println("GamePage-initViews");
        setPreferredSize(new Dimension(800, 800));

        mapView = new MapView();

        setFocusable(true);
        add(mapView);
    }

//    @Override
//    public void initListeners() {
//        addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                GamePage.this.keyPressed(e);
//            }
//            @Override
//            public void keyReleased(KeyEvent e) {
//                GamePage.this.keyReleased(e);
//            }
//        });
//    }

    public void keyPressed(KeyEvent e) {
//        System.out.println("Pressed: " + e);
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            parent.setPage(MainWindow.MAIN_PAGE);
        } else {
            mapView.keyPressed(e);
        }
    }

    public void keyReleased(KeyEvent e) {
//        System.out.println("Released: " + e);
        mapView.keyReleased(e);
    }
}
