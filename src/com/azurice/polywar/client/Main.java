package com.azurice.polywar.client;

import com.azurice.polywar.client.ui.MainWindow;

public class Main {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        mainWindow.display();
//        Thread.currentThread().setName("Thread-Renderer");
//        PolyWarClient polyWarClient = PolyWarClient.getInstance();
//
//        polyWarClient.run();
    }
}
