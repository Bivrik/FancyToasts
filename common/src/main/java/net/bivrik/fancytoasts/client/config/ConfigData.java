package net.bivrik.fancytoasts.client.config;

import net.minecraft.resources.ResourceLocation;

public class ConfigData {
    private final ResourceLocation animationId;
    private final ResourceLocation textureId;
    private final ResourceLocation[] soundIds = new ResourceLocation[3];

    public ConfigData(ResourceLocation animationId, ResourceLocation textureId, ResourceLocation[] soundIds) {
        this.animationId = animationId;
        this.textureId = textureId;
        System.arraycopy(soundIds, 0, this.soundIds, 0, this.soundIds.length);
    }

    public ResourceLocation getAnimationId() {
        return animationId;
    }

    public ResourceLocation getTextureId() {
        return textureId;
    }

    public ResourceLocation[] getSoundIds() {
        return soundIds;
    }

    public ResourceLocation getTaskSoundId() {
        return soundIds[0];
    }

    public ResourceLocation getGoalSoundId() {
        return soundIds[1];
    }

    public ResourceLocation getChallengeSoundId() {
        return soundIds[2];
    }
}
