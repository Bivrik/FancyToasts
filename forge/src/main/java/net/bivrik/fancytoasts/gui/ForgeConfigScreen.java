package net.bivrik.fancytoasts.gui;

import net.bivrik.fancytoasts.client.gui.screen.FancyToastsConfigScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ForgeConfigScreen {

    public static void registerConfigScreen(FMLJavaModLoadingContext context) {
        context.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory(
                        FancyToastsConfigScreen::new
                )
        );
    }
}
