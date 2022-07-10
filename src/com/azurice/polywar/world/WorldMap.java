package com.azurice.polywar.world;

import com.azurice.polywar.entity.Wall;
import com.azurice.polywar.model.Model;
import com.azurice.polywar.util.PerlinNoise;
import com.azurice.polywar.util.math.Polygon;
import com.azurice.polywar.util.math.Util;
import com.azurice.polywar.util.math.Vec2d;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WorldMap implements Serializable {
    //vvvvvv CONSTANTS vvvvvv//
    private static final int WALL_THICK = 10;
    private static final int[] dirX = new int[]{0, 0, 1, 1};
    private static final int[] dirY = new int[]{1, 0, 0, 1};
    private static final int LATTICE_CNT = 8;
    //^^^^^^ CONSTANTS ^^^^^^//

    public List<Wall> walls = new ArrayList<>();
    public transient int mapSize;

    private WorldMap(int mapSize) {
        this.mapSize = mapSize;
    }

    public static WorldMap generateWorldMap(int mapSize) {
        PerlinNoise.shuffle();
        WorldMap map = new WorldMap(mapSize);

        int resolution = 64, squareLength = mapSize / resolution;
        for (int i = 0; i < resolution - 1; i++) {
            for (int j = 0; j < resolution - 1; j++) {
                int bitmask = 0;
                for (int k = 0; k < 4; k++) {
                    double mappedI = Util.mapValue((i + dirX[k]) * squareLength, mapSize, LATTICE_CNT);
                    double mappedJ = Util.mapValue((j + dirY[k]) * squareLength, mapSize, LATTICE_CNT);
                    if (PerlinNoise.noise(mappedI, mappedJ) < -0.2) {
                        bitmask |= (1 << k);
                    }
                }
                if (bitmask != 0) {
                    map.walls.add(new Wall(new Vec2d((i + 0.5) * squareLength, (j + 0.5) * squareLength), Model.MARCHING_SQUARE.get(bitmask).multiply(squareLength / 2.0)));
                }
            }
        }

        map.addBorder();

        return map;
    }

    public boolean isAvailableToSpawnAt(Vec2d p) {
        double mappedI = Util.mapValue((int) p.x, mapSize, LATTICE_CNT);
        double mappedJ = Util.mapValue((int) p.y, mapSize, LATTICE_CNT);
        return PerlinNoise.noise(mappedI, mappedJ) > 0;
    }

    private void addBorder() {
        walls.add(new Wall(new Polygon(
                new Vec2d(0, 0), new Vec2d(0, WALL_THICK),
                new Vec2d(mapSize, WALL_THICK), new Vec2d(mapSize, 0)
        )));
        walls.add(new Wall(new Polygon(
                new Vec2d(WALL_THICK, 0), new Vec2d(0, 0),
                new Vec2d(0, mapSize), new Vec2d(WALL_THICK, mapSize)
        )));
        walls.add(new Wall(new Polygon(
                new Vec2d(mapSize - WALL_THICK, 0), new Vec2d(mapSize - WALL_THICK, mapSize),
                new Vec2d(mapSize, mapSize), new Vec2d(mapSize, 0)
        )));
        walls.add(new Wall(new Polygon(
                new Vec2d(0, mapSize - WALL_THICK), new Vec2d(0, mapSize),
                new Vec2d(mapSize, mapSize), new Vec2d(mapSize, mapSize - WALL_THICK)
        )));

        walls.add(new Wall(new Polygon(
                new Vec2d(0, 0), new Vec2d(0, WALL_THICK * 4),
                new Vec2d(WALL_THICK * 4, 0)
        )));
        walls.add(new Wall(new Polygon(
                new Vec2d(0, mapSize), new Vec2d(WALL_THICK * 4, mapSize),
                new Vec2d(0, mapSize - WALL_THICK * 4)
        )));
        walls.add(new Wall(new Polygon(
                new Vec2d(mapSize, mapSize), new Vec2d(mapSize, mapSize - WALL_THICK * 4),
                new Vec2d(mapSize - WALL_THICK * 4, mapSize)
        )));
        walls.add(new Wall(new Polygon(
                new Vec2d(mapSize, 0), new Vec2d(mapSize - WALL_THICK * 4, 0),
                new Vec2d(mapSize, WALL_THICK * 4)
        )));
    }
}
