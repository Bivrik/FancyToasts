package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.toast.AnimationSetup;
import net.bivrik.fancytoasts.client.toast.Phase;
import net.bivrik.fancytoasts.core.Easing;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;

public class PlayfulAnimation extends FancyToastAnimation {
    private final Phase ICON_APPEARANCE = new Phase(20, 0);
    private final Phase ICON_MOVEMENT = new Phase(30, 20);
    private final Phase BANNER_APPEARANCE = new Phase(20, 30);
    private final Phase BACKGROUND_APPEARANCE = new Phase(16, 32);
    private final Phase TEXT_APPEARANCE = new Phase(20, 40);

    private final int FADE_OUT_DURATION = 30;
    private final int DURATION = 120 + FADE_OUT_DURATION;
    private final Phase FADE_OUT_PHASE = new Phase(FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

    @Override
    public void setup(AnimationSetup setup, Advancement advancement, GeneralConfigData generalConfig, Font font, int toastWidth, int toastHeight) {
        super.setup(setup, advancement, generalConfig, font, toastWidth, toastHeight);

        setLines(this.display.getTitle(), this.display.getDescription());
    }

    @Override
    protected void render(GuiContext context, float renderTicks) {
        float globalSinY = sinLoop(renderTicks, 2.0f, 1.0f) - 1;

        if (FADE_OUT_PHASE.isStarted(renderTicks)) {
            int toastCenterX = this.toastWidth / 2;
            int toastCenterY = this.toastHeight / 2;
            context.push();

            float progress = FADE_OUT_PHASE.getProgress(renderTicks);

            float fadeOutScale = Easing.OCT_EASE_IN.lerp(1.0f, 0, progress);
            context.scaleAround(fadeOutScale, toastCenterX, toastCenterY);
        }

        if (BACKGROUND_APPEARANCE.isStarted(renderTicks)) {
            context.push();
            if (BACKGROUND_APPEARANCE.isActive(renderTicks)) {
                float progress = BACKGROUND_APPEARANCE.getProgress(renderTicks);

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

        if (BANNER_APPEARANCE.isStarted(renderTicks)) {
            context.push();
            if (BANNER_APPEARANCE.isActive(renderTicks)) {
                float progress = BANNER_APPEARANCE.getProgress(renderTicks);

                float scaleX = Easing.OCT_EASE_OUT.lerp(0, 1.0f, progress);
                context.scaleAround(scaleX, 1, 15, -12);
            }
            context.translate(0, globalSinY);
            drawBanner(context);
            context.pop();
        }

        if (ICON_APPEARANCE.isStarted(renderTicks)) {
            context.push();
            if (ICON_APPEARANCE.isActive(renderTicks)) {
                float progress = ICON_APPEARANCE.getProgress(renderTicks);

                float scale = Easing.ELASTIC_OUT.lerp(0, 1.0f, progress);
                context.scaleAround(scale, 68 + 13, 13);

                float rotation = Easing.ELASTIC_OUT.lerp(-1.0f, 0, progress);
                context.rotateAround(rotation, 68 + 13, 13);
            }
            else if (ICON_MOVEMENT.isStarted(renderTicks)) {
                float progress = ICON_MOVEMENT.getProgress(renderTicks);

                float x = Easing.OCT_EASE_OUT.lerp(0, -60.0f, progress);
                context.translate(x, 0);
            }
            context.translate(0, globalSinY - 5);
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
    }

    @Override
    public int getDuration() {
        return DURATION;
    }

    @Override
    public int getToastSoundTiming() {
        return TEXT_APPEARANCE.startTicks() + 4;
    }
}