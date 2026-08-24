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
import net.bivrik.fancytoasts.utility.DefaultIdentifiers;
import net.bivrik.fancytoasts.utility.file.Paths;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

public class ToastConfigData extends ConfigData {
    private static final int VERSION = Constants.ConfigVersions.TOAST;

    private Identifier textureId;
    private Identifier animationId;
    private final Map<FancyAdvancementType, Identifier> soundIds = new EnumMap<>(FancyAdvancementType.class);
    private final Map<QuestType, Identifier> questSoundIds = new EnumMap<>(QuestType.class);

    private ToastConfigData(Identifier textureId, Identifier animationId, Map<FancyAdvancementType, Identifier> soundIds, Map<QuestType, Identifier> questSoundIds) {
        super(Paths.TOAST_CONFIG_FILE);

        this.textureId = textureId;
        this.animationId = animationId;
        this.soundIds.putAll(soundIds);
        this.questSoundIds.putAll(questSoundIds);
    }

    public ToastConfigData() {
        this(DefaultIdentifiers.Textures.VANILLA, DefaultIdentifiers.Animations.STANDARD,
                Map.of(
                        FancyAdvancementType.TASK, SoundEvents.NOTE_BLOCK_CHIME.value().location(),
                        FancyAdvancementType.GOAL, SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR.location(),
                        FancyAdvancementType.CHALLENGE, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.location()),
                Map.of(
                        QuestType.TASK, SoundEvents.NOTE_BLOCK_BELL.value().location(),
                        QuestType.QUEST, SoundEvents.NOTE_BLOCK_CHIME.value().location(),
                        QuestType.CHAPTER, SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR.location(),
                        QuestType.BOOK, SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.location())
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

    public Identifier getSoundIdByType(FancyAdvancementType type) {
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

    public void putSoundIdForType(Identifier location, FancyAdvancementType type) {
        soundIds.put(type, location);
    }

    public Identifier getSoundIdByQuestType(QuestType type) {
        var availableSounds = Minecraft.getInstance().getSoundManager().getAvailableSounds();
        Identifier soundId = questSoundIds.get(type);

        if (!availableSounds.contains(soundId)) {
            Identifier standardSoundId = new ToastConfigData().questSoundIds.get(type);
            putSoundIdForQuestType(standardSoundId, type);
            ConfigHandler.save(copy());
            return standardSoundId;
        }

        return questSoundIds.get(type);
    }

    public void putSoundIdForQuestType(Identifier location, QuestType type) {
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

    private boolean isConfig(Identifier id) {
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
