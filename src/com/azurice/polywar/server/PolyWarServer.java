package com.azurice.polywar.server;

import com.azurice.polywar.network.Util;
import com.azurice.polywar.network.packet.*;
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

    private PolyWarServer() {
    }

    private boolean running = true;

    public static PolyWarServer getInstance() {
        return instance;
    }

    public void tick() {

    }

    public void run() {
        this.running = true;

        // Initialize Selector and ServerSocketChannel
        try {
            LOGGER.info("Initializing network......");
//            LOGGER.info("Creating Selector");
            selector = Selector.open();

//            LOGGER.info("Creating ServerSocketChannel...");
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(LISTEN_ADDRESS);

//            LOGGER.info("Registering ServerSocketChannel...");
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            LOGGER.error("Network initialize failed", e);
            running = false;
        }

        LOGGER.info("Starting ticking thread...");
        Thread tickThread = new Thread(() -> {
            while (running) {
                long timeTickStart = com.azurice.polywar.util.Util.getMeasuringTimeMs();
                tick();
                long timeTickFinished = com.azurice.polywar.util.Util.getMeasuringTimeMs();
                try {
                    Thread.sleep(Math.max(0, 1000 / 20 - timeTickFinished + timeTickStart));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        tickThread.start();


        LOGGER.info("Handling connections...");
        while (running) {
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
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
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
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            if (deletedPlayerIds.size() == 0) {
                socketsToPlayers.put(socketChannel, new Player(socketsToPlayers.size(), socketChannel));
            } else {
                socketsToPlayers.put(socketChannel, new Player(deletedPlayerIds.get(0), socketChannel));
                deletedPlayerIds.remove(0);
            }
            LOGGER.info("Created Player, total {} Players: {}", socketsToPlayers.size(), socketsToPlayers);
//            socketChannel.write(new PingPacket().toByteBuffer());
        } catch (IOException e) {
            LOGGER.error("Accept failed: ", e);
        }
    }

    public void handleRead(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            Packet packet = Util.readPacket(socketChannel);

            if (packet instanceof PingPacket) {
//                LOGGER.info("[{}] Sending Ping response", socketChannel.getRemoteAddress());
                socketChannel.write(new PingPacket().toByteBuffer());
            } else {
                LOGGER.info("[{}] Received {}: {}",
                        socketChannel.getRemoteAddress(),
                        packet.getTypeString(),
                        packet.toString());
                if (packet instanceof GetRoomListPacket) {
                    handleGetRoom(socketChannel);
                } else if (packet instanceof CreateRoomPacket) {
                    handleCreateRoom(socketChannel);
                } else if (packet instanceof RoomPacket) {
                    Room room = ((RoomPacket) packet).getRoom();
                    handleJoinRoom(socketChannel, getRoomById(room.id));
                } else if (packet instanceof ExitRoomPacket) {
                    handleExitRoom(socketChannel);
                } else if (packet instanceof RegenerateMapPacket) {
                    Player player = socketsToPlayers.get(socketChannel);
                    Room room = playersToRooms.get(player);
                    LOGGER.info("Regenerating WorldMap...");
                    room.regenerateMap();
                    LOGGER.info("Sending MapPacket...");
                    sendRoomMapInfo(room);
                } else if (packet instanceof StartGamePacket) {
                    Player player = socketsToPlayers.get(socketChannel);
                    Room room = playersToRooms.get(player);
                    room.startGame();
                }
            }
        } catch (IOException e) {
            LOGGER.error("Read failed: ", e);
            key.cancel();
            socketsToPlayers.remove((SocketChannel) key.channel());
            LOGGER.error("Canceled key");
        }
    }

    private void sendRoomMapInfo(Room room) throws IOException {
        for (int i = 0; i < room.players.size(); i++) {
            Util.sendPacket(room.players.get(i).socketChannel, MapPacket.of(room.map));
        }
    }

    private void sendRoomPlayersInfo(Room room) throws IOException {
        for (int i = 0; i < room.players.size(); i++) {
            Util.sendPacket(room.players.get(i).socketChannel, PlayerListPacket.of(room.players));
        }
    }

    private void handleGetRoom(SocketChannel socketChannel) throws IOException {
        LOGGER.info("[{}] Sending Room List", socketChannel.getRemoteAddress());
        Util.sendPacket(socketChannel, RoomListPacket.of(rooms));
    }

    private void handleCreateRoom(SocketChannel socketChannel) throws IOException {
        Player player = socketsToPlayers.get(socketChannel);
        Room room;
        if (deletedRoomIds.size() == 0) {
            room = new Room(rooms.size(), player);
        } else {
            room = new Room(deletedRoomIds.get(0), player);
            deletedRoomIds.remove(0);
        }
        rooms.add(room);
        LOGGER.info("Created Room, total {} Rooms: {}", rooms.size(), rooms);

        playersToRooms.put(player, room);
        LOGGER.info("Sending RoomPacket");
        Util.sendPacket(socketChannel, RoomPacket.of(room));
    }

    private void handleJoinRoom(SocketChannel socketChannel, Room room) throws IOException {
        Player player = socketsToPlayers.get(socketChannel);
        LOGGER.info("Player{{}} Joining room {{}}", player.id, room.id);
        room.addPlayer(socketsToPlayers.get(socketChannel));
        playersToRooms.put(player, room);
        LOGGER.info("Room: {}", room);
        LOGGER.info("Sending RoomPacket");
        Util.sendPacket(socketChannel, RoomPacket.of(room));
        LOGGER.info("Sending PlayerListPackets");
        sendRoomPlayersInfo(room);
    }

    private void handleExitRoom(SocketChannel socketChannel) throws IOException {
        Player player = socketsToPlayers.get(socketChannel);
        Room room = playersToRooms.get(player);
        LOGGER.info("Player{{}} Exiting room {{}}", player.id, room.id);
        room.removePlayer(player);
        playersToRooms.remove(player);

        LOGGER.info("Sending ExitRoomPacket");
        Util.sendPacket(socketChannel, new ExitRoomPacket());

        if (room.players.size() == 0) {
            LOGGER.info("No player left, deleting room...");
            rooms.remove(room);
        } else if (room.owner.id == player.id) {
            LOGGER.info("Owner exit, deleting room...");
            for (int i = 0; i < room.players.size(); i++) {
                Util.sendPacket(room.players.get(i).socketChannel, new ExitRoomPacket());
            }
            rooms.remove(room);
        } else {
            LOGGER.info("Sending PlayerListPackets");
            sendRoomPlayersInfo(room);
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
}
