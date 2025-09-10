package net.bivrik.fancytoasts.gui;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.client.gui.FancyToastConfigScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public class NeoforgeConfigScreen  {

    public static void registerConfigScreen(FMLClientSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                () -> (modContainer, screen) -> new FancyToastConfigScreen(Component.translatable(Constants.MOD_ID + ".gui.config.title"), screen)
        );
    }
}
