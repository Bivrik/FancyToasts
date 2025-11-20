package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.gui.ForgeConfigScreen;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class FancyToasts {

    public FancyToasts(FMLJavaModLoadingContext context) {
        Common.onModInit();

        ForgeConfigScreen.registerConfigScreen(context);
    }
}
