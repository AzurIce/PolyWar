package com.azurice.polywar.client.ui;

import com.azurice.polywar.entity.Vehicle;
import com.azurice.polywar.util.MyColor;
import com.azurice.polywar.util.MyMath;
import com.azurice.polywar.util.PerlinNoise;
import com.azurice.polywar.util.math.Vec2d;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GamePage extends AbstractPage {
    public int mapSize;
    private static final Random r = new Random();
    private int[][] height;

    private List<Vehicle> vehicleList = new ArrayList<>();
//    private List<Missile> missileList = new ArrayList<>();

    private Image offScreenImage;

    public GamePage(int mapSize) {
        this.mapSize = mapSize;
        height = new int[mapSize][mapSize];

        generateHeight();
        vehicleList.add(new Vehicle(new Vec2d(200, 200), this));

        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(mapSize, mapSize));

        initListener();
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

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
//                System.out.println("Pressed: " + e);
                vehicleList.get(0).keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
//                System.out.println("Released: " + e);
                vehicleList.get(0).keyReleased(e);
            }
        });
    }

    public void tick() {
        for (Vehicle vehicle : vehicleList) {
            vehicle.tick();
        }
    }

    public void generateHeight() {
        PerlinNoise.shuffle();
        int latticeCnt = 8;
        for (int i = 0; i < mapSize; i++) {
            for (int j = 0; j < mapSize; j++) {
                double mappedI = MyMath.mapValue(i, mapSize, latticeCnt);
                double mappedJ = MyMath.mapValue(j, mapSize, latticeCnt);
//                System.out.println(mappedI);
//                System.out.println(mappedJ);

                double noise = PerlinNoise.noise(mappedI, mappedJ);

                height[i][j] = (int) (256 * (noise + 2) / 3) - 1;
//                System.out.println("(" + i + ", " + j + "): " + noise + " " + height[i][j]);
            }
        }
    }


    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            // 截取窗体所在位置的图片
            offScreenImage = this.createImage(WIDTH, HEIGHT);
        }
        // 获得截取图片的画布
        Graphics gImage = offScreenImage.getGraphics();
        // 获取画布的底色并且使用这种颜色填充画布（默认的颜色为黑色）
        Color c = Color.BLACK;
        gImage.setColor(c);
        gImage.fillRect(0, 0, WIDTH, HEIGHT);
        // 将截下的图片上的画布传给重绘函数，重绘函数只需要在截图的画布上绘制即可，不必在从底层绘制
        paint(gImage);
        // 将截下来的图片加载到窗体画布上去，才能考到每次画的效果
        g.drawImage(offScreenImage, 0, 0, null);
    }

    @Override
    public void paint(Graphics g) {
//        super.paint(g);

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

        for (Vehicle vehicle : vehicleList) {
            vehicle.paint(g);
        }
    }

    @Override
    public void display() {
        super.display();
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
            while (true) {
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
