package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.ui.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainPage extends AbstractPage {


    JButton btnStartGame;

    public MainPage(MainWindow parent) {
        super(parent);
    }

    @Override
    void render() {

    }

    @Override
    void tick() {

    }

    @Override
    public void initViews() {
        super.initViews();
        btnStartGame = new JButton("Start");
        add(btnStartGame);
    }

    @Override
    public void initListeners() {
        super.initListeners();
        btnStartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.setPage(MainWindow.GAME_PAGE);
            }
        });
    }
}
