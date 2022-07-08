package com.azurice.polywar.server;

import com.azurice.polywar.network.Packet;
import com.azurice.polywar.network.Util;
import com.azurice.polywar.server.network.Handler;
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
    List<Handler> handlers = Collections.synchronizedList(new ArrayList<>());
    ServerSocketChannel serverSocketChannel;
    SocketChannel socketChannel;
    Selector selector;
    List<Room> rooms = Collections.synchronizedList(new ArrayList<>());

    private PolyWarServer() {
    }

    private boolean running = true;

    public static PolyWarServer getInstance() {
        return instance;
    }

    public void run() {
        this.running = true;

        try {
            LOGGER.info("Creating Selector");
            selector = Selector.open();

            LOGGER.info("Creating ServerSocketChannel...");
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(LISTEN_ADDRESS);

            LOGGER.info("Registering ServerSocketChannel...");
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        } catch (IOException e) {
            LOGGER.error(e);
            this.running = false;
        }

        LOGGER.info("Handling for connection...");
        while (this.running) {
            try {
                selector.select();
            } catch (IOException e) {
                LOGGER.error("Select failed: ", e);
                continue;
//                throw new RuntimeException(e);
            }

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
//            System.out.println(selectionKeys);
            Iterator<SelectionKey> it = selectionKeys.iterator();
            while (it.hasNext()) {
                SelectionKey key = it.next();
                it.remove();

                if (key.isAcceptable()) { // OP_ACCEPT
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
//                    LOGGER.info("{} is Accept-ready", serverSocketChannel);
                    handleAccept(serverSocketChannel);
                } else if (key.isReadable()) { // OP_READ
                    SocketChannel socketChannel = (SocketChannel) key.channel();
//                    LOGGER.info("{} is Read-ready", socketChannel);
                    handleRead(socketChannel);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleAccept(ServerSocketChannel serverSocketChannel) {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            LOGGER.info("Accepted Client<{}>, registering...", socketChannel);
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            LOGGER.error("Accept failed: ", e);
        }
    }

    public void handleRead(SocketChannel socketChannel) {
        try {
            Packet packet = Util.getPacket(socketChannel);
            if (packet == null) return;

            LOGGER.info("[{}] Received {}: {}",
                    socketChannel.getRemoteAddress(),
                    packet.getTypeString(),
                    packet.toString());
        } catch (IOException e) {
            LOGGER.error("Read failed: ", e);
        }
    }


    public void broadcast(String msg) {
        for (int i = 0; i < handlers.size(); i++) {
            handlers.get(i).sendMessage(msg);
        }
    }

    public void removeHandler(Handler handler) {
        LOGGER.info("Removing handler: {} ...", handler);
        this.handlers.remove(handler);
    }
}
