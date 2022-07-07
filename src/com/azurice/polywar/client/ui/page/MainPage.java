package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.ui.Layout.Row;
import com.azurice.polywar.client.ui.MainWindow;

import javax.swing.*;
import java.awt.*;

public class MainPage extends AbstractPage {
    JButton btnStartGame;
    JButton btnCreateRoom;
    JButton btnJoinRoom;

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
}
