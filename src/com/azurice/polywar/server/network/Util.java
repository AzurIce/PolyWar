package com.azurice.polywar.server.network;

public class Util {
    public class CloseException extends Exception {
        public CloseException() {
            super("Remote SocketChannel has already closed");
        }
    }

//    public synchronized void sendMessage(String str) {
//        System.out.println("Response to Client<" + connection.getInetAddress() + ":" + connection.getPort() + ">: " + str);
//        clientIn.println(str);
//    }
//    public Packet readPacket(SocketChannel socketChannel) {
//
//    }
}
