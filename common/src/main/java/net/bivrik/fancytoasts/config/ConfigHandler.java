package net.bivrik.fancytoasts.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.client.toast.animation.AnimationType;
import net.bivrik.fancytoasts.texture.TextureType;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigHandler {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("./config/" + Constants.MOD_ID + ".json");

    public static ConfigData load() {
        ConfigData data;

        if (CONFIG_FILE.exists() && CONFIG_FILE.length() > 0) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                data = GSON.fromJson(reader, ConfigData.class);
                Constants.LOGGER.info(Constants.MOD_ID + " config file loaded.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            data = new ConfigData(AnimationType.STANDARD, TextureType.VANILLA);
            save(data);
            Constants.LOGGER.info(Constants.MOD_ID + " config file created.");
        }

        return data;
    }


    public static void save(AnimationType animationType, TextureType textureType) {
        var data = new ConfigData(animationType, textureType);

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(data, writer);
            Constants.LOGGER.info(Constants.MOD_ID + " config file saved.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Common.CONFIG = data;
    }
    public static void save(ConfigData data) {
        save(data.getAnimationType(), data.getTextureType());
    }
}
