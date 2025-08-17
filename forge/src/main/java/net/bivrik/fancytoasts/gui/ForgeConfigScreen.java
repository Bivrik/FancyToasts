package net.bivrik.fancytoasts.gui;

import net.bivrik.fancytoasts.Constants;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

public class ForgeConfigScreen {

    public static void registerConfigScreen(ModLoadingContext context) {
        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        (parent) -> new ConfigScreen(Constants.MOD_ID, parent)
                )
        );
    }
}
