package net.bivrik.fancytoasts.client.toast;

import net.bivrik.fancytoasts.FancyToasts;
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
    private static final Random random = new Random();

    private final GeneralConfigData generalConfig;

    private FancyToastAnimation animation;
    private ResourceLocation toastSoundId;
    private float volume;
    private SoundManager soundManager;

    private float time = 0;
    private boolean isEnded = false;
    private int playedSoundsCount = 0;

    public FancyAdvancementToast(Minecraft minecraft, GeneralConfigData generalConfig, AdvancementDisplay display, ResourceLocation soundId, ResourceLocation textureId, ResourceLocation animationId) {
        this.generalConfig = generalConfig;

        if (generalConfig.areSoundsEnabled()) {
            soundManager = minecraft.getSoundManager();
        }

        switch (display.getType()) {
            case GOAL -> volume = generalConfig.getGoalVolume();
            case CHALLENGE -> volume = generalConfig.getChallengeVolume();
            default -> volume = generalConfig.getTaskVolume();
        }

        animation = AnimationRegistry.getAnimation(animationId).get();
        AnimationSetup setup = new AnimationSetup(textureId, display, DefaultUVs.BACKGROUND, DefaultUVs.PLAQUE);
        animation.setup(setup, generalConfig, minecraft, getWidth(), getHeight());

        toastSoundId = soundId;

        Debug.info("Created new fancy advancement toast: {}", display.getTitle().getString());

        Debug.warn("{} - {}", display.getTitle().getString(), display.getTitle().toString());
        Debug.warn("{} - {}", display.getDescription().getString(), display.getDescription().toString());
        Debug.warn("{} - {}", display.getAnnouncement().getString(), display.getAnnouncement().toString());
    }

    public void draw(GuiGraphics graphics) {
        animation.draw(graphics, (long) time);
    }

    public void update(float delta) {
        time += delta * 50;

        if (time >= animation.getDuration() && !isEnded) {
            animation.unsubscribeFromGeneralConfigDataEvent();
            isEnded = true;
        }

        if (soundManager != null) {
            int timeInSeconds = (int) (time / 50);

            switch (playedSoundsCount) {
                case 0 -> playSound(SoundEvents.UI_TOAST_IN, 1.5f);
                case 1 -> {
                    if (timeInSeconds == animation.getToastSoundTiming() / 50) {
                        playSound(toastSoundId, volume);
                    }
                }
                case 2 -> {
                    if (timeInSeconds == animation.getDuration() / 50 - 10) {
                        playSound(SoundEvents.UI_TOAST_IN, 1.5f);
                    }
                }
            }
        }
    }

    private void playSound(SoundEvent sound, float volume) {
        float pitch = 1.0f;
        float pitchRandomness = generalConfig.getPitchRandomness();
        if (pitchRandomness != 0.0f) {
            pitch = random.nextFloat(pitch - pitchRandomness, pitch + pitchRandomness);
        }
        soundManager.play(SimpleSoundInstance.forUI(sound, pitch, volume));
        playedSoundsCount++;
    }

    private void playSound(ResourceLocation soundLocation, float volume) {
        playSound(SoundEvent.createVariableRangeEvent(soundLocation), volume);
    }

    public boolean isEnded() {
        return isEnded;
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }
}
