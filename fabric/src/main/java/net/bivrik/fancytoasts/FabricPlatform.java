package net.bivrik.fancytoasts;

import net.fabricmc.api.ModInitializer;

public class FabricPlatform implements ModInitializer {

    @Override
    public void onInitialize() {
        FancyToasts.getInstance().onModInit();
    }
}
