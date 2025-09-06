package net.bivrik.fancytoasts.gui;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.client.gui.FancyToastConfigScreen;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class NeoforgeConfigScreen  {

    public static void registerConfigScreen(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, screen) -> new FancyToastConfigScreen(Constants.MOD_ID, screen)
        );
    }
}
