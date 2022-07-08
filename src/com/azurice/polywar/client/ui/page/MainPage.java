package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.PolyWarClient;
import com.azurice.polywar.client.ui.Layout.VerticalFlowLayout;
import com.azurice.polywar.client.ui.Layout.container.Row;
import com.azurice.polywar.client.ui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainPage extends PerformanceOverlayedPage {
    private static final Image imageBg;

    static {
        java.net.URL imageURL = MainPage.class.getResource("/images/main-bg.png");
        imageBg = new ImageIcon(imageURL).getImage();
    }

    JButton btnStartGame;
    JButton btnCreateRoom;
    JButton btnJoinRoom;
    JLabel labelTitle;

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
        btnCreateRoom.setEnabled(parent.client.isConnected());
        btnJoinRoom.setEnabled(parent.client.isConnected());
    }

    @Override
    public void paintBg(Graphics g) {
        g.drawImage(imageBg, 0, 0, null);
    }


    @Override
    public void initViews() {
        super.initViews();
        setOpaque(false);
        setLayout(new BorderLayout());

        JPanel rightPanel = new JPanel();
        VerticalFlowLayout layout = new VerticalFlowLayout();
        layout.setVAlign(VerticalFlowLayout.CENTER);
        rightPanel.setLayout(layout);
//        rightPanel.setOpaque(false);
        rightPanel.setBackground(new Color(0x55ffffff, true));
        rightPanel.setPreferredSize(new Dimension(310, 800));
//        rightPanel.setBorder(new LineBorder(new Color(0x0000ff)));


        JPanel row0 = new Row();
        row0.setOpaque(false);
        labelTitle = new JLabel("Poly War");
        labelTitle.setOpaque(false);
        labelTitle.setFont(new Font("default", Font.BOLD, 30));
        row0.add(labelTitle);
        rightPanel.add(row0);

        JPanel row1 = new Row();
        row1.setOpaque(false);
        row1.setPreferredSize(new Dimension(295, 50));
//        row1.setBorder(new LineBorder(new Color(0xff0000)));
        btnCreateRoom = new JButton("Create Room");
        btnJoinRoom = new JButton("Join Room");
        btnCreateRoom.setPreferredSize(new Dimension(140, 40));
        btnJoinRoom.setPreferredSize(new Dimension(140, 40));
        row1.add(btnCreateRoom);
        row1.add(btnJoinRoom);

        rightPanel.add(row1);

        JPanel row2 = new Row();
        row2.setOpaque(false);
        row2.setPreferredSize(new Dimension(295, 60));
//        row2.setBorder(new LineBorder(new Color(0xff0000)));
        btnStartGame = new JButton("TestStart");
        btnStartGame.setPreferredSize(new Dimension(285, 50));
        row2.add(btnStartGame);

        rightPanel.add(row2);

        add(rightPanel, BorderLayout.EAST);
    }

    @Override
    public void initListeners() {
        super.initListeners();
        btnStartGame.addActionListener(e -> parent.setPage(MainWindow.Page.GAME_PAGE));
        btnCreateRoom.addActionListener(e -> {
            // TODO: CreateRoom Packet
        });
        btnJoinRoom.addActionListener(e -> parent.setPage(MainWindow.Page.ROOM_LIST_PAGE));
    }

    ////// Key Listener //////
    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
