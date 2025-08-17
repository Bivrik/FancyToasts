package net.bivrik.fancytoasts.texture;

import net.bivrik.fancytoasts.Constants;
import net.minecraft.resources.ResourceLocation;

public class TextureLocations {
    public static final ResourceLocation VANILLA = getFromPath("textures/gui/advancement_toasts.png");
    public static final ResourceLocation NATURE = getFromPath("textures/gui/nature_advancement_toasts.png");
    public static final ResourceLocation OG = getFromPath("textures/gui/og_advancement_toasts.png");
    public static final ResourceLocation MODERN = getFromPath("textures/gui/modern_advancement_toasts.png");

    private static ResourceLocation getFromPath(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
