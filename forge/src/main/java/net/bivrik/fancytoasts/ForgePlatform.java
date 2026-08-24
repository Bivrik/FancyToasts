package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.gui.ForgeConfigScreen;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Constants.MOD_ID)
public class ForgePlatform {
    public ForgePlatform() {
        FancyToasts.getInstance().onModInit();

        ForgeConfigScreen.registerConfigScreen(ModLoadingContext.get());
    }
}
