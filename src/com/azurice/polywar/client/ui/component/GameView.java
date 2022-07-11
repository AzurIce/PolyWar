package com.azurice.polywar.client.ui.component;

import com.azurice.polywar.client.PolyWarClient;
import com.azurice.polywar.client.render.MapRenderer;
import com.azurice.polywar.client.render.PlayerRenderer;
import com.azurice.polywar.entity.predict.GamePlayer;
import com.azurice.polywar.network.data.GamePlayerData;
import com.azurice.polywar.util.MyColor;
import com.azurice.polywar.util.math.Util;
import com.azurice.polywar.util.math.Vec2d;
import com.azurice.polywar.world.WorldMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.*;

public class GameView extends AbstractView {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final PolyWarClient client = PolyWarClient.getInstance();
    private static final Random r = new Random();
    private static final int MAP_SIZE = 4096;
    private static final int VIEWPORT_SIZE = 800;


    ////// Entities //////
//        private List<Missile> missileList = new ArrayList<>();
    private Map<Integer, GamePlayer> gamePlayers;
    // Player
    private GamePlayer mainGamePlayer;

    private Vec2d screenLocation = Vec2d.ZERO;
    private WorldMap worldMap;

    private boolean debug = false;

    public void setMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public void setMainGamePlayerData(GamePlayerData data) {
        gamePlayers = Collections.synchronizedMap(new HashMap<>());
        mainGamePlayer = new GamePlayer(data.coord, new Color(MyColor.BLUE), worldMap);
        gamePlayers.put(data.id, mainGamePlayer);
        LOGGER.info("Set mainGamePlayer: {}", mainGamePlayer);
    }

    public void updateGamePlayersData(List<GamePlayerData> gamePlayersData) {
        for (GamePlayerData data : gamePlayersData) {
            GamePlayer gamePlayer;
            if (!gamePlayers.containsKey(data.id)) {
                gamePlayer = new GamePlayer(data.coord, worldMap, data.id);
                gamePlayers.put(data.id, gamePlayer);
            } else {
                gamePlayer = gamePlayers.get(data.id);
            }
            gamePlayer.updatePlayerData(data);
//            LOGGER.info("Update player data: {}", gamePlayer);
        }
    }


//    private void initGame() {
//        gamePlayerList = new ArrayList<>();
//        LOGGER.info("Generating map...");
//        map = WorldMap.generateWorldMap(MAP_SIZE);
//
//        LOGGER.info("Creating player...");
//        mainGamePlayer = new GamePlayer(
//                new Vec2d(r.nextInt(0, MAP_SIZE), r.nextInt(0, MAP_SIZE)),
//                new Color(30, 144, 255),
//                map
//        );
//        while (!map.isAvailableToSpawnAt(mainGamePlayer.getCoord())) {
//            mainGamePlayer.setCoord(new Vec2d(r.nextInt(0, MAP_SIZE), r.nextInt(0, MAP_SIZE)));
//        }
//        gamePlayerList.add(mainGamePlayer);
//    }

    // KeyListener
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F3) {
            debug = !debug;
        }
        mainGamePlayer.keyPressed(e);
    }

    public void keyReleased(KeyEvent e) {
        mainGamePlayer.keyReleased(e);
    }


    // Render & tick
    @Override
    public void tick() {
        mainGamePlayer.tick();
//        for (GamePlayer gamePlayer : gamePlayers.values()) {
//            gamePlayer.tick();
//        }
    }

    private void updateScreenLocation() {
        double x = mainGamePlayer.getPredictedCoord().x;
        double y = mainGamePlayer.getPredictedCoord().y;
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

        MapRenderer.render(worldMap, g2d, screenLocation);

        for (GamePlayer gamePlayer : gamePlayers.values()) {
            PlayerRenderer.render(gamePlayer, g2d, screenLocation.negate(), new Vec2d(VIEWPORT_SIZE, VIEWPORT_SIZE));
        }

        if (debug) {
            g.setColor(new Color(0x3c3c3c));
            g.fillOval(VIEWPORT_SIZE / 2 - 5, VIEWPORT_SIZE / 2 - 5, 10, 10);
            g.drawString("PlayerCoord: " + mainGamePlayer.getCoord(), 350, 14);
            g.drawString("PredictedPlayerCoord: " + mainGamePlayer.getPredictedCoord(), 350, 28);
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
//        initGame();
    }

    @Override
    public void initListeners() {
        addMouseListener(new MouseAdapter() {
        });
    }
}