package com.azurice.polywar.entity;

import com.azurice.polywar.client.ui.GamePage;
import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import static java.lang.Math.abs;

public class Vehicle extends BaseDrawableEntity {
    ////// CONSTANTS //////
    // Keys
    private static final int KEY_UP = KeyEvent.VK_W;
    private static final int KEY_DOWN = KeyEvent.VK_S;
    private static final int KEY_RIGHT = KeyEvent.VK_D;
    private static final int KEY_LEFT = KeyEvent.VK_A;

    private static final int KEY_SHOOT = MouseEvent.BUTTON1;

    // Vehicle properties
    private static final double FRICTION = 0.01;
    private static final double ACC = 4;
    //
    private static final Color color = new Color(30, 144, 255);
    private static double angle;
    protected Vec2d acceleration = Vec2d.ZERO;
    ////// Properties //////
    // Key status
    private boolean keyUpPressed;
    private boolean keyDownPressed;
    private boolean keyRightPressed;
    private boolean keyLeftPressed;

    public Vehicle(Vec2d coord, GamePage gamePage) {
        super(coord, gamePage, new Polygon(
                new Vec2d(0, 15), new Vec2d(-10, -10), new Vec2d(0, -5), new Vec2d(10, -10)
        ));
    }

    @Override
    public void tick() {
        super.tick();
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

        speed = speed.add(acceleration);
        if (abs(speed.length()) < FRICTION * (speed.length() * speed.length() + 2 * speed.length())) {
            speed = Vec2d.ZERO;
        } else {
            speed = speed.minus(speed.normalize().multiply(FRICTION * (speed.length() * speed.length() + 2 * speed.length())));
        }
        if (abs(speed.x) < 0.5) {
            speed = new Vec2d(0, speed.y);
        }
        if (abs(speed.y) < 0.5) {
            speed = new Vec2d(speed.x, 0);
        }
    }

    @Override
    public java.awt.Polygon getAwtPolygon() {
        if (speed.x != 0 && speed.y != 0) {
            angle = speed.getAngle();
        }
        return MODEL.rotate(angle).add(coord).toAwtPolygon();
    }

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

}
