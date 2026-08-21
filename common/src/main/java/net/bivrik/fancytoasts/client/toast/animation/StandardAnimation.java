package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.toast.AnimationSetup;
import net.bivrik.fancytoasts.client.toast.Phase;
import net.bivrik.fancytoasts.core.Easing;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;

public class StandardAnimation extends FancyToastAnimation {
    private final Phase ICON_PHASE = new Phase(40, 0);
    private final Phase BANNER_PHASE = new Phase(14, 32);
    private final Phase BACKGROUND_PHASE = new Phase(20, 31);
    private final Phase TEXT_PHASE = new Phase(20, 40);

    private final int FADE_OUT_DURATION = 40;
    private final int DURATION = 110 + FADE_OUT_DURATION;
    private final Phase FADE_OUT_PHASE = new Phase(FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

    @Override
    public void setup(AnimationSetup setup, GeneralConfigData generalConfig, Font font, int toastWidth, int toastHeight) {
        super.setup(setup, generalConfig, font, toastWidth, toastHeight);

        setLines(this.display.getAnnouncement(), this.display.getTitle());
    }

    @Override
    public void render(GuiContext context, float renderTicks) {
        if (FADE_OUT_PHASE.isStarted(renderTicks)) {
            context.push();

            float progress = FADE_OUT_PHASE.getProgress(renderTicks);

            float fadeOutY = Easing.OCT_EASE_IN.lerp(0, -85.0f, progress);
            context.translate(0, fadeOutY);
        }

        if (BACKGROUND_PHASE.isStarted(renderTicks)) {
            context.push();
            if (BACKGROUND_PHASE.isActive(renderTicks)) {
                float background = BACKGROUND_PHASE.getProgress(renderTicks);

                float y = Easing.OCT_EASE_OUT.lerp(-200.0f, 0, background);
                context.translate(0, y);
            }
            drawBackground(context);
            context.pop();
        }

        if (BANNER_PHASE.isStarted(renderTicks)) {
            context.push();
            if (BANNER_PHASE.isActive(renderTicks)) {
                float progress = BANNER_PHASE.getProgress(renderTicks);

                float xScale = Easing.OCT_EASE_OUT.lerp(0, 1.0f, progress);
                context.scaleAround(xScale, 1, 81, 0);
            }
            drawBanner(context);
            context.pop();
        }

        if (ICON_PHASE.isStarted(renderTicks)) {
            context.push();
            if (ICON_PHASE.isActive(renderTicks)) {
                float progress = ICON_PHASE.getProgress(renderTicks);

                float scale = Easing.OCT_EASE_OUT.lerp(0, 1.0f, progress);
                context.scaleAround(scale, 81, 13);

                float y = Easing.ELASTIC_OUT.lerp(-40.0f, 0, progress);
                context.translate(0, y);
            }
            float sinY = sinLoop(renderTicks, 1.6f, 1.5f);
            context.translate(0, sinY - 5);
            drawIcon(context);
            context.pop();
        }

        if (TEXT_PHASE.isStarted(renderTicks)) {
            float progress = TEXT_PHASE.getProgress(renderTicks);

            drawTitle(context.guiGraphics(), progress);
            drawDescription(context.guiGraphics(), progress);
        }

        if (FADE_OUT_PHASE.isStarted(renderTicks)) {
            context.pop();
        }
    }

    @Override
    protected void drawDescription(GuiGraphics guiGraphics, float alpha) {
        var descriptionLines = getDescriptionLines();
        if (descriptionLines.isEmpty()) {
            return;
        }

        int centerToastX = this.toastWidth / 2;
        int descriptionColorARGB = getDescriptionColor(alpha).getARGB();

        if (descriptionLines.size() == 1) {
            guiGraphics.drawCenteredString(this.font, descriptionLines.getFirst(), centerToastX, 42, descriptionColorARGB);
        } else {
            int lineHeight = 38;
            guiGraphics.drawCenteredString(this.font, descriptionLines.getFirst(), centerToastX, lineHeight, descriptionColorARGB);

            lineHeight += 9;
            FormattedCharSequence secondLine;
            if (descriptionLines.size() == 2) {
                secondLine = descriptionLines.get(1);
            } else {
                secondLine = FormattedCharSequence.composite(descriptionLines.get(1), getDots(this.descriptionStyle));
            }
            guiGraphics.drawCenteredString(this.font, secondLine, centerToastX, lineHeight, descriptionColorARGB);
        }
    }

    @Override
    public int getDuration() {
        return DURATION;
    }

    @Override
    public int getToastSoundTiming() {
        return TEXT_PHASE.startTicks() + 3;
    }
}
