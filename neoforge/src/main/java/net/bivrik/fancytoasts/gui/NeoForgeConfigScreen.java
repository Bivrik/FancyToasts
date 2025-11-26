package net.bivrik.fancytoasts.gui;

import net.bivrik.fancytoasts.client.gui.screen.FancyToastsConfigScreen;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class NeoForgeConfigScreen {
    public static void registerConfigScreen(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, screen) -> new FancyToastsConfigScreen(screen)
        );
    }
}
