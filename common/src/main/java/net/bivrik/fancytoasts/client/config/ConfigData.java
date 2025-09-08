package net.bivrik.fancytoasts.client.config;

import net.minecraft.advancements.AdvancementType;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumMap;
import java.util.Map;

public class ConfigData {
    private ResourceLocation animationId;
    private ResourceLocation textureId;
    private final Map<AdvancementType, ResourceLocation> soundIds = new EnumMap<>(AdvancementType.class);

    public ConfigData(ResourceLocation animationId, ResourceLocation textureId, Map<AdvancementType, ResourceLocation> soundIds) {
        this.animationId = animationId;
        this.textureId = textureId;
        this.soundIds.putAll(soundIds);
    }

    public ResourceLocation getAnimationId() {
        return animationId;
    }

    public void setAnimationId(ResourceLocation id) {
        animationId = id;
    }

    public ResourceLocation getTextureId() {
        return textureId;
    }

    public void setTextureId(ResourceLocation id) {
        textureId = id;
    }

    public ResourceLocation getSoundId(AdvancementType type) {
        return soundIds.get(type);
    }

    public void putSound(AdvancementType type, ResourceLocation location) {
        soundIds.put(type, location);
    }

    public ConfigData get() {
        return new ConfigData(this.animationId, this.textureId, this.soundIds);
    }
}
