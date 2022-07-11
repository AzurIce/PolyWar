package com.azurice.polywar.client.render;

import com.azurice.polywar.entity.predict.Missile;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public class MissileRenderer {
    public static void render(Missile missile, Graphics g, Vec2d screenLocation, Vec2d screenSize) {
        Graphics2D g2d = (Graphics2D) g;

        missile.paint(g2d, screenLocation);
//        player.paint(g2d);
    }
}
