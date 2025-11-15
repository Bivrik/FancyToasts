package net.bivrik.fancytoasts.client.toast;

import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.toast.texture.DisplayData;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.*;

public class TextureRegistry {
    private static final Logger LOGGER = Debug.getLogger(TextureRegistry.class);

    private static final Map<ResourceLocation, DisplayData> TEXTURES = new HashMap<>();

    public static boolean register(ResourceLocation id, String originId, DisplayData data) {
        if (TEXTURES.containsKey(id)) {
            LOGGER.warn("{} already exists! It has to be unique", id);
            return false;
        }
        
        TEXTURES.put(id, data);

        LOGGER.info("Registered {} from {}", id, originId);
        return true;
    }

    public static boolean register(ResourceLocation id, String originId, String name, String author, String description) {
        return register(id, originId, new DisplayData(name, author, description, true));
    }

    public static boolean isRegistered(ResourceLocation id) {
        return TEXTURES.getOrDefault(id, null) != null;
    }
    
    public static void clearCustom() {
        getIds().removeIf(id -> id.toLanguageKey().contains("custom")); // Don't forget to change `config` to `custom` EVERYWHERE, you idiot
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
