package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.ui.Layout.container.Row;
import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.network.packet.GetRoomListPacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class RoomListPage extends AbstractPage {


    JButton btnEsc;
    JScrollPane scrollPane;
    JButton btnRefresh;
    JButton btnJoin;

    public RoomListPage(MainWindow parent) {
        super(parent);
    }

    @Override
    public void initViews() {
        super.initViews();
        setLayout(new BorderLayout());

        btnEsc = new JButton("Back(Esc)");
        Row topContainer = new Row(Row.Alignment.START);
        topContainer.add(btnEsc);

        scrollPane = new JScrollPane();

        btnRefresh = new JButton("Refresh");
        btnJoin = new JButton("Join");
        Row bottomContainer = new Row();
        bottomContainer.add(btnRefresh);
        bottomContainer.add(btnJoin);

        add(topContainer, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomContainer, BorderLayout.SOUTH);
    }

    @Override
    public void initListeners() {
        super.initListeners();
        btnEsc.addActionListener((e) -> parent.setPage(MainWindow.Page.MAIN_PAGE));
        btnRefresh.addActionListener((e) -> parent.client.sendPacket(new GetRoomListPacket()));
        btnJoin.addActionListener((e) -> {
            // TODO: JoinROom Packet
        });

        // Bad Swing focus problem......
        btnEsc.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                RoomListPage.this.keyPressed(e);
            }
        });
        btnRefresh.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                RoomListPage.this.keyPressed(e);
            }
        });
        btnJoin.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                RoomListPage.this.keyPressed(e);
            }
        });
    }

    @Override
    public void onShow() {
        parent.client.sendPacket(new GetRoomListPacket());
    }

    @Override
    public void render() {

    }

    @Override
    public void tick() {

    }

    ////// Key Listener //////
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            parent.setPage(MainWindow.Page.MAIN_PAGE);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
