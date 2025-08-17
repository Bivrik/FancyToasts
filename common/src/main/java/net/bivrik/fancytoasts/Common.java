package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.config.ConfigData;
import net.bivrik.fancytoasts.config.ConfigHandler;
import net.bivrik.fancytoasts.platform.Services;

// Only Vanilla code base and java
public class Common {
    public static ConfigData CONFIG;

    public static void init() {
        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            CONFIG = ConfigHandler.load();

            Constants.LOGGER.info("Common init on {} in a {} environment.", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());
        }
    }
}
