package net.bivrik.fancytoasts.platform.utility;

public class Colors {
    public static final int WHITE = -1;
    public static final int LIGHT_GRAY = 0xFFA0A0A0;
    public static final int YELLOW = 0xFFFFFF00;
    public static final int RED = 0xFFDC4F4F;
    public static final int PURPLE = 0xFFFA3CFA;
    public static final int CYAN = 0xFF22FFFF;

    public static int alpha(int alpha, int color) {
        return alpha << 24 | color & 0xFFFFFF;
    }
}
