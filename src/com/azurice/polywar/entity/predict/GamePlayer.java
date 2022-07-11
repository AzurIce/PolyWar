package com.azurice.polywar.entity.predict;

import com.azurice.polywar.client.PolyWarClient;
import com.azurice.polywar.model.Model;
import com.azurice.polywar.network.data.GamePlayerControlData;
import com.azurice.polywar.network.data.GamePlayerData;
import com.azurice.polywar.network.packet.GamePlayerControlDataPacket;
import com.azurice.polywar.util.math.Vec2d;
import com.azurice.polywar.world.WorldMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GamePlayer extends SpeedDirectionEntity {
    ////// CONSTANTS //////
    private static final Logger LOGGER = LogManager.getLogger();
    // Keys
    private static final int KEY_UP = KeyEvent.VK_W;
    private static final int KEY_DOWN = KeyEvent.VK_S;
    private static final int KEY_RIGHT = KeyEvent.VK_D;
    private static final int KEY_LEFT = KeyEvent.VK_A;

    private static final int KEY_SHOOT = KeyEvent.VK_SPACE;

    // Vehicle properties
//    private static final double FRICTION = 0.01;
//    private static final double ACC = 4;
    public int id = 0;
    protected transient Vec2d acceleration = Vec2d.ZERO;
    ////// Properties //////
    // Key status
    GamePlayerControlData gamePlayerControlData = new GamePlayerControlData();

    private final WorldMap worldMap;

    public GamePlayer(Vec2d coord, WorldMap map, int id) {
        this(coord, map);
        this.id = id;
    }

    public GamePlayer(Vec2d coord, Color color, WorldMap map, int id) {
        this(coord, color, map);
        this.id = id;
    }

    private int health = 100;

    public GamePlayer(Vec2d coord, WorldMap map) {
        super(coord, Model.PLAYER);
        worldMap = map;
    }

    public GamePlayer(Vec2d coord, Color color, WorldMap map) {
        super(coord, Model.PLAYER, color);
        worldMap = map;
    }

    public void updatePlayerData(GamePlayerData data) {
        this.coord = data.coord;
        this.speed = data.speed;
        this.health = data.health;
        lastTickCoord = new Vec2d(coord);
        lastTickSpeed = new Vec2d(speed);
        updateLastTickTime();
    }

    @Override
    public void tick() {
        PolyWarClient.getInstance().sendPacket(GamePlayerControlDataPacket.of(gamePlayerControlData));
    }

    ////// Key listeners //////
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KEY_UP -> gamePlayerControlData.keyUpPressed = true;
            case KEY_DOWN -> gamePlayerControlData.keyDownPressed = true;
            case KEY_LEFT -> gamePlayerControlData.keyLeftPressed = true;
            case KEY_RIGHT -> gamePlayerControlData.keyRightPressed = true;
            case KEY_SHOOT -> gamePlayerControlData.keyShootPressed = true;
            default -> {
            } // Never happens
        }
//        System.out.println(keyUpPressed + " " + keyDownPressed + " " + keyLeftPressed + " " + keyRightPressed);
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KEY_UP -> gamePlayerControlData.keyUpPressed = false;
            case KEY_DOWN -> gamePlayerControlData.keyDownPressed = false;
            case KEY_LEFT -> gamePlayerControlData.keyLeftPressed = false;
            case KEY_RIGHT -> gamePlayerControlData.keyRightPressed = false;
            case KEY_SHOOT -> gamePlayerControlData.keyShootPressed = false;
            default -> {
            } // Never happens
        }
//        System.out.println(keyUpPressed + " " + keyDownPressed + " " + keyLeftPressed + " " + keyRightPressed);
    }

    ////// Getters & Setters //////
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    @Override
    public String toString() {
        return "GamePlayer<id: " + id + ", coord: " + coord + ", speed: " + speed + ", health: " + health + ">";
    }
}
