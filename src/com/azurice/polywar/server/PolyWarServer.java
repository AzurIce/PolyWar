package com.azurice.polywar.server;

import com.azurice.polywar.server.network.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PolyWarServer {
    ///// Singleton //////
    private static PolyWarServer instance = new PolyWarServer();
    List<Handler> handlers = Collections.synchronizedList(new ArrayList<>());
    ServerSocket serverSocket;
    private boolean running = true;


    private PolyWarServer() {
    }

    public static PolyWarServer getInstance() {
        return instance;
    }

    public void run() {

        try {
            serverSocket = new ServerSocket(7777);
            while (this.running) {
                Handler handler = new Handler(this, serverSocket.accept());
                handlers.add(handler);
                new Thread(handler).start();


//                long tickStartTime = Util.getMeasuringTimeMs();
//                tick();
//                try {
//                    Thread.sleep(1000 / 20 - Util.getMeasuringTimeMs() + tickStartTime);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void tick() {

    }

    public void broadcast(String msg) {
        for (int i = 0; i < handlers.size(); i++) {
            handlers.get(i).sendMessage(msg);
        }
    }

    public void removeHandler(Handler handler) {
        this.handlers.remove(handler);
    }
}
