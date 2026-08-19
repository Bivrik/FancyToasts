package net.bivrik.fancytoasts.client.config.data;

import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.registry.AnimationRegistry;
import net.bivrik.fancytoasts.client.registry.TextureRegistry;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.platform.utility.FancyToastType;
import net.bivrik.fancytoasts.utility.DefaultIdentifiers;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class ToastConfigData extends ConfigData {
    private static final int VERSION = Constants.ConfigVersions.TOAST;

    private Identifier textureId;
    private Identifier animationId;
    private final Map<FancyToastType, Identifier> soundIds = new EnumMap<>(FancyToastType.class);

    private ToastConfigData(Identifier textureId, Identifier animationId, Map<FancyToastType, Identifier> soundIds) {
        super(Paths.TOAST_CONFIG_FILE);

        this.textureId = textureId;
        this.animationId = animationId;
        this.soundIds.putAll(soundIds);
    }

    public ToastConfigData() {
        this(DefaultIdentifiers.Textures.VANILLA, DefaultIdentifiers.Animations.STANDARD, Map.of(
                FancyToastType.TASK, SoundEvents.NOTE_BLOCK_CHIME.value().location(),
                FancyToastType.GOAL, SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR.location(),
                FancyToastType.CHALLENGE, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.location())
        );
    }

    public Identifier getTextureId() {
        if (isConfig(textureId)) {
            if (!TextureRegistry.isRegistered(textureId)) {
                Identifier standardTextureId = new ToastConfigData().textureId;
                setTextureId(standardTextureId);
                ConfigHandler.save(copy());
                return standardTextureId;
            }
        }

        return textureId;
    }
    public void setTextureId(Identifier id) {
        textureId = id;
    }

    public Identifier getAnimationId() {
        return animationId;
    }
    public void setAnimationId(Identifier id) {
        animationId = id;
    }

    public Identifier getSoundIdByType(FancyToastType type) {
        var availableSounds = Minecraft.getInstance().getSoundManager().getAvailableSounds();
        Identifier soundId = soundIds.get(type);

        if (!availableSounds.contains(soundId)) {
            Identifier standardSoundId = new ToastConfigData().soundIds.get(type);
            putSoundIdForType(standardSoundId, type);
            ConfigHandler.save(copy());
            return standardSoundId;
        }

        return soundIds.get(type);
    }
    public void putSoundIdForType(Identifier location, FancyToastType type) {
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

    private boolean isConfig(Identifier id) {
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
