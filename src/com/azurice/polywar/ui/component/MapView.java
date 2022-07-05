package com.azurice.polywar.ui.component;

import com.azurice.polywar.util.MyColor;
import com.azurice.polywar.util.MyMath;
import com.azurice.polywar.util.PerlinNoise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.Random;

public class MapView extends JPanel {
    private int mapSize;
    private static final Random r = new Random();
    private int[][] height;

    public MapView(int mapSize) {
        this.mapSize = mapSize;
        height = new int[mapSize][mapSize];

        generateHeight();

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {
                generateHeight();
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(mapSize, mapSize));
    }

    public void generateHeight(){
        PerlinNoise.shuffle();
        int latticeCnt = 8;
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                double mappedI = MyMath.mapValue(i, mapSize, latticeCnt);
                double mappedJ = MyMath.mapValue(j, mapSize, latticeCnt);
//                height[i][j] = r.nextInt(0, 256);
//                System.out.println(mappedI);
//                System.out.println(mappedJ);

                double noise =PerlinNoise.noise(mappedI, mappedJ);

                height[i][j] = (int) (256 * (noise + 2) / 3) - 1;
//                System.out.println("(" + i + ", " + j + "): " + noise + " " + height[i][j]);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // For better visual
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        BufferedImage bufferedImage = new BufferedImage(mapSize, mapSize, BufferedImage.TYPE_BYTE_GRAY);
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
//                System.out.println(height[i][j]);
                bufferedImage.setRGB(i, j, MyColor.grayRGBColor(height[i][j]));
            }
        }

        g2d.drawImage(bufferedImage, 0, 0, null);
    }
}
