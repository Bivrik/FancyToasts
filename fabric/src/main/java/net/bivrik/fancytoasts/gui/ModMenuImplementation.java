package net.bivrik.fancytoasts.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.client.gui.FancyToastConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ModMenuImplementation implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> new FancyToastConfigScreen(Component.translatable(Constants.MOD_ID + ".gui.config.title"), screen);
    }
}
