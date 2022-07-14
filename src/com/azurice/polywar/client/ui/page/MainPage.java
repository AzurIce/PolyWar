package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.PolyWarClient;
import com.azurice.polywar.client.ui.Layout.VerticalFlowLayout;
import com.azurice.polywar.client.ui.Layout.container.Row;
import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.network.packet.CreateRoomPacket;
import com.azurice.polywar.network.packet.GetGameOverDataListPacket;
import com.azurice.polywar.network.packet.NamePacket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainPage extends PerformanceOverlayedPage {
    private static final Image imageBg;

    static {
        java.net.URL imageURL = MainPage.class.getResource("/images/main-bg.png");
        imageBg = new ImageIcon(imageURL).getImage();
    }

    JButton btnRecord;
    JButton btnCreateRoom;
    JButton btnJoinRoom;
    JLabel labelTitle;

    public JTextField textName;
    JLabel labelYourName;
    JLabel labelName;
    JButton btnName;

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
        btnCreateRoom.setEnabled(parent.client.isConnected() && parent.client.isNameValid());
        btnJoinRoom.setEnabled(parent.client.isConnected() && parent.client.isNameValid());
        btnRecord.setEnabled(parent.client.isConnected() && parent.client.isNameValid());
        if (!parent.client.isNameValid()) {
            labelName.setText("Your Name is Not Available");
            labelName.setForeground(new Color(0xff0000));
        } else {
            labelName.setText(parent.client.name);
            labelName.setForeground(new Color(0x000000));
        }
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
        row1.setPreferredSize(new Dimension(295, 60));
        labelYourName = new JLabel("Your Name:");
        labelYourName.setOpaque(false);
        labelName = new JLabel("Player");
        labelName.setOpaque(false);
        row1.add(labelYourName);
        row1.add(labelName);
        rightPanel.add(row1);

        JPanel row2 = new Row();
        row2.setOpaque(false);
        row2.setPreferredSize(new Dimension(295, 60));
        textName = new JTextField("Player");
        textName.setOpaque(false);
        btnName = new JButton("Confirm");
        row2.add(textName);
        row2.add(btnName);
        rightPanel.add(row2);

        JPanel row3 = new Row();
        row3.setOpaque(false);
        row3.setPreferredSize(new Dimension(295, 50));
//        row3.setBorder(new LineBorder(new Color(0xff0000)));
        btnCreateRoom = new JButton("Create Room");
        btnJoinRoom = new JButton("Join Room");
        btnCreateRoom.setPreferredSize(new Dimension(140, 40));
        btnJoinRoom.setPreferredSize(new Dimension(140, 40));
        row3.add(btnCreateRoom);
        row3.add(btnJoinRoom);

        rightPanel.add(row3);

        JPanel row4 = new Row();
        row4.setOpaque(false);
//        row4.setPreferredSize(new Dimension(295, 60));
//        row4.setBorder(new LineBorder(new Color(0xff0000)));
        btnRecord = new JButton("Check record");
        row4.add(btnRecord);

        rightPanel.add(row4);

        add(rightPanel, BorderLayout.EAST);
    }

    @Override
    public void initListeners() {
        super.initListeners();
//        btnStartGame.addActionListener(e -> parent.setPage(MainWindow.Page.GAME_PAGE));
        btnCreateRoom.addActionListener(e -> parent.client.sendPacket(new CreateRoomPacket()));
        btnJoinRoom.addActionListener(e -> parent.setPage(MainWindow.Page.ROOM_LIST_PAGE));
        btnName.addActionListener(e -> {
            if (textName.getText().equals("")) {
                parent.client.nameValid = false;
            } else {
                parent.client.sendPacket(NamePacket.of(textName.getText()));
            }
        });
        btnRecord.addActionListener(e -> parent.client.sendPacket(new GetGameOverDataListPacket()));
    }

    ////// Key Listener //////
    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
