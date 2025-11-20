package net.bivrik.fancytoasts.compat;

import snownee.jade.Jade;
import snownee.jade.impl.config.WailaConfig;

public class JadeCompat {
    private static boolean isEnabled = false;

    public static void tryDisable() {
        toggle(false);
    }

    public static void tryEnable() {
        toggle(true);
    }

    public static boolean isEnabled() {
        return isEnabled;
    }

    private static void toggle(boolean value) {
        isEnabled = value;
        WailaConfig config = Jade.config();
        config.general().setDisplayTooltip(isEnabled);
    }
}
