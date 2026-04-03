package net.bivrik.fancytoasts.client.toast;

import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.registry.AnimationRegistry;
import net.bivrik.fancytoasts.client.toast.animation.FancyToastAnimation;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.platform.utility.ToastDisplayInfo;
import net.bivrik.fancytoasts.utility.DefaultUVs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.Optional;
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

    public FancyAdvancementToast(Minecraft minecraft, ToastDisplayInfo displayInfo, ResourceLocation textureId, ResourceLocation animationId) {
        var generalConfig = Managers.getConfigManager().getGeneralConfigData();
        this.generalConfig = generalConfig;

        if (generalConfig.areSoundsEnabled()) {
            soundManager = minecraft.getSoundManager();
        }

        AnimationSetup setup = new AnimationSetup(textureId, displayInfo, null, DefaultUVs.BACKGROUND, DefaultUVs.PLAQUE);

        switch (displayInfo.getAdvancementType()) {
            case TASK -> {
                setup.setTypeBasedUVs(DefaultUVs.TASK);
                volume = generalConfig.getTaskVolume();
            }
            case GOAL -> {
                setup.setTypeBasedUVs(DefaultUVs.GOAL);
                volume = generalConfig.getGoalVolume();
            }
            case CHALLENGE -> {
                setup.setTypeBasedUVs(DefaultUVs.CHALLENGE);
                volume = generalConfig.getChallengeVolume();
            }
            default -> throw new RuntimeException("Could match correct advancement type");
        }

        animation = AnimationRegistry.getAnimation(animationId).get();
        animation.setup(setup, minecraft, getWidth(), getHeight());
        toastSoundId = Managers.getConfigManager().getToastConfigData().getSoundIdByType(displayInfo.getAdvancementType());

        Debug.info("Created new fancy advancement toast: {}; texture: {}; animation: {}", displayInfo.getTitle().getString(), textureId, animationId);
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
