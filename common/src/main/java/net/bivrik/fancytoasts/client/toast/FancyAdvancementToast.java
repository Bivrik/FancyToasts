package net.bivrik.fancytoasts.client.toast;

import net.bivrik.fancytoasts.client.toast.animation.AnimationType;
import net.bivrik.fancytoasts.client.toast.animation.FancyAdvancementToastAnimation;
import net.bivrik.fancytoasts.client.toast.animation.FancyAdvancementSetup;
import net.bivrik.fancytoasts.client.toast.texture.ToastTextureRegistry;
import net.bivrik.fancytoasts.client.toast.texture.TextureUV;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

import java.util.Objects;

public class FancyAdvancementToast {
    private static final int WIDTH = 162;
    private static final int HEIGHT = 70;

    private final FancyAdvancementToastAnimation animation;

    private SoundManager soundManager;
    private SoundEvent toastSound;

    private boolean isEnded = false;
    private long time;
    private int playedSoundsCount = 0;

    public FancyAdvancementToast(Advancement advancement, ResourceLocation textureId, AnimationType animationType) {
        DisplayInfo display = advancement.display().orElse(null);

        animation = AnimationType.ANIMATIONS.get(animationType).get();
        ResourceLocation texture = ToastTextureRegistry.getTexture(textureId);

        switch (Objects.requireNonNull(display).getType()) {
            case TASK -> {
                animation.setup(new FancyAdvancementSetup(texture, TextureUV.TASK_FRAME_UV, display, 0xFFFF00, 0xFFFFFF));
                toastSound = SoundEvents.ALLAY_AMBIENT_WITH_ITEM;
            }
            case GOAL -> {
                animation.setup(new FancyAdvancementSetup(texture, TextureUV.GOAL_FRAME_UV, display, 0x00FFFF, 0xFFFFFF));
                toastSound = SoundEvents.FIREWORK_ROCKET_TWINKLE_FAR;
            }
            case CHALLENGE -> {
                animation.setup(new FancyAdvancementSetup(texture, TextureUV.CHALLENGE_FRAME_UV, display, 0xEA3CFF, 0x00FFFF));
                toastSound = SoundEvents.UI_TOAST_CHALLENGE_COMPLETE;
            }
        }
    }

    public void draw(GuiGraphics graphics, Minecraft minecraft) {
        animation.draw(graphics, minecraft, this, time);
    }

    public void update(long time) {
        this.time = time;

        if (this.time >= animation.getDuration()) {
            isEnded = true;
        }

        int timeInSeconds = (int) (this.time / 50);
        if (playedSoundsCount == 0 && timeInSeconds == animation.getToastSoundTiming() / 50) {
            soundManager.play(SimpleSoundInstance.forUI(toastSound, 1f, 0.8f));
            playedSoundsCount++;
        }
        if (playedSoundsCount == 1 && timeInSeconds == animation.getDuration() / 50 - 10) {
            soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1f, 1.6f));
            playedSoundsCount++;
        }
    }

    public void playSounds(SoundManager manager) {
        soundManager = manager;
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_TOAST_IN, 1f, 1.6f));
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
