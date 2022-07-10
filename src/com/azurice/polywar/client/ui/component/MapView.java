package com.azurice.polywar.client.ui.component;

import com.azurice.polywar.client.render.MapRenderer;
import com.azurice.polywar.world.WorldMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;

public class MapView extends AbstractView {
    private static final int MAP_SIZE = 4096;
    private static final Logger LOGGER = LogManager.getLogger();
    private WorldMap map;


    ////// Entities //////
    private boolean debug = false;

    public MapView(WorldMap map) {
        this.map = map;
    }

    public void setMap(WorldMap map) {
        this.map = map;
    }

    @Override
    public void render() {
        if (map != null) {
            repaint();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // For better visual(ANTIALIASING)
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        MapRenderer.render(map, g2d, getWidth() / (double) MAP_SIZE);
    }


    // Overrides of AbstractView
    @Override
    public void initViews() {

        setOpaque(false);
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    public void initListeners() {
        addMouseListener(new MouseAdapter() {
        });
    }

    @Override
    public void tick() {

    }
}
