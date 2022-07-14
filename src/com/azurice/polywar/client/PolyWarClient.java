package com.azurice.polywar.client;

import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.network.Util;
import com.azurice.polywar.network.data.GameOverData;
import com.azurice.polywar.network.data.GamePlayerData;
import com.azurice.polywar.network.data.MissileData;
import com.azurice.polywar.network.packet.*;
import com.azurice.polywar.server.Room;
import com.azurice.polywar.world.WorldMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;

import static com.azurice.polywar.util.Util.getMeasuringTimeMs;

public class PolyWarClient {
//    private static final String SERVER_IP = "127.0.0.1";
private static final String SERVER_IP = "114.116.241.137";
    private static final int SERVER_PORT = 7777;
    private static final InetSocketAddress SERVER_ADDRESS = new InetSocketAddress(SERVER_IP, SERVER_PORT);
    public static int TICK_RATE = 20;
    public static int FRAME_RATE = 144;

    private static final Logger LOGGER = LogManager.getLogger("PolyWarClient");

    // Singleton
    private static final PolyWarClient instance = new PolyWarClient();

    public static PolyWarClient getInstance() {
        return instance;
    }

    private PolyWarClient() {
    }


    public WorldMap worldMap;


    public double fps;
    public double ms;
    private boolean running = true;
    SocketChannel socketChannel;
    public MainWindow window;
    private boolean connected = false;
    public boolean nameValid = false;
    public String name = "Player";
    public int id;

    public boolean isNameValid() {
        return nameValid;
    }

    public boolean isConnected() {
        return connected;
    }

    Selector selector;
    private long timePing;
    private long timePong;

    // Runtime control
    public void run() {
        window = new MainWindow(getInstance());
        window.display();

        try {
            LOGGER.info("Initializing network......");
            selector = Selector.open();
        } catch (IOException e) {
            LOGGER.error("Network initialize failed", e);
            running = false;
        }

        Thread pingThread = new Thread(() -> {
            while (running) {
                if (connected) {
                    try {
                        timePing = getMeasuringTimeMs();
                        Util.sendPacket(socketChannel, new PingPacket());
                    } catch (IOException e) {
                        LOGGER.error("Ping error: ", e);
                        connected = false;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        pingThread.start();

        // Network cycle
        LOGGER.info("Handling connections...");
        while (running) {
            if (!connected) { // Connect to server
                try {
//                    LOGGER.info("Connecting to Server...");
                    socketChannel = SocketChannel.open();
                    socketChannel.connect(SERVER_ADDRESS);
                    connected = true;
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    LOGGER.info("Connected");
                    sendPacket(NamePacket.of(window.mainPage.textName.getText()));
                } catch (IOException e) {
//                    LOGGER.error("Server connect failed: {}, retrying...", e.toString());
                }
            } else {
                try {
                    selector.select();
                } catch (IOException e) {
                    LOGGER.error("Select failed: ", e);
                    continue;
                }

                Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                    SelectionKey key = it.next();
                    it.remove();

                    if (key.isReadable()) { // OP_READ
                        handleRead(key);
                    }
                }
            }
        }

        LOGGER.info("Closing...");
        if (socketChannel != null) {
            try {
                LOGGER.info("Closing SocketChannel...");
                socketChannel.close();
            } catch (IOException e) {
                LOGGER.error("SocketChannel close failed: {}", e.toString());
            }
        }
        onStopped();
    }

    private void handleRead(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            Packet packet = Util.readPacket(socketChannel);

            switch (packet) {
                case PlayerPacket p -> {
                    nameValid = true;
                    id = p.getData().id;
                    name = p.getData().name;
                }
                case GameOverDataListPacket p -> {
                    window.gameOverDataListPage.updateGameOverDataList(p.getData());
                    window.setPage(MainWindow.Page.GAME_OVER_DATA_LIST_PAGE);
                }
                case NameInValidPacket p -> nameValid = false;
                case PingPacket p -> handlePing();
                case RoomListPacket p -> window.roomListPage.updateRoomList(p.getData());
                case RoomPacket p -> handleRoom(p.getData());
                case ExitRoomPacket p -> window.setPage(window.roomPage.getFromPage());
                case PlayerListPacket p -> window.roomPage.updatePlayerList(p.getData());
                case MapPacket p -> handleMap(p.getData());
                case GamePlayerDataPacket p -> handleGamePlayerData(p.getData());
                case GamePlayerDataListPacket p -> window.gamePage.gameView.updateGamePlayersData(p.getData());
                case MissileDataListPacket p -> handleMissileDataList(p.getData());
                case GameOverPacket p -> handleGameOver(p.getData());
                case MapRadiusPacket p -> handleMapRadius(p.getData());
                case RoomFinishPlayingPacket p -> {
                    window.roomPage.playing = false;
                }
                default -> {
                }
            }
        } catch (IOException e) {
            LOGGER.error("Read failed: ", e);
            key.cancel();
            window.setPage(MainWindow.Page.MAIN_PAGE);
            connected = false;
            LOGGER.error("Canceled key");
        }
    }

    /**
     * Join room success
     *
     * @param room The room joined
     */
    private void handleRoom(Room room) {
        window.roomPage.updateRoom(room);
        handleMap(room.map);
        window.setPage(MainWindow.Page.ROOM_PAGE);
    }

    /**
     * GameStart
     *
     * @param gamePlayerData - The data of the main player(you)
     */
    private void handleGamePlayerData(GamePlayerData gamePlayerData) {
        window.gamePage.gameView.setMainGamePlayerData(gamePlayerData);
        window.setPage(MainWindow.Page.GAME_PAGE);
        window.roomPage.playing = true;
    }

    /**
     * Ping pong
     */
    private void handlePing() {
        timePong = getMeasuringTimeMs();
        ms = timePong - timePing;
    }

    /**
     * Get a new map, update the map
     *
     * @param worldMap the new map
     */
    public void handleMap(WorldMap worldMap) {
        this.worldMap = worldMap;
        window.roomPage.updateMap(worldMap);
        window.gamePage.updateMap(worldMap);
    }

    public void handleMissileDataList(List<MissileData> missileDataList) {
        window.gamePage.gameView.updateMissilesData(missileDataList);
    }

    public void handleGameOver(GameOverData gameOverData) {
        LOGGER.info("GG");
        window.gamePage.gameOverData = gameOverData;
        window.gamePage.gameOver = true;
    }

    public void handleMapRadius(double r) {
        window.gamePage.gameView.setMapRadius(r);
    }


    public void stop() {
        running = false;
        LOGGER.info("Set running to false");
        selector.wakeup();
    }

    public void onStopped() {
        System.exit(0);
    }


    public void sendPacket(Packet packet) {
        try {
            Util.sendPacket(socketChannel, packet);
        } catch (IOException e) {
            LOGGER.error("Packet send failed: ", e);
            connected = false;
        }
    }
}
