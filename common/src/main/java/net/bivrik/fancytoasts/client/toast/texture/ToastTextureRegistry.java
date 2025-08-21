package net.bivrik.fancytoasts.client.toast.texture;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.Debug;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ToastTextureRegistry {
    private static final Map<ResourceLocation, ToastTextureHandler> TEXTURES = new HashMap<>();

    public static void register(ResourceLocation id, ResourceLocation texture, String modId, String name) {
        if (TEXTURES.containsKey(id)) {
            Debug.error("{} already exists! It needs to be unique.", id);
            return;
        }

        Component nameComponent = getComponent(modId, name);
        TEXTURES.put(id, new ToastTextureHandler(nameComponent, texture));

        Debug.message("Registered {} with {} location.", id, texture);
    }

    private static Component getComponent(String modId, String name) {
        StringBuilder translationKey = new StringBuilder();
        translationKey.append(Constants.MOD_ID);
        if (!modId.equals(Constants.MOD_ID)) {
            translationKey.append(".").append(modId);
        }
        translationKey.append(".texture.").append(name);

        return Component.translatable(translationKey.toString());
    }

    private static ToastTextureHandler getTextureHandler(ResourceLocation id) {
        return TEXTURES.computeIfAbsent(id, key -> {
            Debug.error("{} is missing.", key);
            return new ToastTextureHandler(getComponent(Constants.MOD_ID, "vanilla"), TextureLocations.VANILLA);
        });
    }

    public record ToastTextureHandler(Component name, ResourceLocation texture) {}

    public static Component getTextureName(ResourceLocation id) {
        return getTextureHandler(id).name();
    }

    public static ResourceLocation getTexture(ResourceLocation id) {
        return getTextureHandler(id).texture();
    }

    public static Collection<ResourceLocation> getIds() {
        return TEXTURES.keySet();
    }
}
