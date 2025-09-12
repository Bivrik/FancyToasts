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

public class PlayfulAnimation extends FancyAdvancementToastAnimation {
    private static final Appearance ICON_APPEARANCE = new Appearance(1000, 0);
    private static final Appearance ICON_MOVEMENT = new Appearance(1500, 1000);
    private static final Appearance BANNER_APPEARANCE = new Appearance(1000, 1500);
    private static final Appearance BACKGROUND_APPEARANCE = new Appearance(800, 1600);
    private static final Appearance TEXT_APPEARANCE = new Appearance(1000, 2000);

    private static final int FADE_OUT_DURATION = 1500;
    private static final int DURATION = 6000 + FADE_OUT_DURATION;

    @Override
    public void draw(GuiGraphics graphics, Minecraft minecraft, FancyAdvancementToast fancyToast, long time) {
        float iconAppearProgress = ICON_APPEARANCE.getProgress(time);
        float iconMovementProgress = ICON_MOVEMENT.getProgress(time);
        float bannerAppearProgress = BANNER_APPEARANCE.getProgress(time);
        float backgroundAppearProgress = BACKGROUND_APPEARANCE.getProgress(time);
        float textAppearProgress = TEXT_APPEARANCE.getProgress(time);
        float fadeOutProgress = getProgress(time, FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

        var setup = this.getSetup();
        var pose = GUIHelper.get(graphics);

        float sinY = (float) (Math.sin(time / 400.0)) - 3;

        if (fadeOutProgress > 0) {
            float fadeOutScale = MathEasing.easeInLerp(1f, 0f, fadeOutProgress);
            float toastCenterX = (float) fancyToast.getWidth() / 2;
            float toastCenterY = (float) fancyToast.getHeight() / 2;

            GUIHelper.push(pose);
            GUIHelper.translate(pose, toastCenterX, toastCenterY);
            GUIHelper.scale(pose, fadeOutScale);
            GUIHelper.translate(pose, -toastCenterX, -toastCenterY);
        }

        if (backgroundAppearProgress > 0) {
            GUIHelper.push(pose);
            if (backgroundAppearProgress != 1) {
                float scale = MathEasing.elasticEaseOutLerp(0f, 1f, backgroundAppearProgress);
                float rotation = MathEasing.elasticEaseOutLerp(-1f, 0f, backgroundAppearProgress);
                int y = MathEasing.easeOutLerp(-20, 0, backgroundAppearProgress);

                GUIHelper.translate(pose, 76, 0);
                GUIHelper.scale(pose, scale);
                GUIHelper.rotate(pose, rotation);
                GUIHelper.translate(pose, -76, 0);
                GUIHelper.translate(pose, 0, y);
            }
            this.drawBackground(graphics);
            GUIHelper.pop(pose);
        }

        if (bannerAppearProgress > 0) {
            GUIHelper.push(pose);
            if (bannerAppearProgress != 1) {
                float scaleX = MathEasing.easeOutLerp(0f, 1f, bannerAppearProgress);

                GUIHelper.translate(pose, 15, -5 -7);
                GUIHelper.scale(pose, scaleX, 1);
                GUIHelper.translate(pose, -15, 5 + 7);
            }
            GUIHelper.translate(pose, 0, sinY);
            this.drawBanner(graphics);
            GUIHelper.pop(pose);
        }

        if (iconAppearProgress > 0) {
            GUIHelper.push(pose);
            if (iconAppearProgress != 1) {
                float scale = MathEasing.elasticEaseOutLerp(0f, 1f, iconAppearProgress);
                float rotation = MathEasing.elasticEaseOutLerp(-1f, 0f, iconAppearProgress);

                GUIHelper.translate(pose, 68 + 13, 13);
                GUIHelper.scale(pose, scale);
                GUIHelper.rotate(pose, rotation);
                GUIHelper.translate(pose, -68 -13, -13);
            }
            else if (iconMovementProgress > 0) {
                float x = MathEasing.easeOutLerp(0f, -60f, iconMovementProgress);

                GUIHelper.translate(pose, x, 0);
            }
            GUIHelper.translate(pose, 0, sinY - 5);
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
            List<FormattedCharSequence> titleList = font.split(display.getTitle(), fancyToast.getWidth() - 16);
            if (!titleList.isEmpty()) {
                FormattedCharSequence titleLine = titleList.get(0);
                if (titleList.size() == 1) {
                    graphics.drawCenteredString(font, titleLine, fancyToast.getWidth() / 2, 25, titleColor);
                } else {
                    graphics.drawCenteredString(font, titleLine, fancyToast.getWidth() / 2 - font.width("...") / 2, 25, titleColor);
                    graphics.drawCenteredString(font, "...", 1 + fancyToast.getWidth() / 2 + font.width(titleLine) / 2, 25, titleColor);
                }
            }

            // Description
            List<FormattedCharSequence> descriptionList = font.split(display.getDescription(), fancyToast.getWidth() - 16);
            if (!descriptionList.isEmpty()) {
                graphics.drawString(font, descriptionList.get(0), 8, 38, toastColor);
                if (descriptionList.size() > 1) {
                    var descriptionSecondLine = descriptionList.get(1);

                    graphics.drawString(font, descriptionSecondLine, 8, 47, toastColor);
                    if (descriptionList.size() > 2) {
                        graphics.drawString(font, "...", 8 + font.width(descriptionSecondLine), 47, toastColor);
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