package com.azurice.polywar.client;

import com.azurice.polywar.client.ui.MainWindow;
import com.azurice.polywar.network.PingPacket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
    //    Socket socket;
//    public boolean connected = false;
    SocketChannel socketChannel;
    ByteBuffer buf = ByteBuffer.allocate(1024);
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
                    LOGGER.info("Creating SocketChannel...");
                    socketChannel = SocketChannel.open();
                    LOGGER.info("Connecting to Server...");
                    socketChannel.connect(SERVER_ADDRESS);
                    connected = true;
                    LOGGER.info("Connected");
                    // TODO:
                } catch (IOException e) {
                    connected = false;
                    LOGGER.error("Cannot connect to server: {}, retrying in 1 second...", e.toString());
//                throw new RuntimeException(e);
                }
            } else {
                try {
                    ByteBuffer buf = new PingPacket().toByteBuffer();
                    while (buf.hasRemaining()) {
                        socketChannel.write(buf);
                    }
                } catch (IOException e) {
                    LOGGER.error("Write error: ", e);
                    connected = false;
//                    throw new RuntimeException(e);
                }
            }

            try {
                Thread.sleep(3000);
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
//                            throw new RuntimeException(e);
            }
        }
        onStopped();
    }


    public void stop() {
        onStop();
    }

    public void onStop() {
        running = false;
    }

    public void onStopped() {
//        stopped = true;
        onClose();
    }

    public void onClose() {
        System.exit(0);
    }
}
