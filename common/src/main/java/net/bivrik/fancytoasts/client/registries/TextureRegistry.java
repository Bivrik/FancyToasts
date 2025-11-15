package net.bivrik.fancytoasts.client.registries;

import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.toast.DisplayData;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.*;

public class TextureRegistry {
    private static final Logger LOGGER = Debug.getLogger(TextureRegistry.class);

    private static final Map<ResourceLocation, DisplayData> TEXTURES = new HashMap<>();

    private static void addTexture(ResourceLocation id, DisplayData data) {
        TEXTURES.put(id, data);
    }

    public static boolean register(ResourceLocation id, DisplayData data) {
        if (TEXTURES.containsKey(id)) {
            LOGGER.warn("{} already exists! It has to be unique", id);
            return false;
        }
        
        addTexture(id, data);

        LOGGER.info("Registered: {}", id);
        return true;
    }

    public static boolean register(ResourceLocation id, String name, String author, String description) {
        return register(id, new DisplayData(name, author, description, true));
    }

    public static boolean isRegistered(ResourceLocation id) {
        return TEXTURES.getOrDefault(id, null) != null;
    }
    
    public static void clearCustom() {
        getIds().removeIf(id -> id.toLanguageKey().contains("config")); // Don't forget to change `config` to `custom` EVERYWHERE, you idiot
    }

    public static DisplayData getData(ResourceLocation id) {
        return TEXTURES.computeIfAbsent(id, key -> {
            LOGGER.error("{} is missing", key);
            return getDefaultData();
        });
    }
    
    public static DisplayData getDefaultData() {
        return new DisplayData("fancytoasts.gui.unknown", "fancytoasts.gui.unknown", "fancytoasts.gui.unknown", true);
    }

    public static Collection<ResourceLocation> getIds() {
        return TEXTURES.keySet();
    }
}
