package com.azurice.polywar.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    public String roomName;
    public List<Integer> players = new ArrayList<>();
    public Integer owner;

    public Room(String roomName, int owner) {
        this.roomName = roomName;
        this.owner = owner;
        this.players.add(owner);
    }
}
