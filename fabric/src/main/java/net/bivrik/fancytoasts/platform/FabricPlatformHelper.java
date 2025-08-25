package net.bivrik.fancytoasts.platform;

import net.bivrik.fancytoasts.compat.JadeCompat;
import net.bivrik.fancytoasts.platform.services.IPlatformHelper;
import net.fabricmc.loader.api.FabricLoader;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public void tryDisableJade() {
        JadeCompat.tryDisableJade();
    }

    @Override
    public void tryEnableJade() {
        JadeCompat.tryEnableJade();
    }
}
