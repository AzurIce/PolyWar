package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.PolyWarClient;
import com.azurice.polywar.client.ui.Layout.container.Row;
import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.client.ui.component.MapView;
import com.azurice.polywar.network.packet.ExitRoomPacket;
import com.azurice.polywar.server.Player;
import com.azurice.polywar.server.Room;
import com.azurice.polywar.world.WorldMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class RoomPage extends PerformanceOverlayedPage {

    private MainWindow.Page fromPage = MainWindow.Page.MAIN_PAGE;

    private JButton btnEsc;
    private JScrollPane scrollPane;
    private JList<Player> playerList;
    private MapView mapView;
    private Player owner;

    private WorldMap map;

    public RoomPage(PolyWarClient client, MainWindow parent) {
        super(client, parent);
    }

    public void updateRoom(Room room) {
        // Map
        // Player
        updatePlayerList(room.players);
        this.owner = room.owner;
        this.map = room.map;
        mapView.setMap(room.map);
        // repaint(); // ?
    }

    public void updatePlayerList(List<Player> players) {
        playerList.setModel(ListModel.from(players));
    }

    @Override
    public void initViews() {
        super.initViews();
        setLayout(new BorderLayout());

        btnEsc = new JButton("Back(Esc)");
        Row topContainer = new Row(Row.Alignment.START);
        topContainer.add(btnEsc);

        mapView = new MapView(map);
        add(mapView, BorderLayout.CENTER);

        playerList = new JList<>();
        playerList.setCellRenderer(new CellRender());
        scrollPane = new JScrollPane(playerList);

//        btnRefresh = new JButton("Refresh");
//        btnJoin = new JButton("Join");
//        Row bottomContainer = new Row();
//        bottomContainer.add(btnRefresh);
//        bottomContainer.add(btnJoin);

        add(topContainer, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.EAST);
//        add(bottomContainer, BorderLayout.SOUTH);
    }

    @Override
    public void initListeners() {
        super.initListeners();
        btnEsc.addActionListener((e) -> parent.client.sendPacket(new ExitRoomPacket()));

        // Bad Swing focus problem......
        btnEsc.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                RoomPage.this.keyPressed(e);
            }
        });
    }

    public MainWindow.Page getFromPage() {
        return fromPage;
    }

    public void setFromPage(MainWindow.Page fromPage) {
        this.fromPage = fromPage;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            parent.client.sendPacket(new ExitRoomPacket());
//            parent.setPage(fromPage);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    private static class ListModel extends DefaultListModel<Player> {
        private java.util.List<Player> players = new ArrayList<>();

        private ListModel(java.util.List<Player> playerList) {
            players = java.util.List.of(playerList.toArray(new Player[0]));
        }

        public static ListModel from(List<Player> playerList) {
            return new ListModel(playerList);
        }

        @Override
        public int getSize() {
            return players.size();
        }

        @Override
        public Player getElementAt(int index) {
            return players.get(index);
        }
    }

    private class CellRender extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
//            setText(value.toString());
            Player player = (Player) value;
            setText("Player " + player.id + (RoomPage.this.owner == player ? "owner" : ""));
            return this;
        }
    }
}
