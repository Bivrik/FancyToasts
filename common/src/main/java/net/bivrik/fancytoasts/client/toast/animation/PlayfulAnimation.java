package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.platform.utility.GUIs;
import net.bivrik.fancytoasts.utility.MathEasing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class PlayfulAnimation extends FancyAdvancementToastAnimation {
    private final Appearance ICON_APPEARANCE = new Appearance(1000, 0);
    private final Appearance ICON_MOVEMENT = new Appearance(1500, 1000);
    private final Appearance BANNER_APPEARANCE = new Appearance(1000, 1500);
    private final Appearance BACKGROUND_APPEARANCE = new Appearance(800, 1600);
    private final Appearance TEXT_APPEARANCE = new Appearance(1000, 2000);

    private final int FADE_OUT_DURATION = 1500;
    private final int DURATION = 6000 + FADE_OUT_DURATION;

    @Override
    public void setup(AnimationSetup setup, Minecraft minecraft, int toastWidth, int toastHeight) {
        super.setup(setup, minecraft, toastWidth, toastHeight);

        this.setLines(displayInfo.getTitle(), displayInfo.getDescription());
    }

    @Override
    public void draw(GuiGraphics guiGraphics, long time) {
        super.draw(guiGraphics, time);

        float iconAppearProgress = ICON_APPEARANCE.getProgress(time);
        float iconMovementProgress = ICON_MOVEMENT.getProgress(time);
        float bannerAppearProgress = BANNER_APPEARANCE.getProgress(time);
        float backgroundAppearProgress = BACKGROUND_APPEARANCE.getProgress(time);
        float textAppearProgress = TEXT_APPEARANCE.getProgress(time);
        float fadeOutProgress = Appearance.getProgress(time, FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

        var stack = GUIs.getStack(guiGraphics);
        float globalSinY = (float) (Math.sin(time / 400.0)) - 3;

        if (fadeOutProgress > 0) {
            float fadeOutScale = MathEasing.easeInLerp(1f, 0f, fadeOutProgress);
            int toastCenterX = toastWidth / 2;
            int toastCenterY = toastHeight / 2;

            GUIs.push(stack);
            GUIs.scaleAround(stack, fadeOutScale, toastCenterX, toastCenterY);
        }

        if (backgroundAppearProgress > 0) {
            GUIs.push(stack);
            if (backgroundAppearProgress != 1) {
                float scale = MathEasing.elasticEaseOutLerp(0, 1.0f, backgroundAppearProgress);
                GUIs.scaleAround(stack, scale, 76, 0);

                float rotation = MathEasing.elasticEaseOutLerp(-1.0f, 0, backgroundAppearProgress);
                GUIs.rotateAround(stack, rotation, 76, 0);

                float y = MathEasing.easeOutLerp(-20.0F, 0, backgroundAppearProgress);
                GUIs.translate(stack, 0, y);
            }
            this.drawBackground(guiGraphics);
            GUIs.pop(stack);
        }

        if (bannerAppearProgress > 0) {
            GUIs.push(stack);
            if (bannerAppearProgress != 1) {
                float scaleX = MathEasing.easeOutLerp(0f, 1f, bannerAppearProgress);
                GUIs.scaleAround(stack, scaleX, 1, 15, -12);
            }
            GUIs.translate(stack, 0, globalSinY);
            this.drawBanner(guiGraphics);
            GUIs.pop(stack);
        }

        if (iconAppearProgress > 0) {
            GUIs.push(stack);
            if (iconAppearProgress != 1) {
                float scale = MathEasing.elasticEaseOutLerp(0f, 1f, iconAppearProgress);
                GUIs.scaleAround(stack, scale, 68 + 13, 13);

                float rotation = MathEasing.elasticEaseOutLerp(-1f, 0f, iconAppearProgress);
                GUIs.rotateAround(stack, rotation, 68 + 13, 13);
            }
            else if (iconMovementProgress > 0) {
                float x = MathEasing.easeOutLerp(0f, -60f, iconMovementProgress);
                GUIs.translate(stack, x, 0);
            }
            GUIs.translate(stack, 0, globalSinY - 5);
            this.drawIcon(guiGraphics);
            GUIs.pop(stack);
        }

        if (textAppearProgress > 0) {
            this.drawTitle(guiGraphics, textAppearProgress);
            this.drawDescription(guiGraphics, textAppearProgress);
        }

        if (fadeOutProgress > 0) {
            GUIs.pop(stack);
        }
    }

    @Override
    public int getDuration() {
        return DURATION;
    }

    @Override
    public int getToastSoundTiming() {
        return TEXT_APPEARANCE.startPoint() + 200;
    }
}