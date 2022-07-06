package com.azurice.polywar.world;

import com.azurice.polywar.entity.Wall;
import com.azurice.polywar.model.Model;
import com.azurice.polywar.util.MyColor;
import com.azurice.polywar.util.MyMath;
import com.azurice.polywar.util.PerlinNoise;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class WorldMap {
    private static int[] dirX = new int[]{0, 0, 1, 1};
    private static int[] dirY = new int[]{1, 0, 0, 1};
    public List<Wall> walls = new ArrayList<>();
    public int[][] height;
    private int mapSize;

    private WorldMap(int mapSize) {
        this.mapSize = mapSize;
        height = new int[mapSize][mapSize];
    }

    public static WorldMap generateWorldMap(int mapSize) {
        WorldMap map = new WorldMap(mapSize);

        int latticeCnt = 8;
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                double mappedI = MyMath.mapValue(i, mapSize, latticeCnt);
                double mappedJ = MyMath.mapValue(j, mapSize, latticeCnt);
//                System.out.println(mappedI);
//                System.out.println(mappedJ);

                double noise = PerlinNoise.noise(mappedI, mappedJ);

                map.height[i][j] = (int) (256 * (noise + 2) / 3) - 1;
//                System.out.println("(" + i + ", " + j + "): " + noise + " " + height[i][j]);
            }
        }

        int resolution = 64, squareLength = 1024 / resolution;
        for (int i = 0; i < resolution - 1; i++) {
            for (int j = 0; j < resolution - 1; j++) {
                int bitmask = 0;
                for (int k = 0; k < 4; k++) {
                    double mappedI = MyMath.mapValue((i + dirX[k]) * squareLength, mapSize, latticeCnt);
                    double mappedJ = MyMath.mapValue((j + dirY[k]) * squareLength, mapSize, latticeCnt);
                    if (PerlinNoise.noise(mappedI, mappedJ) < -0.2) {
                        bitmask |= (1 << k);
                    }
                }
                if (bitmask != 0) {
//                    System.out.println("Bitmask: " + bitmask);
                    map.walls.add(new Wall(new Vec2d((i + 0.5) * squareLength, (j + 0.5) * squareLength), Model.MARCHING_SQUARE.get(bitmask).multiply(squareLength / 2.0)));
                }
            }
        }

//        for (Wall wall: map.walls) {
//            System.out.println(wall.getPolygon());
//        }

        return map;
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        BufferedImage bufferedImage = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
//                System.out.println(height[i][j]);
                bufferedImage.setRGB(i, j, MyColor.grayRGBColor(height[i][j]));
            }
        }
        g2d.drawImage(bufferedImage, 0, 0, null);

//        System.out.println(walls.size());
        for (Wall wall : walls) {
//            System.out.println(wall.getPolygon());
            wall.paint(g2d);
        }
    }
}
