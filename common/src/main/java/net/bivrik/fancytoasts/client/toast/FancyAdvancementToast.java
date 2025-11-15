package net.bivrik.fancytoasts.client.toast;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.registries.AnimationRegistry;
import net.bivrik.fancytoasts.client.toast.animation.FancyAdvancementToastAnimation;
import net.bivrik.fancytoasts.client.toast.animation.FancyAdvancementSetup;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.Objects;
import java.util.Optional;

public class FancyAdvancementToast {
    private static final int WIDTH = 162;
    private static final int HEIGHT = 70;

    private final FancyAdvancementToastAnimation animation;
    private final ResourceLocation toastSoundId;
    private final float volume;

    private SoundManager soundManager;

    private boolean isEnded = false;
    private long time;
    private int playedSoundsCount = 0;

    public FancyAdvancementToast(Advancement advancement, ResourceLocation texture, ResourceLocation animationId) {
        DisplayInfo display = advancement.display().orElse(null);

        animation = AnimationRegistry.getAnimation(animationId).get();

        switch (Objects.requireNonNull(display).getType()) {
            case TASK -> {
                animation.setup(new FancyAdvancementSetup(texture, TextureUV.TASK_FRAME_UV, display, Colors.YELLOW, Colors.WHITE), this);
                toastSoundId = Common.getConfigManager().getToastConfig().getSoundId(AdvancementType.TASK);
                volume = Common.getConfigManager().getGeneralConfig().getTaskVolume();
            }
            case GOAL -> {
                animation.setup(new FancyAdvancementSetup(texture, TextureUV.GOAL_FRAME_UV, display, Colors.CYAN, Colors.WHITE), this);
                toastSoundId = Common.getConfigManager().getToastConfig().getSoundId(AdvancementType.GOAL);
                volume = Common.getConfigManager().getGeneralConfig().getGoalVolume();
            }
            case CHALLENGE -> {
            animation.setup(new FancyAdvancementSetup(texture, TextureUV.CHALLENGE_FRAME_UV, display, Colors.PURPLE, Colors.CYAN), this);
                toastSoundId = Common.getConfigManager().getToastConfig().getSoundId(AdvancementType.CHALLENGE);
                volume = Common.getConfigManager().getGeneralConfig().getChallengeVolume();
            }
            default -> throw new RuntimeException("Could match correct advancement type");
        }

        Debug.info("Created new Fancy Advancement Toast: {}", display.getTitle().getString());
    }

    public void draw(GuiGraphics graphics, Minecraft minecraft) {
        animation.draw(graphics, minecraft, time);
    }

    public void update(long time) {
        this.time = time;

        if (this.time >= animation.getDuration()) {
            isEnded = true;
        }

        if (soundManager != null) {
            int timeInSeconds = (int) (this.time / 50);
            if (playedSoundsCount == 0 && timeInSeconds == animation.getToastSoundTiming() / 50) {
                SoundEvent tse = new SoundEvent(toastSoundId, Optional.empty());
                soundManager.play(SimpleSoundInstance.forUI(tse, 1f, volume));
                playedSoundsCount++;
            }
            if (playedSoundsCount == 1 && timeInSeconds == animation.getDuration() / 50 - 10) {
                soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1f, 1.5f));
                playedSoundsCount++;
            }
        }
    }

    public void trySetSoundManager(SoundManager soundManager) {
        if (!Common.getConfigManager().getGeneralConfig().areSoundsEnabled()) {
            return;
        }

        this.soundManager = soundManager;
        this.soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1f, 1.5f));
    }

    public Minecraft getMinecraft() {
        return Minecraft.getInstance();
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
