package net.bivrik.fancytoasts.client.toast;

import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.registry.AnimationRegistry;
import net.bivrik.fancytoasts.client.toast.animation.FancyToastAnimation;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.platform.utility.AdvancementDisplay;
import net.bivrik.fancytoasts.utility.DefaultUVs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.Random;

public class FancyAdvancementToast {
    private static final int WIDTH = 162;
    private static final int HEIGHT = 70;
    private static final Random RANDOM = new Random();

    private final GeneralConfigData generalConfig;
    private final SoundManager soundManager;
    private final FancyToastAnimation animation;
    private final ResourceLocation toastSoundId;
    private final float volume;

    private float timeTicks;
    private boolean isDead;
    private int playedSoundsCount;

    public FancyAdvancementToast(Minecraft minecraft, GeneralConfigData generalConfig, AdvancementDisplay display, ResourceLocation soundId, ResourceLocation textureId, ResourceLocation animationId) {
        this.generalConfig = generalConfig;
        this.soundManager = minecraft.getSoundManager();

        switch (display.getType()) {
            case GOAL -> volume = generalConfig.getGoalVolume();
            case CHALLENGE -> volume = generalConfig.getChallengeVolume();
            default -> volume = generalConfig.getTaskVolume();
        }

        animation = AnimationRegistry.getAnimation(animationId).get();
        toastSoundId = soundId;

        AnimationSetup setup = new AnimationSetup(textureId, display, DefaultUVs.BACKGROUND, DefaultUVs.PLAQUE);
        animation.setup(setup, generalConfig, minecraft.font, getWidth(), getHeight());

        Debug.info("New fancy advancement toast: {}", display.getTitle().getString());

        /*Debug.warn("{} - {}", display.getTitle().getString(), display.getTitle().toString());
        Debug.warn("{} - {}", display.getDescription().getString(), display.getDescription().toString());
        Debug.warn("{} - {}", display.getAnnouncement().getString(), display.getAnnouncement().toString());*/
    }

    public void render(GuiGraphics graphics, float partialTick) {
        if (isDead) {
            return;
        }

        animation.renderWithTransparency(graphics, timeTicks, partialTick);
    }

    public void tick() {
        if (isDead) {
            return;
        }

        if ((timeTicks += generalConfig.getAnimationSpeed()) > animation.getDuration()) {
            animation.unsubscribeFromGeneralConfigDataEvent();
            isDead = true;
            return;
        }

        if (!generalConfig.areSoundsEnabled()) {
            return;
        }

        switch (playedSoundsCount) {
            case 0 -> playSound(SoundEvents.UI_TOAST_IN, 1.74f);
            case 1 -> {
                if (timeTicks >= animation.getToastSoundTiming()) {
                    playSound(toastSoundId, volume);
                }
            }
            case 2 -> {
                if (timeTicks >= animation.getDuration() - 10) {
                    playSound(SoundEvents.UI_TOAST_OUT, 1.74f);
                }
            }
        }
    }

    private void playSound(SoundEvent sound, float volume) {
        float pitch = 1.0f;
        float pitchRandomness = generalConfig.getPitchRandomness();
        if (pitchRandomness != 0.0f) {
            pitch = RANDOM.nextFloat(pitch - pitchRandomness, pitch + pitchRandomness);
        }
        soundManager.play(SimpleSoundInstance.forUI(sound, pitch, volume));
        playedSoundsCount++;
    }

    private void playSound(ResourceLocation soundLocation, float volume) {
        playSound(SoundEvent.createVariableRangeEvent(soundLocation), volume);
    }

    public boolean isDead() {
        return isDead;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }
}
