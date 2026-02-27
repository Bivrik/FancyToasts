package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.toast.AnimationSetup;
import net.bivrik.fancytoasts.client.toast.Appearance;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.utility.Easing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class PlayfulAnimation extends FancyToastAnimation {
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

        GuiContext context = new GuiContext(guiGraphics);
        float globalSinY = this.sinusoidLoop(time, 2.0f, 1.0f) - 3;

        if (fadeOutProgress > 0) {
            float fadeOutScale = Easing.OCT_EASE_IN.lerp(1.0f, 0, fadeOutProgress);
            int toastCenterX = toastWidth / 2;
            int toastCenterY = toastHeight / 2;

            context.push();
            context.scaleAround(fadeOutScale, toastCenterX, toastCenterY);
        }

        if (backgroundAppearProgress > 0) {
            context.push();
            if (backgroundAppearProgress != 1) {
                float scale = Easing.ELASTIC_OUT.lerp(0, 1.0f, backgroundAppearProgress);
                context.scaleAround(scale, 76, 0);

                float rotation = Easing.ELASTIC_OUT.lerp(-1.0f, 0, backgroundAppearProgress);
                context.rotateAround(rotation, 76, 0);

                float y = Easing.OCT_EASE_OUT.lerp(-20.0f, 0, backgroundAppearProgress);
                context.translate(0, y);
            }
            this.drawBackground(context);
            context.pop();
        }

        if (bannerAppearProgress > 0) {
            context.push();
            if (bannerAppearProgress != 1) {
                float scaleX = Easing.OCT_EASE_OUT.lerp(0, 1.0f, bannerAppearProgress);
                context.scaleAround(scaleX, 1, 15, -12);
            }
            context.translate(0, globalSinY);
            this.drawBanner(context);
            context.pop();
        }

        if (iconAppearProgress > 0) {
            context.push();
            if (iconAppearProgress != 1) {
                float scale = Easing.ELASTIC_OUT.lerp(0, 1.0f, iconAppearProgress);
                context.scaleAround(scale, 68 + 13, 13);

                float rotation = Easing.ELASTIC_OUT.lerp(-1.0f, 0, iconAppearProgress);
                context.rotateAround(rotation, 68 + 13, 13);
            }
            else if (iconMovementProgress > 0) {
                float x = Easing.OCT_EASE_OUT.lerp(0, -60.0f, iconMovementProgress);
                context.translate(x, 0);
            }
            context.translate(0, globalSinY - 5);
            this.drawIcon(context);
            context.pop();
        }

        if (textAppearProgress > 0) {
            this.drawTitle(guiGraphics, textAppearProgress);
            this.drawDescription(guiGraphics, textAppearProgress);
        }

        if (fadeOutProgress > 0) {
            context.pop();
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