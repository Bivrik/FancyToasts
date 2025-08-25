package net.bivrik.fancytoasts.client.config;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.util.FileHelper;
import net.bivrik.fancytoasts.client.util.Paths;
import net.bivrik.fancytoasts.client.toast.registry.ToastAnimationRegistry;
import net.bivrik.fancytoasts.client.util.TextureLocations;
import net.bivrik.fancytoasts.client.toast.registry.ToastTextureRegistry;
import net.bivrik.fancytoasts.client.util.ResLoc;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.util.Optional;

public class ConfigHandler {
    private static final File CONFIG_DIR = new File(Paths.CONFIG);
    private static final File CONFIG_FILE = new File(Paths.CONFIG_FILE);

    public static ConfigData load() {
        FileHelper.tryCreateDir(CONFIG_DIR);

        ConfigData data;
        if (JsonHelper.isValid(CONFIG_FILE)) {
            Optional<ConfigData> optionalData = JsonHelper.tryToRead(CONFIG_FILE, ConfigData.class);

            if (optionalData.isPresent()) {
                data = optionalData.get();

                if (ToastTextureRegistry.isRegistered(data.getTextureId())
                        && ToastAnimationRegistry.isRegistered(data.getAnimationId())) {

                    Debug.message("Config file loaded with following data:");
                    showData(data);

                    return data;
                }
            }
        }

        Debug.error("Config file is outdated or corrupted!");
        data = getStandardData();
        save(data);

        return data;
    }

    private static ConfigData getStandardData() {
        Debug.warn("Default data is created");
        return new ConfigData(
                ResLoc.of("animation/standard"),
                TextureLocations.VANILLA);
    }

    public static void save(ConfigData data) {
        FileHelper.tryCreateDir(CONFIG_DIR);;

        if (JsonHelper.tryToWrite(CONFIG_FILE, data)) {
            Debug.message("Config file saved with following data:");
            showData(data);
        }
        else {
            Debug.error("Could not save config file!");
        }

        Common.CONFIG = data;
    }
    public static void save(ResourceLocation animationId, ResourceLocation textureId) {
        save(new ConfigData(animationId, textureId));
    }

    private static void showData(ConfigData data) {
        Debug.message("===========================");
        Debug.message("Animation ID: " + data.getAnimationId());
        Debug.message("Texture ID: " + data.getTextureId());
        Debug.message("===========================");
    }
}
