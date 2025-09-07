package net.bivrik.fancytoasts.client.config;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.util.FileHelper;
import net.bivrik.fancytoasts.client.util.Paths;
import net.bivrik.fancytoasts.client.toast.registry.ToastAnimationRegistry;
import net.bivrik.fancytoasts.client.util.TextureLocations;
import net.bivrik.fancytoasts.client.toast.registry.ToastTextureRegistry;
import net.bivrik.fancytoasts.client.util.ResLoc;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.sounds.SoundEvents;

import java.io.File;
import java.util.Map;
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
        return STANDARD_DATA;
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

    private static void showData(ConfigData data) {
        Debug.message("===========================");
        Debug.message("Animation ID: " + data.getAnimationId());
        Debug.message("Texture ID: " + data.getTextureId());
        Debug.message("Task Sound ID: " + data.getSoundId(AdvancementType.TASK));
        Debug.message("Goal Sound ID: " + data.getSoundId(AdvancementType.GOAL));
        Debug.message("Challenge Sound ID: " + data.getSoundId(AdvancementType.CHALLENGE));
        Debug.message("===========================");
    }

    public static final ConfigData STANDARD_DATA = new ConfigData(
            ResLoc.of("animation/standard"),
            TextureLocations.VANILLA,
            Map.of(
                    AdvancementType.TASK, SoundEvents.ALLAY_AMBIENT_WITH_ITEM.location(),
                    AdvancementType.GOAL, SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR.location(),
                    AdvancementType.CHALLENGE, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.location()
            )
    );
}
