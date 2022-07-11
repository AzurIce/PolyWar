package com.azurice.polywar.network.data;

import com.azurice.polywar.util.math.Vec2d;

import java.io.Serializable;

public class GamePlayerData implements Serializable {
    public int id;
    public int health;
    public Vec2d coord;
    public Vec2d speed;

    public GamePlayerData(int id, int health, Vec2d coord, Vec2d speed) {
        this.id = id;
        this.health = health;
        this.coord = coord;
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "GamePlayerData<id: " + id + ", health: " + health + ", coord: " + coord + ", speed: " + speed + ">";
    }
}