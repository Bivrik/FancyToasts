package net.bivrik.fancytoasts.platform;

import net.bivrik.fancytoasts.compat.JadeCompat;
import net.bivrik.fancytoasts.platform.services.IJadeHelper;

public class FabricJadeHelper implements IJadeHelper {
    @Override
    public void tryDisable() {
        if (isLoaded()) {
            JadeCompat.tryDisable();
        }
    }

    @Override
    public void tryEnable() {
        if (isLoaded()) {
            JadeCompat.tryEnable();
        }
    }

    @Override
    public boolean isEnabled() {
        if (!isLoaded()) {
            return IJadeHelper.super.isEnabled();
        }

        return JadeCompat.isEnabled();
    }
}
