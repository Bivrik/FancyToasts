package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.toast.AnimationSetup;
import net.bivrik.fancytoasts.client.toast.Phase;
import net.bivrik.fancytoasts.core.Easing;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.minecraft.client.gui.Font;

public class OldlikeAnimation extends FancyToastAnimation {
    private final Phase ICON_PHASE = new Phase(2000, 0);
    private final Phase BANNER_PHASE = new Phase(2000, 100);
    private final Phase BACKGROUND_PHASE = new Phase(2000, 200);
    private final Phase TITLE_TEXT_PHASE = new Phase(2000, 650);
    private final Phase DESCRIPTION_TEXT_PHASE = new Phase(2000, 850);

    private final int FADE_OUT_DURATION = 3000;
    private Phase FADE_OUT_PHASE;

    @Override
    public void setup(AnimationSetup setup, GeneralConfigData generalConfig, Font font, int toastWidth, int toastHeight) {
        super.setup(setup, generalConfig, font, toastWidth, toastHeight);

        setLines(this.display.getAnnouncement(), this.display.getDescription());
        FADE_OUT_PHASE = new Phase(FADE_OUT_DURATION, getDuration() - FADE_OUT_DURATION);
    }

    @Override
    public void render(GuiContext context, float lifetimeMs) {
        float fadeOutProgress = FADE_OUT_PHASE.getProgress(lifetimeMs);

        if (BANNER_PHASE.isStarted(lifetimeMs)) {
            context.push();
            float alpha = 1;
            if (BANNER_PHASE.isActive(lifetimeMs)) {
                float progress = BANNER_PHASE.getProgress(lifetimeMs);

                alpha = Easing.OCT_EASE_OUT.lerp(0, 1.0f, progress);

                float x = Easing.OCT_EASE_OUT.lerp(35, 0, progress);
                context.translate(x, 0);
            }
            else if (FADE_OUT_PHASE.isStarted(lifetimeMs)) {
                alpha = Easing.OCT_EASE_IN.lerp(1.0f, 0, fadeOutProgress);
            }
            float sinY = sinLoop(lifetimeMs, 1.14f, 2.0f);
            context.translate(0, sinY + 5);
            drawBanner(context, alpha);
            context.pop();
        }

        if (BACKGROUND_PHASE.isStarted(lifetimeMs)) {
            context.push();
            float alpha = 1;
            if (BACKGROUND_PHASE.isActive(lifetimeMs)) {
                float progress = BACKGROUND_PHASE.getProgress(lifetimeMs);

                alpha = Easing.OCT_EASE_OUT.lerp(0, 1.0f, progress);

                float x = Easing.OCT_EASE_OUT.lerp(35, 0, progress);
                context.translate(x, 0);
            }
            else if (FADE_OUT_PHASE.isStarted(lifetimeMs)) {
                alpha = Easing.OCT_EASE_IN.lerp(1.0f, 0, fadeOutProgress);
            }
            drawBackground(context, alpha);
            context.pop();
        }

        if (ICON_PHASE.isStarted(lifetimeMs)) {
            context.push();
            float alpha = 1;
            int x = 77;
            float scale = 1;
            if (ICON_PHASE.isActive(lifetimeMs)) {
                float progress = ICON_PHASE.getProgress(lifetimeMs);

                alpha = Easing.OCT_EASE_OUT.lerp(0, 1.0f, progress);
                x = Easing.OCT_EASE_OUT.lerp(115, 77, progress);
            }
            else if (FADE_OUT_PHASE.isStarted(lifetimeMs)) {
                alpha = Easing.OCT_EASE_IN.lerp(1.0f, 0, fadeOutProgress);
                scale = Easing.OCT_EASE_IN.lerp(1.0f, 0, fadeOutProgress);
            }
            context.translate(x, 11);
            context.scaleAround(scale, 68 + 13, 14);
            float cosRotation = cosLoop(lifetimeMs, 1.6f, 0.2f);
            context.rotateAround(cosRotation, 68 + 13, 14);
            drawIcon(context, alpha);
            context.pop();
        }

        float fadeOutTextAlpha = 0;
        if (FADE_OUT_PHASE.isStarted(lifetimeMs)) {
            fadeOutTextAlpha = Easing.OCT_EASE_IN.lerp(0, 1.0f, fadeOutProgress);
        }

        if (TITLE_TEXT_PHASE.isStarted(lifetimeMs)) {
            context.push();
            float progress = TITLE_TEXT_PHASE.getProgress(lifetimeMs);
            if (TITLE_TEXT_PHASE.isActive(lifetimeMs)) {
                float x = Easing.ELASTIC_OUT.lerp(28, 0, progress);
                context.translate(x, 0);
            }
            drawTitle(context.guiGraphics(), progress - fadeOutTextAlpha);
            context.pop();
        }

        if (DESCRIPTION_TEXT_PHASE.isStarted(lifetimeMs)) {
            context.push();
            float progress = DESCRIPTION_TEXT_PHASE.getProgress(lifetimeMs);
            if (DESCRIPTION_TEXT_PHASE.isActive(lifetimeMs)) {
                float x = Easing.ELASTIC_OUT.lerp(28, 0, progress);
                context.translate(x, 0);
            }
            drawDescription(context.guiGraphics(), progress - fadeOutTextAlpha);
            context.pop();
        }
    }

    @Override
    public int getAnimationDuration() {
        return 3500 + FADE_OUT_DURATION;
    }

    @Override
    public int getToastSoundTiming() {
        return TITLE_TEXT_PHASE.startTimeMs() + 300;
    }
}