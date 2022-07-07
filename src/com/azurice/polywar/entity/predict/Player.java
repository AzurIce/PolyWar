package com.azurice.polywar.entity.predict;

import com.azurice.polywar.entity.Wall;
import com.azurice.polywar.model.Model;
import com.azurice.polywar.util.math.Vec2d;
import com.azurice.polywar.world.WorldMap;

import java.awt.*;
import java.awt.event.KeyEvent;

import static java.lang.Math.abs;

public class Player extends SpeedDirectionEntity {
    ////// CONSTANTS //////
    // Keys
    private static final int KEY_UP = KeyEvent.VK_W;
    private static final int KEY_DOWN = KeyEvent.VK_S;
    private static final int KEY_RIGHT = KeyEvent.VK_D;
    private static final int KEY_LEFT = KeyEvent.VK_A;

    private static final int KEY_SHOOT = KeyEvent.VK_SPACE;

    // Vehicle properties
    private static final double FRICTION = 0.01;
    private static final double ACC = 4;

    ////// Properties //////
    // Key status
    private boolean keyUpPressed;
    private boolean keyDownPressed;
    private boolean keyRightPressed;
    private boolean keyLeftPressed;

    protected Vec2d acceleration = Vec2d.ZERO;

    private final WorldMap worldMap;

    private int health = 100;


    public Player(Vec2d coord, WorldMap map) {
        super(coord, Model.PLAYER);
        worldMap = map;
    }

    public Player(Vec2d coord, Color color, WorldMap map) {
        super(coord, Model.PLAYER, color);
        worldMap = map;
    }


    @Override
    public void tick() {
//        System.out.println("Coord: " + coord + ", Speed: " + speed + ", Acceleration: " + acceleration);

        acceleration = Vec2d.ZERO;
        if (keyUpPressed) {
            acceleration = acceleration.add(new Vec2d(0, -ACC));
        }
        if (keyDownPressed) {
            acceleration = acceleration.add(new Vec2d(0, ACC));
        }
        if (keyLeftPressed) {
            acceleration = acceleration.add(new Vec2d(-ACC, 0));
        }
        if (keyRightPressed) {
            acceleration = acceleration.add(new Vec2d(+ACC, 0));
        }

        for (Wall wall : worldMap.walls) {
            Vec2d collisionPoint = getPolygon().intersect(wall.getPolygon()); // Which is better?
//            Vec2d sp = wall.getPolygon().intersect(getPolygon()); // Which is better? Why not working?
            if (collisionPoint != Vec2d.ZERO) {
//                System.out.println(getModel() + " + " + coord);
//                System.out.println(getPolygon());
                while (collisionPoint != Vec2d.ZERO) {
//                    System.out.println("Intersect!");
//                    coord = coord.minus(speed.normalize()); // Not working...
//                    coord = coord.minus(sp.minus(coord).normalize()); // Which is better?
                    coord = coord.minus(collisionPoint.minus(coord).normalize().add(speed.normalize())); // Which is better?
                    collisionPoint = getPolygon().intersect(wall.getPolygon()); // Which is better?
//                    sp = wall.getPolygon().intersect(getPolygon()); // Which is better? Why not working?
                }
                speed = Vec2d.ZERO; // Do we need it?
//                speed = speed.negate();
//                speed = speed.multiply(0.1);
            }
        }

        speed = speed.add(acceleration);
        if (abs(speed.length()) < FRICTION * (speed.length() * speed.length() + 2 * speed.length())) {
            speed = Vec2d.ZERO;
        } else {
            speed = speed.minus(speed.normalize().multiply(FRICTION * (1.2 * speed.length() * speed.length() + 2 * speed.length())));
        }
        if (abs(speed.x) < 0.5) {
            speed = new Vec2d(0, speed.y);
        }
        if (abs(speed.y) < 0.5) {
            speed = new Vec2d(speed.x, 0);
        }

        super.tick();
    }

    ////// Key listeners //////
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KEY_UP -> keyUpPressed = true;
            case KEY_DOWN -> keyDownPressed = true;
            case KEY_LEFT -> keyLeftPressed = true;
            case KEY_RIGHT -> keyRightPressed = true;
            default -> {
            } // Never happens
        }
//        System.out.println(keyUpPressed + " " + keyDownPressed + " " + keyLeftPressed + " " + keyRightPressed);
    }

    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KEY_UP -> keyUpPressed = false;
            case KEY_DOWN -> keyDownPressed = false;
            case KEY_LEFT -> keyLeftPressed = false;
            case KEY_RIGHT -> keyRightPressed = false;
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


}
