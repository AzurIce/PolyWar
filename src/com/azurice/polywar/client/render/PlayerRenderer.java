package com.azurice.polywar.client.render;

import com.azurice.polywar.entity.Player;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public class PlayerRenderer {
    public static void render(Player player, Graphics g, Vec2d screenLocation, Vec2d screenSize) {
        Graphics2D g2d = (Graphics2D) g;

        player.paint(g2d, screenLocation);
//        player.paint(g2d);
    }
}
