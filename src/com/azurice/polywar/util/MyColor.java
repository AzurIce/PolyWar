package com.azurice.polywar.util;

public class MyColor {

    public static int BLUE = 0x1e90ff;

    public static int grayRGBColor(int depth) {
        return (((depth << 8) | depth) << 8) | depth;
    }
}
