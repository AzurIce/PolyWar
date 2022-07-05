package com.azurice.polywar.client.ui;

import com.azurice.polywar.client.ui.component.MapView;

public class MainPage extends AbstractPage {
    private static final int MAP_SIZE = 800;

    private MapView mapView = new MapView(MAP_SIZE);

    public MainPage() {
        mapView.display(this);
//        GamePage gamePage = new GamePage(MAP_SIZE);
//        add(gamePage);
//        Thread t = new Thread(gamePage::tick);
//        t.start();
    }

}
