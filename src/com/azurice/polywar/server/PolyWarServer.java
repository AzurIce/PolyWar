package com.azurice.polywar.server;

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
    Map<SocketChannel, Player> socketsToPlayers = Collections.synchronizedMap(new HashMap<>());
    List<Integer> deletedPlayerIds = Collections.synchronizedList(new ArrayList<>());

    private PolyWarServer() {
    }

    private boolean running = true;

    public static PolyWarServer getInstance() {
        return instance;
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
                    LOGGER.info("[{}] Sending Room List", socketChannel.getRemoteAddress());
                    Util.sendPacket(socketChannel, RoomListPacket.of(rooms));
                } else if (packet instanceof CreateRoomPacket) {
                    if (deletedRoomIds.size() == 0) {
                        rooms.add(new Room(rooms.size(), socketsToPlayers.get(socketChannel)));
                    } else {
                        socketsToPlayers.put(socketChannel, new Player(deletedRoomIds.get(0), socketChannel));
                        deletedRoomIds.remove(0);
                    }
                    LOGGER.info("Created Room, total {} Rooms: {}", rooms.size(), rooms);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Read failed: ", e);
            key.cancel();
            socketsToPlayers.remove((SocketChannel) key.channel());
            LOGGER.error("Canceled key");
        }
    }
}
