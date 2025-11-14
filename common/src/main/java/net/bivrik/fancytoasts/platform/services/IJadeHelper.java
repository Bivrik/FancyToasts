package net.bivrik.fancytoasts.platform.services;

public interface IJadeHelper {
    /**
     * Tries to disable Jade through its config if it's enabled
     */
    void tryDisable();

    /**
     * Tries to enable Jade through its config if it's disabled
     */
    void tryEnable();

    /**
     * Checks if Jade is enabled or disabled through its config
     * @return true if enabled, false otherwise
     */
    boolean isEnabled();
}
