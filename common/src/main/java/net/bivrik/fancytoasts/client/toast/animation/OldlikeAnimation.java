package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.platform.utility.GUIs;
import net.bivrik.fancytoasts.utility.MathEasing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class OldlikeAnimation extends FancyAdvancementToastAnimation {
    private final Appearance ICON_APPEARANCE = new Appearance(2000, 0);
    private final Appearance BANNER_APPEARANCE = new Appearance(2000, 100);
    private final Appearance BACKGROUND_APPEARANCE = new Appearance(2000, 200);
    private final Appearance TITLE_TEXT_APPEARANCE = new Appearance(2000, 1200);
    private final Appearance DESCRIPTION_TEXT_APPEARANCE = new Appearance(2000, 1400);

    private final int FADE_OUT_DURATION = 3000;
    private final int DURATION = 3500 + FADE_OUT_DURATION;

    @Override
    public void setup(AnimationSetup setup, Minecraft minecraft, int toastWidth, int toastHeight) {
        super.setup(setup, minecraft, toastWidth, toastHeight);

        this.setLines(displayInfo.getAdvancementsAnnouncement(), displayInfo.getDescription());
    }

    @Override
    public void draw(GuiGraphics guiGraphics, long time) {
        super.draw(guiGraphics, time);

        float iconAppearProgress = ICON_APPEARANCE.getProgress(time);
        float bannerAppearProgress = BANNER_APPEARANCE.getProgress(time);
        float backgroundAppearProgress = BACKGROUND_APPEARANCE.getProgress(time);
        float titleAppearProgress = TITLE_TEXT_APPEARANCE.getProgress(time);
        float descriptionAppearProgress = DESCRIPTION_TEXT_APPEARANCE.getProgress(time);
        float fadeOutProgress = Appearance.getProgress(time, FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

        var stack = GUIs.getStack(guiGraphics);

        if (bannerAppearProgress > 0) {
            GUIs.push(stack);
            float alpha = 1;
            if (bannerAppearProgress != 1) {
                alpha = MathEasing.easeOutLerp(0.0F, 1.0F, bannerAppearProgress);

                float x = MathEasing.easeOutLerp(35.0F, 0, bannerAppearProgress);
                GUIs.translate(stack, x, 0);
            }
            else if (fadeOutProgress != 1 && fadeOutProgress > 0) {
                alpha = MathEasing.easeInLerp(1.0F, 0, fadeOutProgress);
            }
            float sinY = (float) (Math.sin(time / 700.0F)) * 2.0F;
            GUIs.translate(stack, 0, sinY + 6);
            this.drawBanner(guiGraphics, alpha);
            GUIs.pop(stack);
        }

        if (backgroundAppearProgress > 0) {
            GUIs.push(stack);
            float alpha = 1;
            if (backgroundAppearProgress != 1) {
                alpha = MathEasing.easeOutLerp(0, 1.0F, backgroundAppearProgress);

                float x = MathEasing.easeOutLerp(35.0F, 0, backgroundAppearProgress);
                GUIs.translate(stack, x, 0);
            }
            else if (fadeOutProgress != 1 && fadeOutProgress > 0) {
                alpha = MathEasing.easeInLerp(1.0F, 0, fadeOutProgress);
            }
            this.drawBackground(guiGraphics, alpha);
            GUIs.pop(stack);
        }

        if (iconAppearProgress > 0) {
            GUIs.push(stack);
            float alpha = 1;
            int x = 77;
            float scale = 1;
            if (iconAppearProgress != 1) {
                alpha = MathEasing.easeOutLerp(0.0F, 1.0F, iconAppearProgress);
                x = MathEasing.easeOutLerp(115, 77, iconAppearProgress);
            }
            else if (fadeOutProgress != 1 && fadeOutProgress > 0) {
                alpha = MathEasing.easeInLerp(1.0F, 0, fadeOutProgress);
                scale = MathEasing.easeInLerp(1.0F, 0, fadeOutProgress);
            }
            GUIs.translate(stack, x, 11);
            GUIs.scaleAround(stack, scale, 68 + 13, 14);
            float cosRotation = (float) (Math.cos(time / 500.0)) * 0.2F;
            GUIs.rotateAround(stack, cosRotation, 68 + 13, 14);
            this.drawIcon(guiGraphics, alpha);
            GUIs.pop(stack);
        }

        float fadeOutTextAlpha = 0;
        if (fadeOutProgress != 1 && fadeOutProgress > 0) {
            fadeOutTextAlpha = MathEasing.easeInLerp(0, 1.0F, fadeOutProgress);
        }

        if (titleAppearProgress > 0) {
            GUIs.push(stack);
            if (titleAppearProgress != 1) {
                int x = MathEasing.elasticEaseOutLerp(50, 0, titleAppearProgress);
                GUIs.translate(stack, x, 0);
            }
            this.drawTitle(guiGraphics, titleAppearProgress - fadeOutTextAlpha);
            GUIs.pop(stack);
        }

        if (descriptionAppearProgress > 0) {
            GUIs.push(stack);
            if (descriptionAppearProgress != 1) {
                int x = MathEasing.elasticEaseOutLerp(50, 0, descriptionAppearProgress);
                GUIs.translate(stack, x, 0);
            }
            this.drawDescription(guiGraphics, descriptionAppearProgress - fadeOutTextAlpha);
            GUIs.pop(stack);
        }
    }

    @Override
    public int getDuration() {
        return DURATION - 25;
    }

    @Override
    public int getToastSoundTiming() {
        return TITLE_TEXT_APPEARANCE.startPoint() + 280;
    }
}