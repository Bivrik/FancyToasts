package net.bivrik.fancytoasts.client.config;

import net.minecraft.resources.ResourceLocation;

public class ConfigData {
    private final ResourceLocation animationId;
    private final ResourceLocation textureId;
    private final boolean jadeCompatibility;

    public ConfigData(ResourceLocation animationId, ResourceLocation textureId, boolean jadeCompatibility) {
        this.animationId = animationId;
        this.textureId = textureId;
        this.jadeCompatibility = jadeCompatibility;
    }

    public ResourceLocation getAnimationId() {
        return animationId;
    }

    public ResourceLocation getTextureId() {
        return textureId;
    }

    public boolean getJadeCompatibility() {
        return jadeCompatibility;
    }
}
