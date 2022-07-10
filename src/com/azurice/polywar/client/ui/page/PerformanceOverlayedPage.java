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

    public void paintBg(Graphics g) {

    }

    public void paintOverlay(Graphics g) {
        g.drawString("Fps: " + (int) Math.min(999, client.fps) + "    Ping: " + (int) client.ms + "ms", 300, 14);
        g.drawString("Server: " + (client.isConnected() ? "connected" : "connecting..."), 300, 28);
    }

    // PaintBg -> paintChildren -> paintOverlay
    @Override
    protected void paintChildren(Graphics g) {
        // For better visual(ANTIALIASING)
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        paintBg(g2d);
        super.paintChildren(g);
        paintOverlay(g2d);
    }
}
