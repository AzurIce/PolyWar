package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.ui.Layout.container.Row;
import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.network.data.GameOverData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class GameOverRecordListPage extends PerformanceOverlayedPage {


    private JButton btnEsc;
    private JScrollPane scrollPane;
    private JList<GameOverData> gameOverRecordList;
    private JButton btnRefresh;
    private JButton btnJoin;

    public GameOverRecordListPage(MainWindow parent) {
        super(parent);
    }

    public void updateGameOverDataList(List<GameOverData> gameOverDataList) {
        gameOverRecordList.setModel(ListModel.from(gameOverDataList));
    }

    @Override
    public void initViews() {
        super.initViews();
        setLayout(new BorderLayout());

        btnEsc = new JButton("Back(Esc)");
        Row topContainer = new Row(Row.Alignment.START);
        topContainer.add(btnEsc);

        gameOverRecordList = new JList<>();
        gameOverRecordList.setCellRenderer(new GameOverRecordListPage.CellRender());
        scrollPane = new JScrollPane(gameOverRecordList);

        btnRefresh = new JButton("Refresh");
        btnJoin = new JButton("Join");
//        Row bottomContainer = new Row();
//        bottomContainer.add(btnRefresh);
//        bottomContainer.add(btnJoin);

        add(topContainer, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
//        add(bottomContainer, BorderLayout.SOUTH);
    }

    @Override
    public void initListeners() {
        super.initListeners();
        btnEsc.addActionListener((e) -> parent.setPage(MainWindow.Page.MAIN_PAGE));
//        btnRefresh.addActionListener((e) -> parent.client.sendPacket(new GetRoomListPacket()));
//        btnJoin.addActionListener((e) -> {
//            if (roomList.getSelectedValue() != null) {
//                parent.client.sendPacket(RoomPacket.of(roomList.getSelectedValue()));
//            }
//        });

        // Bad Swing focus problem......
        btnEsc.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                GameOverRecordListPage.this.keyPressed(e);
            }
        });
        scrollPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                GameOverRecordListPage.this.keyPressed(e);
            }
        });
        gameOverRecordList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                GameOverRecordListPage.this.keyPressed(e);
            }
        });
    }

    @Override
    public void onShow() {
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
            GameOverData gameOverData = (GameOverData) value;
            setText("Ranked " + gameOverData.rank +
                    " place, Shot " + gameOverData.shoot +
                    " times, Moved: " + gameOverData.distance + " px");
            return this;
        }
    }

    private static class ListModel extends DefaultListModel<GameOverData> {
        private List<GameOverData> gameOverDataList = new ArrayList<>();

        private ListModel(List<GameOverData> gameOverDataList) {
            this.gameOverDataList = List.of(gameOverDataList.toArray(new GameOverData[0]));
        }

        public static ListModel from(List<GameOverData> gameOverDataList) {
            return new ListModel(gameOverDataList);
        }

        @Override
        public int getSize() {
            return gameOverDataList.size();
        }

        @Override
        public GameOverData getElementAt(int index) {
            return gameOverDataList.get(index);
        }
    }
}