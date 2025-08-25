package net.bivrik.fancytoasts.client.config;

import net.minecraft.resources.ResourceLocation;

public class ConfigData {
    private final ResourceLocation animationId;
    private final ResourceLocation textureId;

    public ConfigData(ResourceLocation animationId, ResourceLocation textureId) {
        this.animationId = animationId;
        this.textureId = textureId;
    }

    public ResourceLocation getAnimationId() {
        return animationId;
    }

    public ResourceLocation getTextureId() {
        return textureId;
    }
}
