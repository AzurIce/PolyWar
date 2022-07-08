package com.azurice.polywar.server;

import com.azurice.polywar.network.Packet;
import com.azurice.polywar.network.PingPacket;
import com.azurice.polywar.network.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
            this.running = false;
        }

        LOGGER.info("Handling connections...");
        while (this.running) {
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
                } else if (key.isReadable() & key.isWritable()) { // OP_READ
                    handleRead(key);
                }
            }
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
        }

        try {
            if (serverSocketChannel != null) {
                serverSocketChannel.close();
            }
        } catch (IOException e) {
            LOGGER.error("ServerSocketChannel close failed: ", e);
        }
    }

    public void handleAccept(ServerSocketChannel serverSocketChannel) {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            LOGGER.info("Accepted Client<{}>, registering...", socketChannel);
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        } catch (IOException e) {
            LOGGER.error("Accept failed: ", e);
        }
    }

    public void handleRead(SelectionKey key) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        try {
            Packet packet = Util.getPacket(socketChannel);
            if (packet == null) {
                LOGGER.info("[{}] Closed", socketChannel.getRemoteAddress());
                socketChannel.close();
                return;
            }
            LOGGER.info("[{}] Received {}: {}",
                    socketChannel.getRemoteAddress(),
                    packet.getTypeString(),
                    packet.toString());

            if (packet instanceof PingPacket) {
                LOGGER.info("[{}] Sending Ping response", socketChannel.getRemoteAddress());
                socketChannel.write(new PingPacket().toByteBuffer());
            }
        } catch (IOException e) {
            LOGGER.error("Read failed: ", e);
            key.cancel();
            LOGGER.error("Canceld key");
        }
    }
}
