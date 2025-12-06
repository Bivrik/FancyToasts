package net.bivrik.fancytoasts.client.config.data;

import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.registry.AnimationRegistry;
import net.bivrik.fancytoasts.client.registry.TextureRegistry;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.platform.utility.FancyToastType;
import net.bivrik.fancytoasts.utility.DefaultLocations;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class ToastConfigData extends ConfigData {
    private static final int VERSION = Constants.ConfigVersions.TOAST;

    private ResourceLocation textureId;
    private ResourceLocation animationId;
    private final Map<FancyToastType, ResourceLocation> soundIds = new EnumMap<>(FancyToastType.class);

    private ToastConfigData(ResourceLocation textureId, ResourceLocation animationId, Map<FancyToastType, ResourceLocation> soundIds) {
        super(Paths.TOAST_CONFIG_FILE);

        this.textureId = textureId;
        this.animationId = animationId;
        this.soundIds.putAll(soundIds);
    }

    public ToastConfigData() {
        this(DefaultLocations.Textures.VANILLA, DefaultLocations.Animations.STANDARD, Map.of(
                FancyToastType.TASK, SoundEvents.ALLAY_AMBIENT_WITH_ITEM.location(),
                FancyToastType.GOAL, SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR.location(),
                FancyToastType.CHALLENGE, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.location())
        );
    }

    public ResourceLocation getTextureId() {
        if (isConfig(textureId)) {
            if (!TextureRegistry.isRegistered(textureId)) {
                ResourceLocation standardTextureId = new ToastConfigData().textureId;
                setTextureId(standardTextureId);
                ConfigHandler.save(copy());
                return standardTextureId;
            }
        }

        return textureId;
    }
    public void setTextureId(ResourceLocation id) {
        textureId = id;
    }

    public ResourceLocation getAnimationId() {
        return animationId;
    }
    public void setAnimationId(ResourceLocation id) {
        animationId = id;
    }

    public ResourceLocation getSoundIdByType(FancyToastType type) {
        var availableSounds = Minecraft.getInstance().getSoundManager().getAvailableSounds();
        ResourceLocation soundId = soundIds.get(type);

        if (!availableSounds.contains(soundId)) {
            ResourceLocation standardSoundId = new ToastConfigData().soundIds.get(type);
            putSoundIdForType(standardSoundId, type);
            ConfigHandler.save(copy());
            return standardSoundId;
        }

        return soundIds.get(type);
    }
    public void putSoundIdForType(ResourceLocation location, FancyToastType type) {
        soundIds.put(type, location);
    }

    @Override
    public boolean isValid() {
        boolean isValid = true;

        if (!isConfig(animationId)) {
            isValid = AnimationRegistry.isRegistered(animationId);
        }

        if (!isConfig(textureId)) {
            isValid = isValid && TextureRegistry.isRegistered(textureId);
        }

        return isValid;
    }

    @Override
    public int getLatestVersion() {
        return VERSION;
    }

    private boolean isConfig(ResourceLocation id) {
        return id.toLanguageKey().contains(Constants.CONFIG);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ToastConfigData that)) return false;
        return Objects.equals(textureId, that.textureId) && Objects.equals(animationId, that.animationId) && Objects.equals(soundIds, that.soundIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(textureId, animationId, soundIds);
    }

    @Override
    public ToastConfigData copy() {
        return new ToastConfigData(textureId, animationId, soundIds).withLatestVersion();
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ", ") + String.format(
                "textureId='%s', animationId='%s', soundIds='%s'}",
                textureId, animationId, soundIds
        );
    }
}
