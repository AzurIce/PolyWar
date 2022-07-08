package com.azurice.polywar.client;

import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        PolyWarClient polyWarClient = PolyWarClient.getInstance();
        polyWarClient.run();
    }
}
