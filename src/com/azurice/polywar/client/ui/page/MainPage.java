package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.PolyWarClient;
import com.azurice.polywar.client.ui.Layout.Row;
import com.azurice.polywar.client.ui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainPage extends PerformanceOverlayedPage {
    JButton btnStartGame;
    JButton btnCreateRoom;
    JButton btnJoinRoom;

    public MainPage(PolyWarClient client, MainWindow parent) {
        super(client, parent);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void initViews() {
        super.initViews();
        setPreferredSize(new Dimension(400, 400));
        setLayout(new FlowLayout());

        btnStartGame = new JButton("Start");
        btnCreateRoom = new JButton("Create Room");
        btnJoinRoom = new JButton("Join Room");

        Row row = new Row();
        row.add(btnCreateRoom);
        row.add(btnJoinRoom);
        row.setPreferredSize(new Dimension(800, 40));
        add(row);

        add(btnStartGame);
    }

    @Override
    public void initListeners() {
        super.initListeners();
        btnStartGame.addActionListener(e -> parent.setPage(MainWindow.GAME_PAGE));
        btnCreateRoom.addActionListener(e -> parent.setPage(MainWindow.CREATE_ROOM_PAGE));
    }

    ////// Key Listener //////
    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
