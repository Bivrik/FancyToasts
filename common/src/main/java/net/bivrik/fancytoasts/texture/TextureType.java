package net.bivrik.fancytoasts.texture;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public enum TextureType {
    VANILLA,
    NATURE,
    OG,
    MODERN;

    public static final Map<TextureType, ResourceLocation> TEXTURES = Map.of(
            TextureType.VANILLA, TextureLocations.VANILLA,
            TextureType.NATURE, TextureLocations.NATURE,
            TextureType.OG, TextureLocations.OG,
            TextureType.MODERN, TextureLocations.MODERN
    );
}
