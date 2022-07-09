package com.azurice.polywar.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    public int id;
    public Player owner;
    public List<Player> players = new ArrayList<>();

    public Room(int id, Player owner) {
        this.id = id;
        this.owner = owner;
        this.players.add(owner);
    }

    @Override
    public String toString() {
        return "Room<Id: " + id + ", owner: " + owner + ", players: " + players + ">";
    }
}
