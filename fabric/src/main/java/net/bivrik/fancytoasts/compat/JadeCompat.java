package net.bivrik.fancytoasts.compat;

import net.bivrik.fancytoasts.platform.Services;
import snownee.jade.Jade;
import snownee.jade.impl.config.WailaConfig;

public class JadeCompat {
    private static boolean isEnabled = false;

    private static boolean isLoaded() {
        return Services.PLATFORM.isModLoaded("jade");
    }

    public static void tryDisableJade() {
        if (!isLoaded()) {
            return;
        }

        toggleJade(false);
    }

    public static void tryEnableJade() {
        if (!isLoaded()) {
            return;
        }

        toggleJade(true);
    }

    public static boolean isJadeEnabled() {
        return isEnabled;
    }

    private static void toggleJade(boolean value) {
        isEnabled = value;
        WailaConfig config = Jade.config();
        config.general().setDisplayTooltip(isEnabled);
    }
}
