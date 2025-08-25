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
        if (!isLoaded() || !isEnabled) {
            return;
        }

        toggleJade();
    }

    public static void tryEnableJade() {
        if (!isLoaded() || isEnabled) {
            return;
        }

        toggleJade();
    }

    private static void toggleJade() {
        isEnabled = !isEnabled;
        WailaConfig config = Jade.config();
        config.general().setDisplayTooltip(isEnabled);
    }
}
