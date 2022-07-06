package com.azurice.polywar.client;

public class Main {
    public static void main(String[] args) {
        Thread.currentThread().setName("Thread-Renderer");
        PolyWarClient polyWarClient = PolyWarClient.getInstance();

        polyWarClient.run();
    }
}
