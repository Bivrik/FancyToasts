package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.gui.ForgeConfigScreen;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class FancyToasts {

    public FancyToasts() {
        Common.onModInitialization();

        ForgeConfigScreen.registerConfigScreen(ModLoadingContext.get());
    }
}
