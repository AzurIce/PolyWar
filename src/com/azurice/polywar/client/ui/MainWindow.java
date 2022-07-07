package com.azurice.polywar.client.ui;

import com.azurice.polywar.client.PolyWarClient;
import com.azurice.polywar.client.ui.page.AbstractPage;
import com.azurice.polywar.client.ui.page.GamePage;
import com.azurice.polywar.client.ui.page.MainPage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainWindow extends JFrame {
    public static final String MAIN_PAGE = "MainPage";
    public static final String GAME_PAGE = "GamePage";

    private AbstractPage curPage;

    private JPanel container = new JPanel();
    private CardLayout cardLayout = new CardLayout();
    private MainPage mainPage;
    private GamePage gamePage;


    private PolyWarClient client;

    public MainWindow(PolyWarClient client) {
        super();
        initViews();
        initListeners();
        this.client = client;
    }

    public void initViews() {
        mainPage = new MainPage(this);
        gamePage = new GamePage(this);

        container.setLayout(cardLayout);
        container.add(MAIN_PAGE, mainPage);
        container.add(GAME_PAGE, gamePage);
        cardLayout.show(container, MAIN_PAGE);

        setContentPane(container);
    }

    public void setPage(String pageName) {
        if (curPage != null) {
            curPage.stopAndWaitUntilStopped();
        }

        cardLayout.show(container, pageName);
        curPage = switch (pageName) {
            case MAIN_PAGE -> mainPage;
            case GAME_PAGE -> gamePage;
            default -> null; // Never happens
        };
//        curPage.requestFocus();
        curPage.requestFocusInWindow();
        curPage.start();
        System.out.println("[MainWindow/setPage]: " + pageName + " " + curPage);
    }


    public void initListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                System.out.println(e);
                onClosing();
                super.windowClosing(e);
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e);
                super.keyPressed(e);
                curPage.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println(e);
                super.keyReleased(e);
                curPage.keyReleased(e);
            }
        });
    }

    /**
     * Lifecycle - onClosing
     */
    public void onClosing() {
        curPage.stopAndWaitUntilStopped();
        System.exit(0);
    }

    public void display() {
//        System.out.println("display");
        setPage(MAIN_PAGE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
