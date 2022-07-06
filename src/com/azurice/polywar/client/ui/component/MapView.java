package com.azurice.polywar.client.ui.component;

import com.azurice.polywar.client.render.MapRenderer;
import com.azurice.polywar.entity.Vehicle;
import com.azurice.polywar.util.math.Vec2d;
import com.azurice.polywar.world.WorldMap;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapView extends AbstractView {
    private static final int WALL_THICK = 5;
    private static final Random r = new Random();

    private WorldMap map;

//    private int[][] height;

    ////// Entities //////
    //    private List<Missile> missileList = new ArrayList<>();
    private List<Vehicle> vehicleList = new ArrayList<>();
    //    public List<Wall> wallList = new ArrayList<>();
    // Player
    private Vehicle player;

    private Image offScreenImage;

    public MapView() {
        initGame();
        init();
    }

    private void initGame() {
        System.out.println("Generating map...");
        map = WorldMap.generateWorldMap(1024);

        System.out.println("Creating player...");
        player = new Vehicle(new Vec2d(200, 200), new Color(30, 144, 255), map);
        while (!map.isAvailableToSpawnAt(player.getCoord())) {
            player.setCoord(new Vec2d(r.nextInt(0, 800), r.nextInt(0, 800)));
        }
        vehicleList.add(player);
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

    // KeyListener
    public void keyPressed(KeyEvent e) {
        player.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        player.keyReleased(e);
    }

    // Render & tick
    public void tick() {
        for (Vehicle vehicle : vehicleList) {
            vehicle.tick();
        }
    }

    public void render() {
        repaint();
    }


    // Overrides of painting
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

        MapRenderer.render(map, g2d);

        for (Vehicle vehicle : vehicleList) {
            vehicle.paint(g);
        }

    }


    // Overrides of AbstractView
    @Override
    public void initViews() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 800));

    }

    @Override
    public void initListeners() {
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
}
