package com.azurice.polywar.server.network;

import com.azurice.polywar.server.PolyWarServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Handler implements Runnable {
    PolyWarServer server;
    Socket connection;
    PrintWriter clientIn;
    BufferedReader clientOut;

    public Handler(PolyWarServer server, Socket connection) throws IOException {
        this.server = server;
        this.connection = connection;
        clientOut = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        clientIn = new PrintWriter(connection.getOutputStream(), true);
    }

    public synchronized void sendMessage(String str) {
        System.out.println("Response to Client<" + connection.getInetAddress() + ":" + connection.getPort() + ">: " + str);
        clientIn.println(str);
    }

    @Override
    public void run() {
        System.out.println("Client<" + connection.getInetAddress() + ":" + connection.getPort() + "> connected...");
        while (!connection.isClosed()) {
            try {
                System.out.println("Client<" + connection.getInetAddress() + ":" + connection.getPort() + "> reading msg...");
                String msg = clientOut.readLine();
                if (msg.equals("close")) {
                    connection.close();
                    break;
                }
                System.out.println("Client<" + connection.getInetAddress() + ":" + connection.getPort() + ">: " + msg);
                server.broadcast(msg);
            } catch (IOException e) {
                System.out.println("[RemoteReader]: IOException: " + e);
            }
            System.out.println("Client<" + connection.getInetAddress() + ":" + connection.getPort() + "> closed.");
        }
        server.removeHandler(this);
    }
}
