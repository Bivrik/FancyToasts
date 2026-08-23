package net.bivrik.fancytoasts.client.config.data;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.registry.AnimationRegistry;
import net.bivrik.fancytoasts.client.registry.TextureRegistry;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.platform.utility.QuestType;
import net.bivrik.fancytoasts.platform.utility.FancyAdvancementType;
import net.bivrik.fancytoasts.utility.DefaultLocations;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

public class ToastConfigData extends ConfigData {
    private static final int VERSION = Constants.ConfigVersions.TOAST;

    private ResourceLocation textureId;
    private ResourceLocation animationId;
    private final Map<FancyAdvancementType, ResourceLocation> soundIds = new EnumMap<>(FancyAdvancementType.class);
    private final Map<QuestType, ResourceLocation> questSoundIds = new EnumMap<>(QuestType.class);

    private ToastConfigData(ResourceLocation textureId, ResourceLocation animationId, Map<FancyAdvancementType, ResourceLocation> soundIds, Map<QuestType, ResourceLocation> questSoundIds) {
        super(Paths.TOAST_CONFIG_FILE);

        this.textureId = textureId;
        this.animationId = animationId;
        this.soundIds.putAll(soundIds);
        this.questSoundIds.putAll(questSoundIds);
    }

    public ToastConfigData() {
        this(DefaultLocations.Textures.VANILLA, DefaultLocations.Animations.STANDARD,
                Map.of(
                        FancyAdvancementType.TASK, SoundEvents.NOTE_BLOCK_CHIME.value().getLocation(),
                        FancyAdvancementType.GOAL, SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR.getLocation(),
                        FancyAdvancementType.CHALLENGE, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.getLocation()),
                Map.of(
                        QuestType.TASK, SoundEvents.NOTE_BLOCK_BELL.value().getLocation(),
                        QuestType.QUEST, SoundEvents.NOTE_BLOCK_CHIME.value().getLocation(),
                        QuestType.CHAPTER, SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR.getLocation(),
                        QuestType.BOOK, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.getLocation())
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

    public ResourceLocation getSoundIdByType(FancyAdvancementType type) {
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

    public void putSoundIdForType(ResourceLocation location, FancyAdvancementType type) {
        soundIds.put(type, location);
    }

    public ResourceLocation getSoundIdByQuestType(QuestType type) {
        var availableSounds = Minecraft.getInstance().getSoundManager().getAvailableSounds();
        ResourceLocation soundId = questSoundIds.get(type);

        if (!availableSounds.contains(soundId)) {
            ResourceLocation standardSoundId = new ToastConfigData().questSoundIds.get(type);
            putSoundIdForQuestType(standardSoundId, type);
            ConfigHandler.save(copy());
            return standardSoundId;
        }

        return questSoundIds.get(type);
    }

    public void putSoundIdForQuestType(ResourceLocation location, QuestType type) {
        questSoundIds.put(type, location);
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

    private boolean isConfig(ResourceLocation id) {
        return id.toLanguageKey().contains(Constants.CONFIG);
    }

    @Override
    public int getLatestVersion() {
        return VERSION;
    }

    @Override
    public ToastConfigData copy() {
        return new ToastConfigData(textureId, animationId, soundIds, questSoundIds).withLatestVersion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(textureId, animationId, soundIds, questSoundIds);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ToastConfigData that)) return false;
        return Objects.equals(textureId, that.textureId)
                && Objects.equals(animationId, that.animationId)
                && Objects.equals(soundIds, that.soundIds)
                && Objects.equals(questSoundIds, that.questSoundIds);
    }

    @Override
    public String toString() {
        return getBaseToStringBuilder()
                .append("textureId", textureId)
                .append("animationId", animationId)
                .append("soundIds", soundIds)
                .append("questSoundIds", questSoundIds)
                .toString();
    }
}
