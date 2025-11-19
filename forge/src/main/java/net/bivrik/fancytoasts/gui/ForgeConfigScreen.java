package net.bivrik.fancytoasts.gui;

import net.bivrik.fancytoasts.client.gui.FancyToastConfigScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class ForgeConfigScreen {

    public static void registerConfigScreen(ModLoadingContext context) {
        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        FancyToastConfigScreen::new
                )
        );
    }
}
