package com.azurice.polywar.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Random;

public final class PerlinNoise {
    private static int[] p = new int[512];
    private static final int[] permutation = {
            151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225,
            140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23, 190, 6, 148,
            247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32,
            57, 177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175,
            74, 165, 71, 134, 139, 48, 27, 166, 77, 146, 158, 231, 83, 111, 229, 122,
            60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143, 54,
            65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169,
            200, 196, 135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64,
            52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82, 85, 212,
            207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213,
            119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
            129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104,
            218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241,
            81, 51, 145, 235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157,
            184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254, 138, 236, 205, 93,
            222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
    };


    /**
     * Shuffle the permutation array with the seed that equals current time
     */
    public static void shuffle() { shuffle(new Date().getTime()); }

    /**
     * Shuffle the permutation array with the given seed
     * @param seed If the seed is 0, then use the default one
     */
    public static void shuffle(long seed) {
        if (seed == 0) {
            for (int i = 0; i < 256; i++) {
                p[i+256] = p[i] = permutation[i];
            }
        } else {
            Random r = new Random();
            r.setSeed(seed);

            HashSet<Integer> set = new HashSet<>();
            for (int i = 0; set.size() < 256;) {
                p[i] = r.nextInt(0, 256);
//                System.out.print("(" + i + ", " + set.size() + "): " + p[i] + " ");
                if (!set.contains(p[i])) {
                    p[i+256] = p[i];
                    set.add(p[i]);
                    i++;
                }
            }
//            for (int i = 0; i < 256; i++) {
//                System.out.print(p[i] + " ");
//            }
//            System.out.println("");
        }
    }

    static {
        shuffle();
    }

    public static double noise(double x, double y) {
        // Find unit square that contains point
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;
//        System.out.println("X: " + X + " Y: " + Y);

        // Find relative x, y of the point in square
        x -= Math.floor(x);
        y -= Math.floor(y);
//        System.out.println("x: " + x + " y: " + y);

        // Compute fade curves for each of x, y
        double u = fade(x);
        double v = fade(y);
//        System.out.println("Fade(" + x + ") = " + u);
//        System.out.println("Fade(" + y + ") = " + v);

        // Hash coordinates of the 4 square corners
        int p0, p1, p2, p3;
        p0 = p[p[X] + Y];
        p1 = p[p[X + 1] + Y];
        p2 = p[p[X] + Y + 1];
        p3 = p[p[X + 1] + Y + 1];

        /*
            0---01-----1
            |   |      |
            |   p      |
            |   |      |
            2---23-----3
         */

        // Add blended results from 8 corners of cube
        return lerp(v,
                lerp(u, grad(p0, x, y), grad(p1, x - 1, y)),
                lerp(u, grad(p2, x, y - 1), grad(p3, x - 1, y - 1))
        );
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static double lerp(double t, double a, double b) {
//        System.out.println("Lerp: " + t + " " + a + " " + b + ": " + (a + t * (b - a)));
        return a + t * (b - a);
    }

    /**
     * Convert low 3 bits of hash code into 8 gradient directions
     * @param hash hash code
     * @param x x coordinate
     * @param y y coordinate
     * @return The dot product of the gradient vector and the position vector
     */
    private static double grad(int hash, double x, double y) {
        /*
            (1, 2) (1, -2) (-1, 2) (-1, -2)
            (2, 1) (2, -1) (-2, 1) (-2, -1)
         */
        return switch (hash&7) {
            case 0b000 -> x + 2*y;
            case 0b001 -> x + -2*y;
            case 0b010 -> -x + 2*y;
            case 0b011 -> -x + -2*y;
            case 0b100 -> y + 2*x;
            case 0b101 -> y + -2*x;
            case 0b110 -> -y + 2*x;
            case 0b111 -> -y + -2*x;
            default -> 0; // Never
        } / Math.sqrt(5);
    }

}
