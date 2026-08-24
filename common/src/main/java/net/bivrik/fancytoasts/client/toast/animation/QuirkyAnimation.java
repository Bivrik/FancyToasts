package net.bivrik.fancytoasts.client.toast.animation;

import java.util.Random;

import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.toast.AnimationSetup;
import net.bivrik.fancytoasts.client.toast.Phase;
import net.bivrik.fancytoasts.core.Easing;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;

public class QuirkyAnimation extends FancyToastAnimation {
    private final Phase ICON_APPEARANCE = new Phase(40, 0);
    private final Phase ICON_SCALE = new Phase(70, 0);
    private final Phase BANNER_APPEARANCE = new Phase(18, 24);
    private final Phase BACKGROUND_APPEARANCE = new Phase(20, 20);
    private final Phase TEXT_APPEARANCE = new Phase(20, 36);

    private final int FADE_OUT_DURATION = 30;
    private final int DURATION = 130 + FADE_OUT_DURATION;
    private final Phase FADE_OUT_PHASE = new Phase(FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

    private float randomRotation;

    @Override
    public void setup(AnimationSetup setup, Advancement advancement, GeneralConfigData generalConfig, Font font, int toastWidth, int toastHeight) {
        super.setup(setup, advancement, generalConfig, font, toastWidth, toastHeight);

        setLines(this.display.getAnnouncement(), this.display.getDescription());
        randomRotation = new Random().nextFloat(-0.4f, 0.4f);
    }

    @Override
    public void render(GuiContext context, float renderTicks) {
        context.push();

        float globalSinX = sinLoop(renderTicks, 1.0f, 7.0f);
        float globalSinY = sinLoop(renderTicks, 2.0f, 5.0f);
        context.translate(globalSinX, globalSinY - 20);

        if (FADE_OUT_PHASE.isStarted(renderTicks)) {
            int toastCenterX = this.toastWidth / 2;
            int toastCenterY = this.toastHeight / 2;
            context.push();

            float progress = Phase.getProgress(renderTicks, FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

            float fadeOutScaleX = Easing.OCT_EASE_IN.lerp(1.0f, 0, progress);
            context.scaleAround(fadeOutScaleX, toastCenterX, toastCenterY);

            float fadeOutRotation = Easing.OCT_EASE_IN.lerp(0, randomRotation, progress);
            context.rotateAround(fadeOutRotation, toastCenterX, toastCenterY);
        }

        if (BANNER_APPEARANCE.isStarted(renderTicks)) {
            context.push();
            float y = 58;
            if (BANNER_APPEARANCE.isActive(renderTicks)) {
                float progress = BANNER_APPEARANCE.getProgress(renderTicks);

                y = Easing.OCT_EASE_OUT.lerp(10.0f, 58.0f, progress);
            }
            context.translate(0, y);
            drawBanner(context);
            context.pop();
        }

        if (BACKGROUND_APPEARANCE.isStarted(renderTicks)) {
            context.push();
            if (BACKGROUND_APPEARANCE.isActive(renderTicks)) {
                float progress = BACKGROUND_APPEARANCE.getProgress(renderTicks);

                float y = Easing.OCT_EASE_OUT.lerp(-95.0f, 0, progress);
                context.translate(0, y);
            }
            drawBackground(context);
            context.pop();
        }

        if (ICON_APPEARANCE.isStarted(renderTicks)) {
            context.push();
            float posY = 55;
            if (ICON_APPEARANCE.isActive(renderTicks)) {
                float progress = ICON_APPEARANCE.getProgress(renderTicks);

                posY = Easing.OCT_EASE_OUT.lerp(-95.0f, 55.0f, progress);
            }
            if (ICON_SCALE.isStarted(renderTicks) && ICON_SCALE.isActive(renderTicks)) {
                float progress = ICON_SCALE.getProgress(renderTicks);

                float scale = Easing.OCT_EASE_OUT.lerp(3.0f, 1.0f, progress);
                context.scaleAround(scale, 68 + 13, 17);
            }
            float sinY = sinLoop(renderTicks, 2.0F, -1.2F);
            context.translate(0, sinY + posY);
            drawIcon(context);
            context.pop();
        }

        if (TEXT_APPEARANCE.isStarted(renderTicks)) {
            float progress = TEXT_APPEARANCE.getProgress(renderTicks);

            drawTitle(context.guiGraphics(), progress);
            drawDescription(context.guiGraphics(), progress);
        }

        if (FADE_OUT_PHASE.isStarted(renderTicks)) {
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
    public int getDuration() {
        return DURATION;
    }

    @Override
    public int getToastSoundTiming() {
        return TEXT_APPEARANCE.startTicks() + 3;
    }
}