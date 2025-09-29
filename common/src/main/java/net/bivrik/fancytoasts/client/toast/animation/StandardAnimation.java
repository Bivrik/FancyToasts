package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.platform.utility.GUIs;
import net.bivrik.fancytoasts.client.toast.FancyAdvancementToast;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.bivrik.fancytoasts.utility.MathEasing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

import static net.bivrik.fancytoasts.client.toast.animation.Appearance.getProgress;

public class StandardAnimation extends FancyAdvancementToastAnimation {
    private static final Appearance ICON_APPEARANCE = new Appearance(2000, 0);
    private static final Appearance BANNER_APPEARANCE = new Appearance(500, 1500);
    private static final Appearance BACKGROUND_APPEARANCE = new Appearance(800, 1600);
    private static final Appearance TEXT_APPEARANCE = new Appearance(1000, 2000);

    private static final int FADE_OUT_DURATION = 2000;
    private static final int DURATION = 6000 + FADE_OUT_DURATION;

    private List<FormattedCharSequence> DESCRIPTION = new ArrayList<>();

    @Override
    public void setup(FancyAdvancementSetup setup, FancyAdvancementToast toast) {
        super.setup(setup, toast);

        DESCRIPTION = toast.getMinecraft().font.split(setup.display().getTitle(), this.toast.getWidth() - 20);
    }

    @Override
    public void draw(GuiGraphics graphics, Minecraft minecraft, long time) {
        super.draw(graphics, minecraft, time);

        float iconAppearProgress = ICON_APPEARANCE.getProgress(time);
        float bannerAppearProgress = BANNER_APPEARANCE.getProgress(time);
        float backgroundAppearProgress = BACKGROUND_APPEARANCE.getProgress(time);
        float textAppearProgress = TEXT_APPEARANCE.getProgress(time);
        float fadeOutProgress = getProgress(time, FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

        var pose = GUIs.getStack(graphics);

        if (fadeOutProgress > 0) {
            float fadeOutY = MathEasing.easeInLerp(0, -80, fadeOutProgress);

            GUIs.push(pose);
            GUIs.translate(pose, 0, fadeOutY);
        }

        if (backgroundAppearProgress > 0) {
            GUIs.push(pose);
            if (backgroundAppearProgress != 1) {
                int y = MathEasing.easeOutLerp(-200, 0, backgroundAppearProgress);

                GUIs.translate(pose, 0, y);
            }
            this.drawBackground(graphics);
            GUIs.pop(pose);
        }

        if (bannerAppearProgress > 0) {
            GUIs.push(pose);
            if (bannerAppearProgress != 1) {
                float xScale = MathEasing.easeOutLerp(0.0f, 1.0f, bannerAppearProgress);

                GUIs.translate(pose, 81, 0);
                GUIs.scale(pose, xScale, 1);
                GUIs.translate(pose, -81, 0);
            }
            this.drawBanner(graphics);
            GUIs.pop(pose);
        }

        if (iconAppearProgress > 0) {
            GUIs.push(pose);
            if (iconAppearProgress != 1) {
                int y = MathEasing.easeOutLerp(-100, 0, iconAppearProgress);
                float scale = MathEasing.easeOutLerp(0.0f, 1.0f, iconAppearProgress);

                GUIs.translate(pose, 81, 13);
                GUIs.scale(pose, scale);
                GUIs.translate(pose, -81, -13);
                GUIs.translate(pose, 0, y);
            }
            GUIs.translate(pose, 0, (float) (Math.sin(time / 500.0f) * 1.5f) - 5);
            this.drawIcon(graphics);
            GUIs.pop(pose);
        }

        if (textAppearProgress > 0) {
            int a = Mth.floor(textAppearProgress * 255.0F);
            int titleColor = Colors.alpha(a, this.setup.titleColor());
            int toastColor = Colors.alpha(a, this.setup.toastColor());

            var font = minecraft.font;
            var display = this.setup.display();

            // Title
            graphics.drawCenteredString(font, display.getType().getDisplayName(), this.toast.getWidth() / 2, 25, titleColor);

            // Description
            if (!DESCRIPTION.isEmpty()) {
                if (DESCRIPTION.size() == 1) {
                    graphics.drawCenteredString(font, DESCRIPTION.get(0), this.toast.getWidth() / 2, 43, toastColor);
                } else {
                    int lineHeight = 42 - (9 * (DESCRIPTION.size() - 1)) / 2;
                    for (FormattedCharSequence text : DESCRIPTION) {
                        graphics.drawCenteredString(font, text, this.toast.getWidth() / 2, lineHeight, toastColor);
                        lineHeight += 9;
                    }
                }
            }
        }

        if (fadeOutProgress > 0) {
            GUIs.pop(pose);
        }
    }

    @Override
    public int getDuration() {
        return DURATION;
    }

    @Override
    public int getToastSoundTiming() {
        return TEXT_APPEARANCE.startPoint();
    }
}
