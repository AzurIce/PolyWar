package com.azurice.polywar.server;

import com.azurice.polywar.world.WorldMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Room implements Serializable {
    private static final int MAP_SIZE = 4096;
    public int id;
    public Player owner;
    public List<Player> players = new ArrayList<>();
    public WorldMap map;

    public Room(int id, Player owner, WorldMap map) {
        this.id = id;
        this.owner = owner;
        this.players.add(owner);
        this.map = map;
    }

    public void regenerateMap() {
        map = WorldMap.generateWorldMap();
    }

    public void startGame() {

    }

    public Room(int id, Player owner) {
        this(id, owner, WorldMap.generateWorldMap(MAP_SIZE));
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
    }

    @Override
    public String toString() {
        return "Room<Id: " + id + ", owner: " + owner + ", players: " + players + ">";
    }
}
