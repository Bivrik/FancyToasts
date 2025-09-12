package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.toast.FancyAdvancementToast;
import net.bivrik.fancytoasts.client.renderer.GUIHelper;
import net.bivrik.fancytoasts.utility.MathEasing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

import static net.bivrik.fancytoasts.client.toast.animation.Appearance.getProgress;

public class StandardAnimation extends FancyAdvancementToastAnimation {
    private static final Appearance ICON_APPEARANCE = new Appearance(2000, 0);
    private static final Appearance BANNER_APPEARANCE = new Appearance(500, 1500);
    private static final Appearance BACKGROUND_APPEARANCE = new Appearance(800, 1600);
    private static final Appearance TEXT_APPEARANCE = new Appearance(1000, 2000);

    private static final int FADE_OUT_DURATION = 2000;
    private static final int DURATION = 6000 + FADE_OUT_DURATION;

    @Override
    public void draw(GuiGraphics graphics, Minecraft minecraft, FancyAdvancementToast fancyToast,  long time) {
        float iconAppearProgress = ICON_APPEARANCE.getProgress(time);
        float bannerAppearProgress = BANNER_APPEARANCE.getProgress(time);
        float backgroundAppearProgress = BACKGROUND_APPEARANCE.getProgress(time);
        float textAppearProgress = TEXT_APPEARANCE.getProgress(time);
        float fadeOutProgress = getProgress(time, FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

        var setup = this.getSetup();
        var pose = GUIHelper.get(graphics);

        if (fadeOutProgress > 0) {
            float fadeOutY = MathEasing.easeInLerp(0, -80, fadeOutProgress);

            GUIHelper.push(pose);
            GUIHelper.translate(pose, 0, fadeOutY);
        }

        if (backgroundAppearProgress > 0) {
            GUIHelper.push(pose);
            if (backgroundAppearProgress != 1) {
                int y = MathEasing.easeOutLerp(-200, 0, backgroundAppearProgress);

                GUIHelper.translate(pose, 0, y);
            }
            this.drawBackground(graphics);
            GUIHelper.pop(pose);
        }

        if (bannerAppearProgress > 0) {
            GUIHelper.push(pose);
            if (bannerAppearProgress != 1) {
                float xScale = MathEasing.easeOutLerp(0.0f, 1.0f, bannerAppearProgress);

                GUIHelper.translate(pose, 81, 0);
                GUIHelper.scale(pose, xScale, 1);
                GUIHelper.translate(pose, -81, 0);
            }
            this.drawBanner(graphics);
            GUIHelper.pop(pose);
        }

        if (iconAppearProgress > 0) {
            GUIHelper.push(pose);
            if (iconAppearProgress != 1) {
                int y = MathEasing.easeOutLerp(-100, 0, iconAppearProgress);
                float scale = MathEasing.easeOutLerp(0.0f, 1.0f, iconAppearProgress);

                GUIHelper.translate(pose, 81, 13);
                GUIHelper.scale(pose, scale);
                GUIHelper.translate(pose, -81, -13);
                GUIHelper.translate(pose, 0, y);
            }
            GUIHelper.translate(pose, 0, (float) (Math.sin(time / 500.0f) * 1.5f) - 5);
            this.drawIcon(graphics);
            GUIHelper.pop(pose);
        }

        if (textAppearProgress > 0) {
            int a = Mth.floor(textAppearProgress * 255.0F) << 24 | 67108864;
            int titleColor = setup.titleColor() | a;
            int toastColor = setup.toastColor() | a;

            var font = minecraft.font;
            var display = setup.display();

            // Title
            graphics.drawCenteredString(font, display.getType().getDisplayName(), fancyToast.getWidth() / 2, 25, titleColor);

            // Description
            List<FormattedCharSequence> descriptionList = font.split(display.getTitle(), fancyToast.getWidth() - 20);
            if (!descriptionList.isEmpty()) {
                if (descriptionList.size() == 1) {
                    graphics.drawCenteredString(font, descriptionList.get(0), fancyToast.getWidth() / 2, 43, toastColor);
                } else {
                    int lineHeight = 42 - (9 * (descriptionList.size() - 1)) / 2;
                    for (FormattedCharSequence text : descriptionList) {
                        graphics.drawCenteredString(font, text, fancyToast.getWidth() / 2, lineHeight, toastColor);
                        lineHeight += 9;
                    }
                }
            }
        }

        if (fadeOutProgress > 0) {
            GUIHelper.pop(pose);
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
