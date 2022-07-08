package com.azurice.polywar.client.ui.component;

import com.azurice.polywar.client.render.MapRenderer;
import com.azurice.polywar.client.render.PlayerRenderer;
import com.azurice.polywar.entity.predict.Player;
import com.azurice.polywar.util.math.Util;
import com.azurice.polywar.util.math.Vec2d;
import com.azurice.polywar.world.WorldMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapView extends AbstractView {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random r = new Random();
    private static final int MAP_SIZE = 4096;
    private static final int VIEWPORT_SIZE = 800;

    private WorldMap map;


    ////// Entities //////
//        private List<Missile> missileList = new ArrayList<>();
    private List<Player> playerList;
    // Player
    private Player mainPlayer;

    private Vec2d screenLocation = Vec2d.ZERO;

    private boolean debug = false;

    private void initGame() {
        playerList = new ArrayList<>();
        LOGGER.info("Generating map...");
        map = WorldMap.generateWorldMap(MAP_SIZE);

        LOGGER.info("Creating player...");
        mainPlayer = new Player(
                new Vec2d(r.nextInt(0, MAP_SIZE), r.nextInt(0, MAP_SIZE)),
                new Color(30, 144, 255),
                map
        );
        while (!map.isAvailableToSpawnAt(mainPlayer.getCoord())) {
            mainPlayer.setCoord(new Vec2d(r.nextInt(0, MAP_SIZE), r.nextInt(0, MAP_SIZE)));
        }
        playerList.add(mainPlayer);
    }

    // KeyListener
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F3) {
            debug = !debug;
        }
        mainPlayer.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        mainPlayer.keyReleased(e);
    }


    // Render & tick
    @Override
    public void tick() {
        for (Player player : playerList) {
            player.tick();
        }
    }

    private void updateScreenLocation() {
        double x = mainPlayer.getPredictedCoord().x;
        double y = mainPlayer.getPredictedCoord().y;
        screenLocation = new Vec2d(
                Util.clip(x - VIEWPORT_SIZE / 2.0, 0, MAP_SIZE - VIEWPORT_SIZE),
                Util.clip(y - VIEWPORT_SIZE / 2.0, 0, MAP_SIZE - VIEWPORT_SIZE)
        );
    }

    @Override
    public void render() {
        updateScreenLocation();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // For better visual(ANTIALIASING)
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        MapRenderer.render(map, g2d, screenLocation, new Vec2d(VIEWPORT_SIZE, VIEWPORT_SIZE));

        for (Player player : playerList) {
            PlayerRenderer.render(player, g2d, screenLocation.negate(), new Vec2d(VIEWPORT_SIZE, VIEWPORT_SIZE));
        }

        if (debug) {
            g.setColor(new Color(0x3c3c3c));
            g.fillOval(VIEWPORT_SIZE / 2 - 5, VIEWPORT_SIZE / 2 - 5, 10, 10);
            g.drawString("PlayerCoord: " + mainPlayer.getCoord(), 350, 14);
            g.drawString("PredictedPlayerCoord: " + mainPlayer.getPredictedCoord(), 350, 28);
            g.drawString("ScreenLocation: " + screenLocation, 350, 42);
            g.drawString("ScreenCenterLocation: " + screenLocation.add(400), 350, 56);
        }
    }


    // Overrides of AbstractView
    @Override
    public void initViews() {

        setOpaque(false);
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(VIEWPORT_SIZE, VIEWPORT_SIZE));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        initGame();
    }

    @Override
    public void initListeners() {
        addMouseListener(new MouseAdapter() {
        });
    }
}
