package net.bivrik.fancytoasts.core;

import net.bivrik.fancytoasts.utility.FastMath;

import java.util.Objects;

public final class Color {
    private final int a;
    private final int r;
    private final int g;
    private final int b;
    private final int argb;

    public Color(int a, int r, int g, int b) {
        this.a = clamp(a);
        this.r = clamp(r);
        this.g = clamp(g);
        this.b = clamp(b);
        this.argb = toARGB();
    }

    public Color(float a, float r, float g, float b) {
        this(floatToInt(a), floatToInt(r), floatToInt(g), floatToInt(b));
    }

    public Color(Color other, float a) {
        this.a = floatToInt(clamp(a));
        this.r = other.r;
        this.g = other.g;
        this.b = other.b;
        this.argb = toARGB();
    }

    public Color() {
        this.a = 255;
        this.r = 255;
        this.g = 255;
        this.b = 255;
        this.argb = toARGB();
    }

    private int toARGB() {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public int getARGB() {
        return argb;
    }

    public int getA() {
        return a;
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public Color withAlpha(float a) {
        return new Color(this, a);
    }

    public Color multiplyAlpha(float scalar) {
        return new Color(a * scalar, r, g, b);
    }

    public boolean isTransparent() {
        return a == 0;
    }

    public boolean isOpaque() {
        return a == 255;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + String.format("{a='%s', r='%s', g='%s', b='%s'}", a, r, g, b);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Color color)) return false;
        return argb == color.argb;
    }

    @Override
    public int hashCode() {
        return Objects.hash(a, r, g, b, argb);
    }

    /**
     * Converts ARGB integer into new color
     * @param argb 32-bit representation of color
     * @return color with values from ARGB integer
     */
    public static Color fromARGB(int argb) {
        int a = (argb >> 24) & 0xFF;
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;

        return new Color(a, r, g, b);
    }

    /**
     * Converts given values of color into ARGB integer
     * @param a alpha channel
     * @param r red channel
     * @param g green channel
     * @param b blue channel
     * @return 32-bit representation of color
     */
    public static int toARGB(int a, int r, int g, int b) {
        return ((a << 24) & 0xFF) | ((r << 16) & 0xFF) | ((g << 8) & 0xFF) | (b & 0xFF);
    }

    /**
     * Clamps int color channel
     * @param channel color channel
     * @return clamped value of int color channel <code>[0-255]</code>
     */
    public static int clamp(int channel) {
        return FastMath.clamp(channel, 0, 255);
    }

    /**
     * Clamps float color channel
     * @param channel color channel
     * @return clamped value of float color channel <code>[0.0-1.0]</code>
     */
    public static float clamp(float channel) {
        return FastMath.clamp(channel, 0.0f, 1.0f);
    }

    /**
     * Converts float color channel to int color channel (example: 0.2f -> 51)
     * @param channel color channel
     * @return int color channel (without clamping)
     */
    public static int floatToInt(float channel) {
        return FastMath.round(channel * 255);
    }

    /**
     * Converts int color channel to float color channel (example: 51 -> 0.2f)
     * @param channel color channel
     * @return float color channel (without clamping)
     */
    public static float intToFloat(int channel) {
        return channel / 255.0f;
    }

    /**
     * The White color (1.0f, 1.0f, 1.0f, 1.0f)
     */
    public static Color WHITE = new Color(255, 255, 255, 255);
    /**
     * The Black color (1.0f, 0.0f, 0.0f, 0.0f)
     */
    public static Color BLACK = new Color(255, 0,  0, 0);
    /**
     * The Transparent color (0.0f, 1.0f, 1.0f, 1.0f)
     */
    public static Color TRANSPARENT = new Color(0, 255, 255, 255);
    /**
     * The Light Gray color (0.625f, 0.625f, 0.625f, 0.625f)
     */
    public static Color LIGHT_GRAY = Color.fromARGB(0xFFA0A0A0);
    /**
     * The Yellow color (1.0f, 1.0f, 1.0f, 0.0f)
     */
    public static Color YELLOW = Color.fromARGB(0xFFFFFF00);
    /**
     * The Red color (1.0f, 0.859f, 0.308f, 0.308f)
     */
    public static Color RED = Color.fromARGB(0xFFDC4F4F);
    /**
     * The Purple color (1.0f, 0.976f, 0.234f, 0.976f)
     */
    public static Color PURPLE = Color.fromARGB(0xFFFA3CFA);
    /**
     * The White color (1.0f, 0.132f, 1.0f, 1.0f)
     */
    public static Color CYAN = Color.fromARGB(0xFF22FFFF);
}
