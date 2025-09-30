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
    public void draw(GuiGraphics guiGraphics, Minecraft minecraft, long time) {
        super.draw(guiGraphics, minecraft, time);

        float iconAppearProgress = ICON_APPEARANCE.getProgress(time);
        float iconMovementProgress = ICON_MOVEMENT.getProgress(time);
        float bannerAppearProgress = BANNER_APPEARANCE.getProgress(time);
        float backgroundAppearProgress = BACKGROUND_APPEARANCE.getProgress(time);
        float textAppearProgress = TEXT_APPEARANCE.getProgress(time);
        float fadeOutProgress = getProgress(time, FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

        var stack = GUIs.getStack(guiGraphics);

        float sinY = (float) (Math.sin(time / 400.0)) - 3;

        if (fadeOutProgress > 0) {
            float fadeOutScale = MathEasing.easeInLerp(1f, 0f, fadeOutProgress);
            float toastCenterX = (float) this.toast.getWidth() / 2;
            float toastCenterY = (float) this.toast.getHeight() / 2;

            GUIs.push(stack);
            GUIs.scaleAround(stack, fadeOutScale, toastCenterX, toastCenterY);
        }

        if (backgroundAppearProgress > 0) {
            GUIs.push(stack);
            if (backgroundAppearProgress != 1) {
                float scale = MathEasing.elasticEaseOutLerp(0f, 1f, backgroundAppearProgress);
                float rotation = MathEasing.elasticEaseOutLerp(-1f, 0f, backgroundAppearProgress);
                int y = MathEasing.easeOutLerp(-20, 0, backgroundAppearProgress);

                GUIs.scaleAround(stack, scale, 76, 0);
                GUIs.rotateAround(stack, rotation, 76, 0);
                GUIs.translate(stack, 0, y);
            }
            this.drawBackground(guiGraphics);
            GUIs.pop(stack);
        }

        if (bannerAppearProgress > 0) {
            GUIs.push(stack);
            if (bannerAppearProgress != 1) {
                float scaleX = MathEasing.easeOutLerp(0f, 1f, bannerAppearProgress);

                GUIs.scaleAround(stack, scaleX, 1, 15, -12);
            }
            GUIs.translate(stack, 0, sinY);
            this.drawBanner(guiGraphics);
            GUIs.pop(stack);
        }

        if (iconAppearProgress > 0) {
            GUIs.push(stack);
            if (iconAppearProgress != 1) {
                float scale = MathEasing.elasticEaseOutLerp(0f, 1f, iconAppearProgress);
                float rotation = MathEasing.elasticEaseOutLerp(-1f, 0f, iconAppearProgress);

                GUIs.scaleAround(stack, scale, 68 + 13, 13);
                GUIs.rotateAround(stack, rotation, 68 + 13, 13);
            }
            else if (iconMovementProgress > 0) {
                float x = MathEasing.easeOutLerp(0f, -60f, iconMovementProgress);

                GUIs.translate(stack, x, 0);
            }
            GUIs.translate(stack, 0, sinY - 5);
            this.drawIcon(guiGraphics);
            GUIs.pop(stack);
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
                    guiGraphics.drawCenteredString(font, titleLine, this.toast.getWidth() / 2, 25, titleColor);
                } else {
                    guiGraphics.drawCenteredString(font, titleLine, this.toast.getWidth() / 2 - font.width("...") / 2, 25, titleColor);
                    guiGraphics.drawCenteredString(font, "...", 1 + this.toast.getWidth() / 2 + font.width(titleLine) / 2, 25, titleColor);
                }
            }

            // Description
            if (!DESCRIPTION.isEmpty()) {
                guiGraphics.drawString(font, DESCRIPTION.get(0), 8, 38, toastColor);
                if (DESCRIPTION.size() > 1) {
                    var descriptionSecondLine = DESCRIPTION.get(1);

                    guiGraphics.drawString(font, descriptionSecondLine, 8, 47, toastColor);
                    if (DESCRIPTION.size() > 2) {
                        guiGraphics.drawString(font, "...", 8 + font.width(descriptionSecondLine), 47, toastColor);
                    }
                }
            }
        }

        if (fadeOutProgress > 0) {
            GUIs.pop(stack);
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