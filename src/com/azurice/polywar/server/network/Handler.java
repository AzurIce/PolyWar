package com.azurice.polywar.server.network;

import com.azurice.polywar.server.PolyWarServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.SocketChannel;

public class Handler implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger("Handler");
    PolyWarServer server;
    SocketChannel connection;
    PrintWriter clientIn;
    BufferedReader clientOut;

    public Handler(PolyWarServer server, SocketChannel connection) throws IOException {
        this.server = server;
        this.connection = connection;
//        clientOut = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        clientIn = new PrintWriter(connection.getOutputStream(), true);
    }

    public synchronized void sendMessage(String str) {
//        System.out.println("Response to Client<" + connection.getInetAddress() + ":" + connection.getPort() + ">: " + str);
        clientIn.println(str);
    }

    @Override
    public void run() {
        LOGGER.info("Client<{}> connected", connection);
//        while (!connection.isClosed()) {
//            LOGGER.info("Client<{}:{}> isClosed={}", connection.getInetAddress(), connection.getPort(), connection.isClosed());
////            System.out.println(connection.isClosed());
//            try {
//                System.out.println("Client<" + connection.getInetAddress() + ":" + connection.getPort() + "> reading msg...");
//                String msg = clientOut.readLine();
//                if (msg.equals("close")) {
//                    connection.close();
//                    break;
//                }
//                System.out.println("Client<" + connection.getInetAddress() + ":" + connection.getPort() + ">: " + msg);
//                server.broadcast(msg);
//            } catch (IOException e) {
//                System.out.println("[RemoteReader]: IOException: " + e);
//            } catch (Exception e) {
//                try {
//                    connection.close();
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        }
//        System.out.println("Client<" + connection.getInetAddress() + ":" + connection.getPort() + "> closed.");
        server.removeHandler(this);
    }
}
