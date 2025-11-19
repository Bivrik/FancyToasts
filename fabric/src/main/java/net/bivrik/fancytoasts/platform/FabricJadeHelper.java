package net.bivrik.fancytoasts.platform;

import net.bivrik.fancytoasts.compat.JadeCompat;
import net.bivrik.fancytoasts.platform.services.IJadeHelper;

public class FabricJadeHelper implements IJadeHelper {
    @Override
    public void tryDisable() {
        JadeCompat.tryDisable();
    }

    @Override
    public void tryEnable() {
        JadeCompat.tryEnable();
    }

    @Override
    public boolean isEnabled() {
        return JadeCompat.isEnabled();
    }
}
