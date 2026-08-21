package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.gui.NeoForgeConfigScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class NeoForgePlatform {

    public NeoForgePlatform(IEventBus eventBus) {
        FancyToasts.getInstance().onModInit();

        eventBus.addListener(NeoForgeConfigScreen::registerConfigScreen);
    }
}
