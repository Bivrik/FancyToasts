package net.bivrik.fancytoasts.platform.utility;

import net.minecraft.util.Mth;

public class Colors {
    public static final int WHITE = -1;
    public static final int BLACK = 0x000000;
    public static final int LIGHT_GRAY = 0xFFA0A0A0;
    public static final int YELLOW = 0xFFFFFF00;
    public static final int RED = 0xFFDC4F4F;
    public static final int PURPLE = 0xFFFA3CFA;
    public static final int CYAN = 0xFF22FFFF;

    public static int alpha(int alpha, int color) {
        if (alpha == 255) {
            return color;
        }

        alpha = Mth.clamp(alpha, 0, 255);
        return alpha << 24 | color & 0xFFFFFF;
    }

    public static int alpha(float alpha, int color) {
        return alpha((int) (255 * alpha), color);
    }
}
