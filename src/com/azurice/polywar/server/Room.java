package com.azurice.polywar.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    public int id;
    public List<Player> players = new ArrayList<>();
    public Player owner;

    public Room(int id, Player owner) {
        this.id = id;
        this.owner = owner;
        this.players.add(owner);
    }
}
