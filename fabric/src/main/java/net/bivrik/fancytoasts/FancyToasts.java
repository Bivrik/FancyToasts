package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.core.Common;
import net.fabricmc.api.ModInitializer;

public class FancyToasts implements ModInitializer {

    @Override
    public void onInitialize() {
        Common.onModInit();
    }
}
