package net.bivrik.fancytoasts.client.registry;

import net.bivrik.fancytoasts.client.toast.DisplayData;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.core.Debug;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TextureRegistry {
    private static final Logger LOGGER = Debug.getLogger(TextureRegistry.class);

    private static final Map<Identifier, DisplayData> TEXTURES = new HashMap<>();

    private static void addTexture(Identifier id, DisplayData data) {
        TEXTURES.put(id, data);
    }

    public static boolean register(Identifier id, DisplayData data) {
        if (isRegistered(id)) {
            LOGGER.warn("{} already exists! It has to be unique", id);
            return false;
        }
        
        addTexture(id, data);

        LOGGER.info("Registered: {}", id);
        return true;
    }

    public static boolean register(Identifier id, String name, String author, String description) {
        return register(id, new DisplayData(name, author, description, true));
    }

    public static void unregister(Identifier id) {
        TEXTURES.remove(id);
        LOGGER.info("Unregistered: {}", id);
    }

    public static boolean isRegistered(Identifier id) {
        return TEXTURES.getOrDefault(id, null) != null;
    }

    public static DisplayData getData(Identifier id) {
        DisplayData data = TEXTURES.getOrDefault(id, null);
        if (data == null) {
            LOGGER.error("{} is missing, using default", id);
            return getDefaultData();
        }

        return data;
    }
    
    public static DisplayData getDefaultData() {
        return new DisplayData("fancytoasts.gui.unknown", "fancytoasts.gui.unknown", "fancytoasts.gui.unknown", true);
    }

    public static Collection<Identifier> getIds() {
        return TEXTURES.keySet();
    }

    public static List<Identifier> getCustomIds() {
        return TEXTURES.keySet().stream().filter(id -> id.getPath().contains(Constants.CONFIG)).collect(Collectors.toList());
    }
}
