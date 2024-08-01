package dev.microcontrollers.rendertweaks.util;

import java.awt.Color;

public class ColorUtil {
    public static Color rainbow(float speed, float saturation, float brightness) {
        double rainbowState = Math.ceil(System.currentTimeMillis()) * speed / 2F;
        rainbowState %= 360;
        return Color.getHSBColor((float) (rainbowState / 360.0F), saturation, brightness);
    }

    public static Color getColor(boolean chroma, Color solidColor, float speed, float saturation, float brightness, float alpha) {
        if (chroma) {
            Color color = ColorUtil.rainbow(speed, saturation, brightness);
            return new Color(color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, alpha / 255F);
        } else {
            return solidColor;
        }
    }
}
