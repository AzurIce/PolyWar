package com.azurice.polywar.server;

import java.io.Serializable;
import java.nio.channels.SocketChannel;

public class Player implements Serializable {
    public int id;
    public String name;
    public transient SocketChannel socketChannel;

    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.socketChannel = null;
    }

    public Player(int id, String name, SocketChannel socketChannel) {
        this.id = id;
        this.name = name;
        this.socketChannel = socketChannel;
    }

    @Override
    public String toString() {
        return "Player<id: " + id + ", name: " + name + ">";
    }
}
