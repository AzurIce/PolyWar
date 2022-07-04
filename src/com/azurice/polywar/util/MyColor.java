package com.azurice.polywar.util;

public class MyColor {
    public static int grayRGBColor(int depth) {
        return (((depth<<8) | depth) << 8) | depth;
    }
}
