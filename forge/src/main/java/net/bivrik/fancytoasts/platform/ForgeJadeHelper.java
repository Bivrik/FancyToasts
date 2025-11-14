package net.bivrik.fancytoasts.platform;

import net.bivrik.fancytoasts.platform.services.IJadeHelper;

public class ForgeJadeHelper implements IJadeHelper {
    @Override
    public void tryDisable() {}

    @Override
    public void tryEnable() {}

    @Override
    public boolean isEnabled() {
        return false;
    }
}
