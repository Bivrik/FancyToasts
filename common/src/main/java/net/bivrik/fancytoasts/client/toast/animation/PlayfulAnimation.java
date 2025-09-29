package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.toast.FancyAdvancementToast;
import net.bivrik.fancytoasts.platform.utility.GUIs;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.bivrik.fancytoasts.utility.MathEasing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.ArrayList;
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

    private List<FormattedCharSequence> TITLE = new ArrayList<>();
    private List<FormattedCharSequence> DESCRIPTION = new ArrayList<>();

    @Override
    public void setup(FancyAdvancementSetup setup, FancyAdvancementToast toast) {
        super.setup(setup, toast);

        TITLE = toast.getMinecraft().font.split(setup.display().getTitle(), this.toast.getWidth() - 16);
        DESCRIPTION = toast.getMinecraft().font.split(setup.display().getDescription(), this.toast.getWidth() - 16);
    }

    @Override
    public void draw(GuiGraphics graphics, Minecraft minecraft, long time) {
        super.draw(graphics, minecraft, time);

        float iconAppearProgress = ICON_APPEARANCE.getProgress(time);
        float iconMovementProgress = ICON_MOVEMENT.getProgress(time);
        float bannerAppearProgress = BANNER_APPEARANCE.getProgress(time);
        float backgroundAppearProgress = BACKGROUND_APPEARANCE.getProgress(time);
        float textAppearProgress = TEXT_APPEARANCE.getProgress(time);
        float fadeOutProgress = getProgress(time, FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

        var pose = GUIs.getStack(graphics);

        float sinY = (float) (Math.sin(time / 400.0)) - 3;

        if (fadeOutProgress > 0) {
            float fadeOutScale = MathEasing.easeInLerp(1f, 0f, fadeOutProgress);
            float toastCenterX = (float) this.toast.getWidth() / 2;
            float toastCenterY = (float) this.toast.getHeight() / 2;

            GUIs.push(pose);
            GUIs.translate(pose, toastCenterX, toastCenterY);
            GUIs.scale(pose, fadeOutScale);
            GUIs.translate(pose, -toastCenterX, -toastCenterY);
        }

        if (backgroundAppearProgress > 0) {
            GUIs.push(pose);
            if (backgroundAppearProgress != 1) {
                float scale = MathEasing.elasticEaseOutLerp(0f, 1f, backgroundAppearProgress);
                float rotation = MathEasing.elasticEaseOutLerp(-1f, 0f, backgroundAppearProgress);
                int y = MathEasing.easeOutLerp(-20, 0, backgroundAppearProgress);

                GUIs.translate(pose, 76, 0);
                GUIs.scale(pose, scale);
                GUIs.rotate(pose, rotation);
                GUIs.translate(pose, -76, 0);
                GUIs.translate(pose, 0, y);
            }
            this.drawBackground(graphics);
            GUIs.pop(pose);
        }

        if (bannerAppearProgress > 0) {
            GUIs.push(pose);
            if (bannerAppearProgress != 1) {
                float scaleX = MathEasing.easeOutLerp(0f, 1f, bannerAppearProgress);

                GUIs.translate(pose, 15, -5 -7);
                GUIs.scale(pose, scaleX, 1);
                GUIs.translate(pose, -15, 5 + 7);
            }
            GUIs.translate(pose, 0, sinY);
            this.drawBanner(graphics);
            GUIs.pop(pose);
        }

        if (iconAppearProgress > 0) {
            GUIs.push(pose);
            if (iconAppearProgress != 1) {
                float scale = MathEasing.elasticEaseOutLerp(0f, 1f, iconAppearProgress);
                float rotation = MathEasing.elasticEaseOutLerp(-1f, 0f, iconAppearProgress);

                GUIs.translate(pose, 68 + 13, 13);
                GUIs.scale(pose, scale);
                GUIs.rotate(pose, rotation);
                GUIs.translate(pose, -68 -13, -13);
            }
            else if (iconMovementProgress > 0) {
                float x = MathEasing.easeOutLerp(0f, -60f, iconMovementProgress);

                GUIs.translate(pose, x, 0);
            }
            GUIs.translate(pose, 0, sinY - 5);
            this.drawIcon(graphics);
            GUIs.pop(pose);
        }

        if (textAppearProgress > 0) {
            int a = Mth.floor(textAppearProgress * 255.0F);
            int titleColor = Colors.alpha(a, this.setup.titleColor());
            int toastColor = Colors.alpha(a, this.setup.toastColor());

            var font = minecraft.font;

            // Title
            if (!TITLE.isEmpty()) {
                FormattedCharSequence titleLine = TITLE.get(0);
                if (TITLE.size() == 1) {
                    graphics.drawCenteredString(font, titleLine, this.toast.getWidth() / 2, 25, titleColor);
                } else {
                    graphics.drawCenteredString(font, titleLine, this.toast.getWidth() / 2 - font.width("...") / 2, 25, titleColor);
                    graphics.drawCenteredString(font, "...", 1 + this.toast.getWidth() / 2 + font.width(titleLine) / 2, 25, titleColor);
                }
            }

            // Description
            if (!DESCRIPTION.isEmpty()) {
                graphics.drawString(font, DESCRIPTION.get(0), 8, 38, toastColor);
                if (DESCRIPTION.size() > 1) {
                    var descriptionSecondLine = DESCRIPTION.get(1);

                    graphics.drawString(font, descriptionSecondLine, 8, 47, toastColor);
                    if (DESCRIPTION.size() > 2) {
                        graphics.drawString(font, "...", 8 + font.width(descriptionSecondLine), 47, toastColor);
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