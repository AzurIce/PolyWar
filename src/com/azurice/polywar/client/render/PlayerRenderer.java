package com.azurice.polywar.client.render;

import com.azurice.polywar.entity.predict.GamePlayer;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;

public class PlayerRenderer {
    public static void render(GamePlayer gamePlayer, Graphics g, Vec2d screenLocation, Vec2d screenSize) {
        Graphics2D g2d = (Graphics2D) g;

        gamePlayer.paint(g2d, screenLocation);
//        player.paint(g2d);
    }
}
