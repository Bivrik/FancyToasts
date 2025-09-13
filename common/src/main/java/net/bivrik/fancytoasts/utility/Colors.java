package net.bivrik.fancytoasts.utility;

public class Colors {
    public static final int YELLOW = -256;
    public static final int RED = -2142128;
    public static final int LIGHT_GRAY = -6250336;
    public static final int WHITE = -1;
    public static final int GRAY = -8355712;
    public static final int PURPLE = 0xEA3CFF;
    public static final int CYAN = 0x00FFFF;

    public static int alpha(int alpha, int color) {
        return alpha << 24 | color & 16777215;
    }
}
