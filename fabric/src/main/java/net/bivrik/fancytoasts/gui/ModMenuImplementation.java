package net.bivrik.fancytoasts.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.client.gui.ConfigScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuImplementation implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> new ConfigScreen(Constants.MOD_ID, screen);
    }
}
