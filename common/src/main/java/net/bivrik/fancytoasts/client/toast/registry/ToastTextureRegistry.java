package net.bivrik.fancytoasts.client.toast.registry;

import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.toast.texture.ToastTextureData;
import net.bivrik.fancytoasts.client.util.FileType;
import net.bivrik.fancytoasts.utility.ComponentHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ToastTextureRegistry {
    private static final Map<ResourceLocation, ToastTextureData> TEXTURES = new HashMap<>();

    public static boolean register(ResourceLocation id, String modId, String name, String author, String description) {
        if (TEXTURES.containsKey(id)) {
            Debug.error("{} already exists! It needs to be unique", id);
            return false;
        }

        Component componentName;
        Component translatableDescription;
        if (modId != null) {
            componentName = ComponentHelper.getTranslatableToastTexture(modId, name);
            translatableDescription = Component.translatable(description);
        }
        else {
            componentName = Component.literal(name);
            translatableDescription = Component.literal(description);
        }

        var data = new ToastTextureData(componentName, author, translatableDescription);
        TEXTURES.put(id, data);

        Debug.info("Registered {}", id);
        return true;
    }

    public static boolean register(ResourceLocation id, String modId, String name, String author) {
        return register(id, modId, name, author, modId + ".textures.toast." + name + ".description");
    }

    public static void clearCustom() {
        getIds().removeIf(id -> id.toString().contains("config"));
    }

    public static ToastTextureData getData(ResourceLocation id) {
        return TEXTURES.computeIfAbsent(id, key -> {
            Debug.error("Texture {} is missing", key);
            return new ToastTextureData(Component.translatable("fancytoasts.textures.toast.vanilla"), "Fancy Toasts", Component.translatable("fancytoasts.textures.vanilla.description"));
        });
    }

    public static boolean isRegistered(ResourceLocation id) {
        return TEXTURES.getOrDefault(id, null) != null;
    }

    public static Component getTextureName(ResourceLocation id) {
        return getData(id).getName();
    }

    public static Component getTextureAuthor(ResourceLocation id) {
        return getData(id).getAuthor();
    }

    public static Collection<ResourceLocation> getIds() {
        return TEXTURES.keySet();
    }
}
