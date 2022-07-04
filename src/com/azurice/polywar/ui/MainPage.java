package com.azurice.polywar.ui;

import com.azurice.polywar.ui.component.MapView;

import javax.swing.*;

public class MainPage extends JFrame {
    private static final int MAP_SIZE = 400;

    public MainPage() {
        MapView mapView = new MapView(MAP_SIZE);
        add(mapView);
    }

    public void display() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

}
