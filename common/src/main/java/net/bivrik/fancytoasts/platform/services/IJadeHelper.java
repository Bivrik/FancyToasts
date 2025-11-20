package net.bivrik.fancytoasts.platform.services;

import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.platform.Services;

public interface IJadeHelper {
    /**
     * Tries to disable Jade through its config if it's enabled
     */
    default void tryDisable() {}

    /**
     * Tries to enable Jade through its config if it's disabled
     */
    default void tryEnable() {}

    /**
     * Checks if Jade is enabled or disabled through its config
     * @return true if enabled, false otherwise
     */
    default boolean isEnabled() {
        return false;
    }

    default boolean isLoaded() {
        return Services.PLATFORM.isModLoaded(Constants.Compatibilities.JADE_ID);
    }
}
