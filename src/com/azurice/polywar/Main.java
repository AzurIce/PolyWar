package com.azurice.polywar;

import com.azurice.polywar.client.ui.GamePage;

public class Main {
    private static final int MAP_SIZE = 800;
    public static void main(String[] args) {
//        MainPage mainPage = new MainPage();
//        mainPage.display();
        GamePage gamePage = new GamePage(MAP_SIZE);
        gamePage.display();
    }
}
