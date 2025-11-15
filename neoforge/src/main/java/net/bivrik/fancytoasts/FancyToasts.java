package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.gui.NeoforgeConfigScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class FancyToasts {

    public FancyToasts(IEventBus eventBus) {
        Common.onModInit();

        eventBus.addListener(NeoforgeConfigScreen::registerConfigScreen);
    }
}
