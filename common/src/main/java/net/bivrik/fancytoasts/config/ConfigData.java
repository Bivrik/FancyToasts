package net.bivrik.fancytoasts.config;

import net.bivrik.fancytoasts.client.toast.animation.AnimationType;
import net.bivrik.fancytoasts.texture.TextureType;

public class ConfigData {
    private final AnimationType animationType;
    private final TextureType textureType;

    public ConfigData(AnimationType animationType, TextureType textureType) {
        this.animationType = animationType;
        this.textureType = textureType;
    }

    public AnimationType getAnimationType() {
        return animationType;
    }

    public TextureType getTextureType() {
        return textureType;
    }
}
