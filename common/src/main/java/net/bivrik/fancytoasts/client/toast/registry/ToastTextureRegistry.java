package net.bivrik.fancytoasts.client.toast.registry;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.toast.texture.ToastTextureData;
import net.bivrik.fancytoasts.utility.ComponentHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ToastTextureRegistry {
    private static final Map<ResourceLocation, ToastTextureData> TEXTURES = new HashMap<>();

    public static boolean register(ResourceLocation id, String modId, String name, String author) {
        if (TEXTURES.containsKey(id)) {
            Debug.error("{} already exists! It needs to be unique", id);
            return false;
        }

        Component componentName;
        if (modId != null) {
            componentName = ComponentHelper.getTranslatableToastTexture(modId, name);
        }
        else {
            componentName = Component.literal(name);
        }

        var data = new ToastTextureData(componentName, author);
        TEXTURES.put(id, data);

        Debug.message("Registered {}", id);
        return true;
    }

    public static void clearCustom() {
        getIds().removeIf(id -> id.toString().contains("config"));
    }

    private static ToastTextureData getData(ResourceLocation id) {
        return TEXTURES.computeIfAbsent(id, key -> {
            Debug.error("Texture {} is missing", key);
            return new ToastTextureData(ComponentHelper.getTranslatableToastTexture(Constants.MOD_ID, "vanilla"), "Bivrik");
        });
    }

    public static boolean isRegistered(ResourceLocation id) {
        return TEXTURES.getOrDefault(id, null) != null;
    }

    public static Component getTextureName(ResourceLocation id) {
        return getData(id).name();
    }

    public static Component getTextureAuthor(ResourceLocation id) {
        return getData(id).author();
    }

    public static Collection<ResourceLocation> getIds() {
        return TEXTURES.keySet();
    }
}
