package net.bivrik.fancytoasts.client.toast.animation;

import java.util.Random;

import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.toast.AnimationSetup;
import net.bivrik.fancytoasts.client.toast.Phase;
import net.bivrik.fancytoasts.core.Easing;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;

public class QuirkyAnimation extends FancyToastAnimation {
    private final Phase ICON_APPEARANCE = new Phase(2000, 0);
    private final Phase ICON_SCALE = new Phase(3500, 0);
    private final Phase BANNER_APPEARANCE = new Phase(900, 1200);
    private final Phase BACKGROUND_APPEARANCE = new Phase(1000, 1000);
    private final Phase TEXT_APPEARANCE = new Phase(1000, 1800);

    private final int FADE_OUT_DURATION = 1500;
    private Phase FADE_OUT_PHASE;

    private float randomRotation;

    @Override
    public void setup(AnimationSetup setup, GeneralConfigData generalConfig, Font font, int toastWidth, int toastHeight) {
        super.setup(setup, generalConfig, font, toastWidth, toastHeight);

        setLines(this.display.getAnnouncement(), this.display.getDescription());
        FADE_OUT_PHASE = new Phase(FADE_OUT_DURATION, getDuration() - FADE_OUT_DURATION);

        randomRotation = new Random().nextFloat(-0.4f, 0.4f);
    }

    @Override
    public void render(GuiContext context, float lifetimeMs) {
        context.push();

        float globalSinX = sinLoop(lifetimeMs, 1.0f, 7.0f);
        float globalSinY = sinLoop(lifetimeMs, 2.0f, 5.0f);
        context.translate(globalSinX, globalSinY - 20);

        if (FADE_OUT_PHASE.isStarted(lifetimeMs)) {
            int toastCenterX = this.toastWidth / 2;
            int toastCenterY = this.toastHeight / 2;
            context.push();

            float progress = Phase.getProgress(lifetimeMs, FADE_OUT_DURATION, getDuration() - FADE_OUT_DURATION);

            float fadeOutScaleX = Easing.OCT_EASE_IN.lerp(1.0f, 0, progress);
            context.scaleAround(fadeOutScaleX, toastCenterX, toastCenterY);

            float fadeOutRotation = Easing.OCT_EASE_IN.lerp(0, randomRotation, progress);
            context.rotateAround(fadeOutRotation, toastCenterX, toastCenterY);
        }

        if (BANNER_APPEARANCE.isStarted(lifetimeMs)) {
            context.push();
            float y = 58;
            if (BANNER_APPEARANCE.isActive(lifetimeMs)) {
                float progress = BANNER_APPEARANCE.getProgress(lifetimeMs);

                y = Easing.OCT_EASE_OUT.lerp(10.0f, 58.0f, progress);
            }
            context.translate(0, y);
            drawBanner(context);
            context.pop();
        }

        if (BACKGROUND_APPEARANCE.isStarted(lifetimeMs)) {
            context.push();
            if (BACKGROUND_APPEARANCE.isActive(lifetimeMs)) {
                float progress = BACKGROUND_APPEARANCE.getProgress(lifetimeMs);

                float y = Easing.OCT_EASE_OUT.lerp(-95.0f, 0, progress);
                context.translate(0, y);
            }
            drawBackground(context);
            context.pop();
        }

        if (ICON_APPEARANCE.isStarted(lifetimeMs)) {
            context.push();
            float posY = 55;
            if (ICON_APPEARANCE.isActive(lifetimeMs)) {
                float progress = ICON_APPEARANCE.getProgress(lifetimeMs);

                posY = Easing.OCT_EASE_OUT.lerp(-95.0f, 55.0f, progress);
            }
            if (ICON_SCALE.isStarted(lifetimeMs) && ICON_SCALE.isActive(lifetimeMs)) {
                float progress = ICON_SCALE.getProgress(lifetimeMs);

                float scale = Easing.OCT_EASE_OUT.lerp(3.0f, 1.0f, progress);
                context.scaleAround(scale, 68 + 13, 17);
            }
            float sinY = sinLoop(lifetimeMs, 2.0F, -1.2F);
            context.translate(0, sinY + posY);
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

        context.pop();
    }

    @Override
    protected void drawDescription(GuiGraphics guiGraphics, float alpha) {
        var descriptionLines = getDescriptionLines();
        if (descriptionLines.isEmpty()) {
            return;
        }

        int centerToastX = this.toastWidth / 2;
        int descriptionColorARGB = getDescriptionColor(alpha).getARGB();

        guiGraphics.drawCenteredString(this.font, descriptionLines.get(0), centerToastX, 38, descriptionColorARGB);
        if (descriptionLines.size() > 1) {
            FormattedCharSequence descriptionSecondLine = descriptionLines.get(1);
            if (descriptionLines.size() == 2) {
                guiGraphics.drawCenteredString(this.font, descriptionSecondLine, centerToastX, 47, descriptionColorARGB);
            } else {
                guiGraphics.drawCenteredString(this.font, FormattedCharSequence.composite(descriptionSecondLine, getDots(this.descriptionStyle)), centerToastX, 47, descriptionColorARGB);
            }
        }
    }

    @Override
    public int getAnimationDuration() {
        return 6500 + FADE_OUT_DURATION;
    }

    @Override
    public int getToastSoundTiming() {
        return TEXT_APPEARANCE.startTimeMs() + 150
                ;
    }
}