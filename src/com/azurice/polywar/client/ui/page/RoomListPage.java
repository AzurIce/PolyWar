package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.ui.Layout.container.Row;
import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.network.packet.GetRoomListPacket;
import com.azurice.polywar.server.Room;
import com.azurice.polywar.util.MyColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class RoomListPage extends AbstractPage {


    JButton btnEsc;
    JScrollPane scrollPane;
    JList<Room> roomList;
    JButton btnRefresh;
    JButton btnJoin;

    public RoomListPage(MainWindow parent) {
        super(parent);
    }

    public void updateRoomList(List<Room> rooms) {
        roomList.setModel(new ListModel(rooms));
    }

    @Override
    public void initViews() {
        super.initViews();
        setLayout(new BorderLayout());

        btnEsc = new JButton("Back(Esc)");
        Row topContainer = new Row(Row.Alignment.START);
        topContainer.add(btnEsc);

        roomList = new JList<>();
        roomList.setCellRenderer(new CellRender());
        scrollPane = new JScrollPane(roomList);

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
        scrollPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                RoomListPage.this.keyPressed(e);
            }
        });
        roomList.addKeyListener(new KeyAdapter() {
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

    private static class CellRender extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//            setText(value.toString());
            Room room = (Room) value;
            setText("Room " + room.id + "    Players: " + room.players.size());
            if (isSelected) {
                setBackground(new Color(MyColor.BLUE | 0x77 << 24, true));
            } else {
                setBackground(new Color(0xfcfcfc));
            }
            return this;
        }
    }

    private static class ListModel extends DefaultListModel<Room> {
        private List<Room> rooms = new ArrayList<>();

        private ListModel(List<Room> roomList) {
            rooms = List.of(roomList.toArray(new Room[0]));
        }

        public ListModel from(List<Room> roomList) {
            return new ListModel(roomList);
        }

        @Override
        public int getSize() {
            return rooms.size();
        }

        @Override
        public Room getElementAt(int index) {
            return rooms.get(index);
        }
    }
}