package com.azurice.polywar.client.render;

import com.azurice.polywar.entity.Wall;
import com.azurice.polywar.util.MyColor;
import com.azurice.polywar.world.WorldMap;

import java.awt.*;
import java.awt.image.BufferedImage;

public class MapRenderer {
    public static void render(WorldMap map, Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw noise
        BufferedImage bufferedImage = new BufferedImage(map.mapSize, map.mapSize, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < map.mapSize; i++) {
            for (int j = 0; j < map.mapSize; j++) {
                bufferedImage.setRGB(i, j, MyColor.grayRGBColor(map.height[i][j]));
            }
        }
        g2d.drawImage(bufferedImage, 0, 0, null);

        // Draw walls
        for (Wall wall : map.walls) {
            if (wall.getCoord().x > 800 || wall.getCoord().y > 800) {
                continue;
            }
            wall.paint(g2d);
        }
    }

}
