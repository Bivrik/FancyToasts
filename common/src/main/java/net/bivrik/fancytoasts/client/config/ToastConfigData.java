package net.bivrik.fancytoasts.client.config;

import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.toast.ToastAnimationRegistry;
import net.bivrik.fancytoasts.client.toast.ToastTextureRegistry;
import net.bivrik.fancytoasts.utility.DefaultLocations;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

import java.io.File;
import java.nio.charset.MalformedInputException;
import java.util.EnumMap;
import java.util.Map;

public class ToastConfigData extends ConfigData {
    private ResourceLocation animationId;
    private ResourceLocation textureId;
    private final Map<AdvancementType, ResourceLocation> soundIds = new EnumMap<>(AdvancementType.class);

    public ToastConfigData(ResourceLocation animationId, ResourceLocation textureId, Map<AdvancementType, ResourceLocation> soundIds) {
        super(Paths.TOAST_CONFIG_FILE);

        this.animationId = animationId;
        this.textureId = textureId;
        this.soundIds.putAll(soundIds);
    }

    public ToastConfigData() {
        super(Paths.TOAST_CONFIG_FILE);

        this.animationId = DefaultLocations.Animations.STANDARD;
        this.textureId = DefaultLocations.Textures.VANILLA;
        this.soundIds.putAll(Map.of(
                AdvancementType.TASK, SoundEvents.ALLAY_AMBIENT_WITH_ITEM.location(),
                AdvancementType.GOAL, SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR.location(),
                AdvancementType.CHALLENGE, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.location())
        );
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
        var soundManager = Minecraft.getInstance().getSoundManager();

        if (!soundManager.getAvailableSounds().contains(soundIds.get(type))) {
            Debug.warn("Saved sounds are invalid. Used standard ones");
            switch (type) {
                case TASK -> {
                    return SoundEvents.ALLAY_AMBIENT_WITH_ITEM.location();
                }
                case GOAL -> {
                    return SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR.location();
                }
                case null, default -> {
                    return SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.location();
                }
            }
        }

        return soundIds.get(type);
    }
    public void putSound(AdvancementType type, ResourceLocation location) {
        soundIds.put(type, location);
    }

    @Override
    public String getPath() {
        File oldPathConfigFile = new File(Paths.OLD_TOAST_CONFIG_FILE);
        oldPathConfigFile.delete();

        return super.getPath();
    }

    @Override
    public boolean isValid() {
        if (ToastTextureRegistry.isRegistered(textureId)) {
            if (textureId.toLanguageKey().contains("config")) {
                ConfigTextureManager.registerInMinecraft(textureId);
            }
        }
        else {
            return false;
        }

        return ToastAnimationRegistry.isRegistered(animationId);
    }

    @Override
    public ToastConfigData get() {
        return new ToastConfigData(animationId, textureId, soundIds);
    }

    @Override
    public String toString() {
        return super.toString().replace("}", ", ") + String.format("animationId='%s', textureId='%s', soundIds='%s'}", animationId, textureId, soundIds);
    }
}
