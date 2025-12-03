package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.core.Common;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.gui.ForgeConfigScreen;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class FancyToasts {
    public FancyToasts() {
        Common.onModInit();

        ForgeConfigScreen.registerConfigScreen(ModLoadingContext.get());
    }
}
