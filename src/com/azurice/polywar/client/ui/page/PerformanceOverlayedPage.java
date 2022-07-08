package com.azurice.polywar.client.ui.page;

import com.azurice.polywar.client.PolyWarClient;
import com.azurice.polywar.client.ui.MainWindow;

import java.awt.*;

public abstract class PerformanceOverlayedPage extends AbstractPage {
    private final PolyWarClient client;

    public PerformanceOverlayedPage(PolyWarClient client, MainWindow parent) {
        super(parent);
        this.client = client;
    }

    @Override
    public void render() {
        repaint();
    }

    @Override
    public void tick() {

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawString("FPS: " + (int) client.fps, 5, 14);
        g.drawString("Server: " + (client.isConnected() ? "connected" : "connecting..."), 5, 28);
    }
}
