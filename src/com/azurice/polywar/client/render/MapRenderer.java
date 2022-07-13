package com.azurice.polywar.client.render;

import com.azurice.polywar.entity.Wall;
import com.azurice.polywar.util.math.Vec2d;
import com.azurice.polywar.world.WorldMap;

import java.awt.*;

public class MapRenderer {

    public static void render(WorldMap map, Graphics g, double scale) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw noise
//        BufferedImage bufferedImage = new BufferedImage(map.mapSize, map.mapSize, BufferedImage.TYPE_BYTE_GRAY);
//        for (int i = 0; i < map.mapSize; i++) {
//            for (int j = 0; j < map.mapSize; j++) {
//                bufferedImage.setRGB(i, j, MyColor.grayRGBColor(map.height[i][j]));
//            }
//        }
//        g2d.drawImage(bufferedImage, 0, 0, null);

        // Draw walls
//        System.out.println(map.walls.size());
        for (Wall wall : map.walls) {
//            if (wall.getPolygon().intersect(new Polygon(
//                    screenLocation, screenLocation.add(Vec2d.D.multiply(screenSize.y)),
//                    screenLocation.add(screenSize), screenLocation.add(Vec2d.R.multiply(screenSize.x))
//            )) == Vec2d.ZERO) {
//                continue;
//            }
            wall.paint(g2d, scale);
//            wall.paint(g2d);
        }
    }

    public static void render(WorldMap map, Graphics g, Vec2d screenLocation) {
        Graphics2D g2d = (Graphics2D) g;

        // Draw noise
//        BufferedImage bufferedImage = new BufferedImage(map.mapSize, map.mapSize, BufferedImage.TYPE_BYTE_GRAY);
//        for (int i = 0; i < map.mapSize; i++) {
//            for (int j = 0; j < map.mapSize; j++) {
//                bufferedImage.setRGB(i, j, MyColor.grayRGBColor(map.height[i][j]));
//            }
//        }
//        g2d.drawImage(bufferedImage, 0, 0, null);

        // Draw walls
//        System.out.println(map.walls.size());
        for (Wall wall : map.walls) {
//            if (wall.getPolygon().intersect(new Polygon(
//                    screenLocation, screenLocation.add(Vec2d.D.multiply(screenSize.y)),
//                    screenLocation.add(screenSize), screenLocation.add(Vec2d.R.multiply(screenSize.x))
//            )) == Vec2d.ZERO) {
//                continue;
//            }
            wall.paint(g2d, screenLocation.negate());
//            wall.paint(g2d);
        }

        g2d.setColor(new Color(0xff0000));
        g2d.drawOval(
                (WorldMap.MAP_SIZE / 2 - (int) map.radius) - (int) screenLocation.x,
                (WorldMap.MAP_SIZE / 2 - (int) map.radius) - (int) screenLocation.y,
                2 * (int) map.radius, 2 * (int) map.radius
        );

//        g2d.drawString("x: " + ((map.mapSize / 2 - (int) map.radius) - (int) screenLocation.x) +
//                ", y: " + ((map.mapSize / 2 - (int) map.radius) - (int) screenLocation.y) + ", r: " + map.radius, 300, 300);
    }

}
