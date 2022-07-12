package com.azurice.polywar.server;

import com.azurice.polywar.network.Util;
import com.azurice.polywar.network.packet.*;
import com.azurice.polywar.server.database.DatabaseHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class PolyWarServer {
    ////// Constants //////
    private static final InetSocketAddress LISTEN_ADDRESS = new InetSocketAddress(7777);
    private static final Logger LOGGER = LogManager.getLogger("PolyWarServer");

    private static final int TICK_RATE = 20;


    ///// Singleton //////
    private static PolyWarServer instance = new PolyWarServer();
    ////// Properties //////
    ServerSocketChannel serverSocketChannel;
    Selector selector;

    List<Room> rooms = Collections.synchronizedList(new ArrayList<>());
    List<Integer> deletedRoomIds = Collections.synchronizedList(new ArrayList<>());
    Map<Player, Room> playersToRooms = Collections.synchronizedMap(new HashMap<>());
    Map<SocketChannel, Player> socketsToPlayers = Collections.synchronizedMap(new HashMap<>());
    List<Integer> deletedPlayerIds = Collections.synchronizedList(new ArrayList<>());
    Map<SocketChannel, SelectionKey> socketsToKeys = Collections.synchronizedMap(new HashMap<>());
    public static final DatabaseHelper database = DatabaseHelper.getInstance();
    Map<SocketChannel, Integer> socketToPlayerId = Collections.synchronizedMap(new HashMap<>());
    Map<Integer, Room> playerIdToRoom = Collections.synchronizedMap(new HashMap<>());

    private PolyWarServer() {
    }

    private boolean running = true;

    public static PolyWarServer getInstance() {
        return instance;
    }

    public void tick() {
//        LOGGER.info("Ticking rooms: {}", rooms);
        for (int i = 0; i < rooms.size(); i++) {
            rooms.get(i).tick();
        }
    }

    public void run() {
        this.running = true;

        initNetwork(); // Initialize Selector and ServerSocketChannel

        startTick(); // Tick cycle


        // Network cycle
        LOGGER.info("Handling connections...");
        while (running) {
//            LOGGER.info("Selecting...");
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

                if (key.isAcceptable()) { // OP_ACCEPT
                    handleAccept((ServerSocketChannel) key.channel());
                } else if (key.isReadable() & key.isWritable()) { // OP_READ | OP_WRITE
                    handleRead(key);
                }
            }
        }

        if (serverSocketChannel != null) {
            try {
                serverSocketChannel.close();
            } catch (IOException e) {
                LOGGER.error("ServerSocketChannel close failed: ", e);
            }
        }
    }

    public void handleAccept(ServerSocketChannel serverSocketChannel) {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            LOGGER.info("Accepted Client<{}>, registering...", socketChannel);
            socketChannel.configureBlocking(false);
            SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            socketsToKeys.put(socketChannel, key);
        } catch (IOException e) {
            LOGGER.error("Accept failed: ", e);
        }
    }

    public void handleRead(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            Packet packet = Util.readPacket(socketChannel);

            switch (packet) {
                case NamePacket p -> handleName(socketChannel, p.getData());
                case PingPacket p -> handlePing(socketChannel);
                case GetRoomListPacket p -> handleGetRoomList(socketChannel);
                case CreateRoomPacket p -> handleCreateRoom(socketChannel);
                case RoomPacket p -> handleJoinRoom(socketChannel, p.getData());
                case ExitRoomPacket p -> handleExitRoom(socketChannel);
                case RegenerateMapPacket p -> {
                    int playerId = socketToPlayerId.get(socketChannel);
                    Room room = playerIdToRoom.get(playerId);
                    LOGGER.info("Regenerating WorldMap...");
                    room.regenerateMap();
                    LOGGER.info("Sending MapPacket...");
                    sendRoomMapInfo(room);
                }
                case StartGamePacket p -> {
                    int playerId = socketToPlayerId.get(socketChannel);
                    Room room = playerIdToRoom.get(playerId);
                    room.startGame();
                }
                case GamePlayerControlDataPacket p -> {
                    int playerId = socketToPlayerId.get(socketChannel);
                    Room room = playerIdToRoom.get(playerId);
                    room.updatePlayerControlDataById(playerId, p.getData());
                }
                default -> {
                }
            }
        } catch (IOException e) {
            LOGGER.error("Read failed: ", e);
            handlePlayerLogout(key);
        }
    }

    //--------------------------------------------------------------------//

    private void handleName(SocketChannel socketChannel, String name) {
        int id = database.updatePlayer(name);

        if (!socketToPlayerId.containsValue(id)) {
            socketToPlayerId.put(socketChannel, id);
        }
        if (socketToPlayerId.get(socketChannel) == null || socketToPlayerId.get(socketChannel) != id) {
            sendPacket(socketChannel, new NameInValidPacket());
        } else {
            sendPacket(socketChannel, NamePacket.of(name));
        }
    }

    private void handlePing(SocketChannel socketChannel) {
        sendPacket(socketChannel, new PingPacket());
    }

    private void handleGetRoomList(SocketChannel socketChannel) {
        sendPacket(socketChannel, RoomListPacket.of(rooms));
    }

    private void handleCreateRoom(SocketChannel socketChannel) throws IOException {
        int playerId = socketToPlayerId.get(socketChannel);

        Room room;
        if (deletedRoomIds.size() == 0) {
            room = new Room(rooms.size(), new Player(playerId, socketChannel));
        } else {
            room = new Room(deletedRoomIds.get(0), new Player(playerId, socketChannel));
            deletedRoomIds.remove(0);
        }
        rooms.add(room);

        playerIdToRoom.put(playerId, room);
        sendPacket(socketChannel, RoomPacket.of(room));
    }

    private void handleJoinRoom(SocketChannel socketChannel, Room room) throws IOException {
        room = getRoomById(room.id);
        if (room == null) return;

        int playerId = socketToPlayerId.get(socketChannel);
        room.addPlayer(new Player(playerId, socketChannel));
        playerIdToRoom.put(playerId, room);

        room.sendPlayerListPacket();
        sendPacket(socketChannel, RoomPacket.of(room));
    }

    private void handleExitRoom(SocketChannel socketChannel) throws IOException {
        int playerId = socketToPlayerId.get(socketChannel);
        Room room = playerIdToRoom.get(playerId);

        room.removePlayerById(playerId);
        playerIdToRoom.remove(playerId);

        room.sendPlayerListPacket();
        sendPacket(socketChannel, new ExitRoomPacket());
    }

    //--------------------------------------------------------------------//

    public void handlePlayerLogout(SocketChannel socketChannel) {
        this.handlePlayerLogout(socketsToKeys.get(socketChannel));
    }

    public void handlePlayerLogout(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        key.cancel();
        socketsToKeys.remove(socketChannel);
        LOGGER.error("Canceled key");


        int playerId = socketToPlayerId.get(socketChannel);
        socketToPlayerId.remove(socketChannel);

        // If the player is in a room, remove the player
        Room room = playerIdToRoom.get(playerId);
        if (room != null) { // This player is still in room
            room.removePlayerById(playerId);
            playerIdToRoom.remove(playerId);
        }
    }


    public void dismissRoom(Room room) {
        for (int i = 0; i < room.playerList.size(); i++) {
            sendPacket(room.playerList.get(i).socketChannel, new ExitRoomPacket());
        }
        rooms.remove(room);
    }


    public void sendPacket(SocketChannel socketChannel, Packet packet) {
        try {
            Util.sendPacket(socketChannel, packet);
        } catch (IOException e) {
            LOGGER.error("Write failed: ", e);
            handlePlayerLogout(socketChannel);
        }
    }


    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    private void sendRoomMapInfo(Room room) throws IOException {
        for (int i = 0; i < room.playerList.size(); i++) {
            Util.sendPacket(room.playerList.get(i).socketChannel, MapPacket.of(room.map));
        }
    }


    private Room getRoomById(int id) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).id == id) {
                return rooms.get(i);
            }
        }
        return null;
    }


    private void initNetwork() {
        LOGGER.info("Initializing network......");
        try {
            selector = Selector.open();

            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(LISTEN_ADDRESS);

            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            LOGGER.error("Network initialize failed", e);
            running = false;
        }
    }

    public void startTick() {
        LOGGER.info("Starting ticking thread...");
        Thread tickThread = new Thread(() -> {
            while (running) {
                long timeTickStart = com.azurice.polywar.util.Util.getMeasuringTimeMs();
//                LOGGER.info("Server ticking...");
                tick();
                long timeTickFinished = com.azurice.polywar.util.Util.getMeasuringTimeMs();
                try {
                    Thread.sleep(Math.max(0, 1000 / TICK_RATE - timeTickFinished + timeTickStart));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        tickThread.start();
    }
}
