package net.bivrik.fancytoasts.config;

import net.bivrik.fancytoasts.client.toast.animation.AnimationType;
import net.minecraft.resources.ResourceLocation;

public class ConfigData {
    private final AnimationType animationType;
    private final ResourceLocation textureId;

    public ConfigData(AnimationType animationType, ResourceLocation textureId) {
        this.animationType = animationType;
        this.textureId = textureId;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public ResourceLocation getTextureId() {
        return textureId;
    }
}
