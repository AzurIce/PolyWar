package com.azurice.polywar.client;

import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.network.Packet;
import com.azurice.polywar.network.PingPacket;
import com.azurice.polywar.network.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

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


    public double fps;
    public double ms;
    private boolean running = true;
    SocketChannel socketChannel;
    private MainWindow window;
    private boolean connected = false;

    public boolean isConnected() {
        return connected;
    }

    // Runtime control
    public void run() {
        window = new MainWindow(getInstance());
        window.display();

        while (running) {
            if (!connected) {
                try {
                    LOGGER.info("Connecting to Server...");
                    socketChannel = SocketChannel.open();
                    socketChannel.connect(SERVER_ADDRESS);
                    connected = true;
                    LOGGER.info("Connected");
                } catch (IOException e) {
                    connected = false;
                    LOGGER.error("Server connect failed: {}, retrying in 3 seconds...", e.toString());
                }
            } else {
                try {
                    long timeSend = com.azurice.polywar.util.Util.getMeasuringTimeMs();
                    Util.sendPacket(socketChannel, new PingPacket());
//                    LOGGER.info("Sent PingPacket: {}", timeSend);

                    Packet packet = Util.getPacket(socketChannel);
                    long timeReceived = com.azurice.polywar.util.Util.getMeasuringTimeMs();
//                    LOGGER.info("Received PingPacket: {}", timeReceived);
                    ms = timeReceived - timeSend;
                    if (packet == null) {
                        socketChannel.close();
                        connected = false;
                    }
                } catch (IOException e) {
                    LOGGER.error("Ping error: ", e);
                    connected = false;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        LOGGER.info("Closing...");
        if (socketChannel != null) {
            try {
                LOGGER.info("Closing SocketChannel...");
                socketChannel.close();
            } catch (IOException e) {
                LOGGER.error("Cannot close SocketChannel: {}", e.toString());
            }
        }
        onStopped();
    }


    public void stop() {
        running = false;
    }

    public void onStopped() {
        System.exit(0);
    }
}
