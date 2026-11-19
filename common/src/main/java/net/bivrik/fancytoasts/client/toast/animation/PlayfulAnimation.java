package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.toast.AnimationSetup;
import net.bivrik.fancytoasts.client.toast.Phase;
import net.bivrik.fancytoasts.core.Easing;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.minecraft.client.gui.Font;

public class PlayfulAnimation extends FancyToastAnimation {
    private final Phase ICON_APPEARANCE = new Phase(1000, 0);
    private final Phase ICON_MOVEMENT = new Phase(1500, 1000);
    private final Phase BANNER_APPEARANCE = new Phase(1000, 1500);
    private final Phase BACKGROUND_APPEARANCE = new Phase(800, 1600);
    private final Phase TEXT_APPEARANCE = new Phase(1000, 2000);

    private final int FADE_OUT_DURATION = 1500;
    private Phase FADE_OUT_PHASE;

    @Override
    public void setup(AnimationSetup setup, GeneralConfigData generalConfig, Font font, int toastWidth, int toastHeight) {
        super.setup(setup, generalConfig, font, toastWidth, toastHeight);

        setLines(this.display.getTitle(), this.display.getDescription());
        FADE_OUT_PHASE = new Phase(FADE_OUT_DURATION, getDuration() - FADE_OUT_DURATION);
    }

    @Override
    protected void render(GuiContext context, float lifetimeMs) {
        float globalSinY = sinLoop(lifetimeMs, 2.0f, 1.0f) - 1;

        if (FADE_OUT_PHASE.isStarted(lifetimeMs)) {
            int toastCenterX = this.toastWidth / 2;
            int toastCenterY = this.toastHeight / 2;
            context.push();

            float progress = FADE_OUT_PHASE.getProgress(lifetimeMs);

            float fadeOutScale = Easing.OCT_EASE_IN.lerp(1.0f, 0, progress);
            context.scaleAround(fadeOutScale, toastCenterX, toastCenterY);
        }

        if (BACKGROUND_APPEARANCE.isStarted(lifetimeMs)) {
            context.push();
            if (BACKGROUND_APPEARANCE.isActive(lifetimeMs)) {
                float progress = BACKGROUND_APPEARANCE.getProgress(lifetimeMs);

                float scale = Easing.ELASTIC_OUT.lerp(0, 1.0f, progress);
                context.scaleAround(scale, 76, 0);

                float rotation = Easing.ELASTIC_OUT.lerp(-1.0f, 0, progress);
                context.rotateAround(rotation, 76, 0);

                float y = Easing.OCT_EASE_OUT.lerp(-20.0f, 0, progress);
                context.translate(0, y);
            }
            drawBackground(context);
            context.pop();
        }

        if (BANNER_APPEARANCE.isStarted(lifetimeMs)) {
            context.push();
            if (BANNER_APPEARANCE.isActive(lifetimeMs)) {
                float progress = BANNER_APPEARANCE.getProgress(lifetimeMs);

                float scaleX = Easing.OCT_EASE_OUT.lerp(0, 1.0f, progress);
                context.scaleAround(scaleX, 1, 15, -12);
            }
            context.translate(0, globalSinY);
            drawBanner(context);
            context.pop();
        }

        if (ICON_APPEARANCE.isStarted(lifetimeMs)) {
            context.push();
            if (ICON_APPEARANCE.isActive(lifetimeMs)) {
                float progress = ICON_APPEARANCE.getProgress(lifetimeMs);

                float scale = Easing.ELASTIC_OUT.lerp(0, 1.0f, progress);
                context.scaleAround(scale, 68 + 13, 13);

                float rotation = Easing.ELASTIC_OUT.lerp(-1.0f, 0, progress);
                context.rotateAround(rotation, 68 + 13, 13);
            }
            else if (ICON_MOVEMENT.isStarted(lifetimeMs)) {
                float progress = ICON_MOVEMENT.getProgress(lifetimeMs);

                float x = Easing.OCT_EASE_OUT.lerp(0, -60.0f, progress);
                context.translate(x, 0);
            }
            context.translate(0, globalSinY - 5);
            drawIcon(context);
            context.pop();
        }

        if (TEXT_APPEARANCE.isStarted(lifetimeMs)) {
            float progress = TEXT_APPEARANCE.getProgress(lifetimeMs);

            drawTitle(context.guiGraphics(), progress);
            drawDescription(context.guiGraphics(), progress);
        }

        if (FADE_OUT_PHASE.isStarted(lifetimeMs)) {
            context.pop();
        }
    }

    @Override
    public int getAnimationDuration() {
        return 6000 + FADE_OUT_DURATION;
    }

    @Override
    public int getToastSoundTiming() {
        return TEXT_APPEARANCE.startTimeMs() + 200;
    }
}