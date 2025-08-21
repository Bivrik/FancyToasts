package net.bivrik.fancytoasts.client.config;

import com.google.gson.*;
import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.toast.animation.AnimationType;
import net.bivrik.fancytoasts.utility.FancyResourceLocation;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public class ConfigHandler {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocationAdapter())
            .create();
    private static final File CONFIG_FILE = new File("./config/" + Constants.MOD_ID + ".json");

    public static ConfigData load() {
        ConfigData data;

        if (CONFIG_FILE.exists() && CONFIG_FILE.length() > 0) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                data = GSON.fromJson(reader, ConfigData.class);
                Debug.message("Config file loaded with data:");
                showDebug(data);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            data = new ConfigData(AnimationType.STANDARD, FancyResourceLocation.of("toast/vanilla"));
            save(data);
            Debug.message("Config file created with data:");
            showDebug(data);
        }

        return data;
    }

    public static void save(AnimationType animationType, ResourceLocation textureId) {
        var data = new ConfigData(animationType, textureId);

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(data, writer);
            Debug.message("Config file saved with data:");
            showDebug(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Common.CONFIG = data;
    }
    public static void save(ConfigData data) {
        save(data.getAnimationType(), data.getTextureId());
    }

    private static void showDebug(ConfigData data) {
        Debug.message("===================================");
        Debug.message("Animation Type: " + data.getAnimationType());
        Debug.message("Texture ID: " + data.getTextureId());
        Debug.message("===================================");
    }

    private static class ResourceLocationAdapter implements JsonSerializer<ResourceLocation>, JsonDeserializer<ResourceLocation> {
        @Override
        public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return ResourceLocation.parse(json.getAsString());
        }

        @Override
        public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }
}
