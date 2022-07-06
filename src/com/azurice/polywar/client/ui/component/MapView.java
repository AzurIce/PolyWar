package com.azurice.polywar.client.ui.component;

import com.azurice.polywar.entity.Vehicle;
import com.azurice.polywar.util.math.Vec2d;
import com.azurice.polywar.world.WorldMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapView extends JPanel {
    private static final int WALL_THICK = 5;
    private static final Random r = new Random();

    private WorldMap map;

    public int mapSize;
//    private int[][] height;

    ////// Entities //////
    //    private List<Missile> missileList = new ArrayList<>();
    private List<Vehicle> vehicleList = new ArrayList<>();
    //    public List<Wall> wallList = new ArrayList<>();
    // Player
    private Vehicle player;

    private Image offScreenImage;

    public MapView(int mapSize) {
        this.mapSize = mapSize;

//        height = new int[mapSize][mapSize];

//        generateHeight();

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(mapSize, mapSize));

        initGame();

        initListener();
    }

    private void initGame() {
        System.out.println("Generating map...");
        map = WorldMap.generateWorldMap(1024);

        System.out.println("Creating player...");
        player = new Vehicle(new Vec2d(200, 200), new Color(30, 144, 255), map);
//        vehicleList.add(player);
//        wallList.add(new Wall(new Polygon(
//                new Vec2d(0, 0), new Vec2d(0, WALL_THICK),
//                new Vec2d(mapSize, WALL_THICK), new Vec2d(mapSize, 0)
//        )));
//        wallList.add(new Wall(new Polygon(
//                new Vec2d(WALL_THICK, 0), new Vec2d(0, 0),
//                new Vec2d(0, mapSize), new Vec2d(WALL_THICK, mapSize)
//        )));
//        wallList.add(new Wall(new Polygon(
//                new Vec2d(mapSize - WALL_THICK, 0), new Vec2d(mapSize - WALL_THICK, mapSize),
//                new Vec2d(mapSize, mapSize), new Vec2d(mapSize, 0)
//        )));
//        wallList.add(new Wall(new Polygon(
//                new Vec2d(0, mapSize - WALL_THICK), new Vec2d(0, mapSize),
//                new Vec2d(mapSize, mapSize), new Vec2d(mapSize, mapSize - WALL_THICK)
//        )));
//
//        wallList.add(new Wall(new Polygon(
//                new Vec2d(0, 0), new Vec2d(0, WALL_THICK * 4),
//                new Vec2d(WALL_THICK * 4, 0)
//        )));
//        wallList.add(new Wall(new Polygon(
//                new Vec2d(0, mapSize), new Vec2d(WALL_THICK * 4, mapSize),
//                new Vec2d(0, mapSize - WALL_THICK * 4)
//        )));
//        wallList.add(new Wall(new Polygon(
//                new Vec2d(mapSize, mapSize), new Vec2d(mapSize, mapSize - WALL_THICK * 4),
//                new Vec2d(mapSize - WALL_THICK * 4, mapSize)
//        )));
//        wallList.add(new Wall(new Polygon(
//                new Vec2d(mapSize, 0), new Vec2d(mapSize - WALL_THICK * 4, 0),
//                new Vec2d(mapSize, WALL_THICK * 4)
//        )));
    }

    private void initListener() {
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
//                player.setCoord(new Vec2d(200, 200));
//                generateHeight();
//                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void keyPressed(KeyEvent e) {
//                System.out.println("Pressed: " + e);
        player.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
//                System.out.println("Released: " + e);
        player.keyReleased(e);
    }

    public void tick() {
        for (Vehicle vehicle : vehicleList) {
            vehicle.tick();
        }
    }

//    public void generateHeight() {
//        PerlinNoise.shuffle();
//        int latticeCnt = 8;
//        for (int i = 0; i < mapSize; i++) {
//            for (int j = 0; j < mapSize; j++) {
//                double mappedI = MyMath.mapValue(i, mapSize, latticeCnt);
//                double mappedJ = MyMath.mapValue(j, mapSize, latticeCnt);
////                System.out.println(mappedI);
////                System.out.println(mappedJ);
//
//                double noise = PerlinNoise.noise(mappedI, mappedJ);
//
//                height[i][j] = (int) (256 * (noise + 2) / 3) - 1;
////                System.out.println("(" + i + ", " + j + "): " + noise + " " + height[i][j]);
//            }
//        }
//    }


    @Override
    public void update(Graphics g) {
        // Buffer image
        if (offScreenImage == null) {
            offScreenImage = this.createImage(WIDTH, HEIGHT);
        }

        Graphics gImage = offScreenImage.getGraphics();

        Color c = Color.BLACK;
        gImage.setColor(c);
        gImage.fillRect(0, 0, WIDTH, HEIGHT); // clear

        paint(gImage); // Draw on the image

        g.drawImage(offScreenImage, 0, 0, null);
    }

    @Override
    public void paint(Graphics g) {
//        super.paint(g);

        // For better visual
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//        BufferedImage bufferedImage = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_BYTE_GRAY);
//        for (int i = 0; i < mapSize; i++) {
//            for (int j = 0; j < mapSize; j++) {
////                System.out.println(height[i][j]);
//                bufferedImage.setRGB(i, j, MyColor.grayRGBColor(height[i][j]));
//            }
//        }
//        g2d.drawImage(bufferedImage, 0, 0, null);

        map.paint(g);

        for (Vehicle vehicle : vehicleList) {
            vehicle.paint(g);
        }

//        for (Wall wall : wallList) {
//            wall.paint(g);
//        }
    }

    public void display(JFrame parent) {
        parent.setContentPane(this);
        Thread renderThread = new Thread(() -> {
            for (; ; ) {
                long time = System.currentTimeMillis();
                repaint();
//                System.out.println(System.currentTimeMillis() - time);
                try {
                    Thread.sleep(1000 / 60 - (System.currentTimeMillis() - time)); // 60fps
                } catch (InterruptedException e) {
                    // TODO: Game over
                    break;
                }
            }
        });
        Thread t = new Thread(() -> {
            for (; ; ) {
                long time = System.currentTimeMillis();
                tick();
                try {
                    Thread.sleep(1000 / 60 - System.currentTimeMillis() + time);
                } catch (InterruptedException e) {
                    // TODO: Game over
                    break;
                }
            }
        });
        t.start();
        renderThread.start();
    }
}
