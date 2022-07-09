package com.azurice.polywar.server;

import java.io.Serializable;
import java.nio.channels.SocketChannel;

public class Player implements Serializable {
    public int id;
    public transient SocketChannel socketChannel;

    public Player(int id, SocketChannel socketChannel) {
        this.id = id;
        this.socketChannel = socketChannel;
    }
}
