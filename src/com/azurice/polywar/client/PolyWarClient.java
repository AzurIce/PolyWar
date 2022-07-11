package com.azurice.polywar.client;

import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.network.Util;
import com.azurice.polywar.network.data.GamePlayerData;
import com.azurice.polywar.network.packet.*;
import com.azurice.polywar.server.Player;
import com.azurice.polywar.server.Room;
import com.azurice.polywar.world.WorldMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PolyWarClient {
    private static final String SERVER_IP = "127.0.0.1";
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

    public Room curRoom;
    public List<Room> roomList = new ArrayList<>();
    SocketChannel socketChannel;
    private MainWindow window;
    private boolean connected = false;

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
                        timePing = com.azurice.polywar.util.Util.getMeasuringTimeMs();
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

            if (packet instanceof PingPacket) {
                timePong = com.azurice.polywar.util.Util.getMeasuringTimeMs();
                ms = timePong - timePing;
            } else {
//                LOGGER.info("Received {}: {}", packet.getTypeString(), packet.toString());
                if (packet instanceof RoomListPacket) {
                    roomList = ((RoomListPacket) packet).getRoomList();
                    window.roomListPage.updateRoomList(roomList);
//                    LOGGER.info("Updated roomList, {} Rooms: {}", roomList.size(), roomList.toString());
                } else if (packet instanceof RoomPacket) {
                    curRoom = ((RoomPacket) packet).getRoom();
                    window.roomPage.updateRoom(curRoom);
                    updateWorldMap(curRoom.map);
//                    LOGGER.info("Updated curRoom: {}", curRoom);
                    window.setPage(MainWindow.Page.ROOM_PAGE);
                } else if (packet instanceof ExitRoomPacket) {
//                    LOGGER.info("Exited room");
                    window.setPage(window.roomPage.getFromPage());
                } else if (packet instanceof PlayerListPacket) {
                    List<Player> playerList = ((PlayerListPacket) packet).getPlayerList();
                    window.roomPage.updatePlayerList(playerList);
//                    LOGGER.info("Updated playerList: {}", playerList);
                } else if (packet instanceof MapPacket) {
                    WorldMap map = ((MapPacket) packet).getMap();
                    updateWorldMap(map);
//                    LOGGER.info("Updated map: {}", map);
                } else if (packet instanceof GamePlayerDataPacket) {
                    GamePlayerData gamePlayerData = ((GamePlayerDataPacket) packet).getGamePlayerData();
                    window.gamePage.gameView.setMainGamePlayerData(gamePlayerData);
//                    LOGGER.info("Updated mainGamePlayer: {}", gamePlayerData);
                    window.setPage(MainWindow.Page.GAME_PAGE);
                } else if (packet instanceof GamePlayerDataListPacket) {
                    List<GamePlayerData> gamePlayerDataList = ((GamePlayerDataListPacket) packet).getGamePlayerDataList();
                    window.gamePage.gameView.updateGamePlayersData(gamePlayerDataList);
                }/* else if (packet instanceof MissileListPacket) {

                } else if (packet instanceof EndGamePacket) {

                }*/
            }
        } catch (IOException e) {
            LOGGER.error("Read failed: ", e);
            key.cancel();
            window.setPage(MainWindow.Page.MAIN_PAGE);
            connected = false;
            LOGGER.error("Canceled key");
        }
    }


    public void updateWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
        window.roomPage.updateMap(worldMap);
        window.gamePage.updateMap(worldMap);
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
//            throw new RuntimeException(e);
        }
    }
}
