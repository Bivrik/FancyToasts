package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.toast.FancyAdvancementToast;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.bivrik.fancytoasts.platform.utility.GUIs;
import net.bivrik.fancytoasts.utility.MathEasing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

import static net.bivrik.fancytoasts.client.toast.animation.Appearance.getProgress;

public class OldlikeAnimation extends FancyAdvancementToastAnimation {
    private static final Appearance ICON_APPEARANCE = new Appearance(2000, 0);
    private static final Appearance BANNER_APPEARANCE = new Appearance(2000, 100);
    private static final Appearance BACKGROUND_APPEARANCE = new Appearance(2000, 200);
    private static final Appearance TEXT_APPEARANCE = new Appearance(2000, 1200);

    private static final int FADE_OUT_DURATION = 3000;
    private static final int DURATION = 3500 + FADE_OUT_DURATION;

    private List<FormattedCharSequence> DESCRIPTION = new ArrayList<>();

    @Override
    public void setup(FancyAdvancementSetup setup, FancyAdvancementToast toast) {
        super.setup(setup, toast);

        DESCRIPTION = toast.getMinecraft().font.split(setup.display().getDescription(), this.toast.getWidth() - 16);
    }

    @Override
    public void draw(GuiGraphics guiGraphics, Minecraft minecraft, long time) {
        super.draw(guiGraphics, minecraft, time);

        float iconAppearProgress = ICON_APPEARANCE.getProgress(time);
        float bannerAppearProgress = BANNER_APPEARANCE.getProgress(time);
        float backgroundAppearProgress = BACKGROUND_APPEARANCE.getProgress(time);
        float textAppearProgress = TEXT_APPEARANCE.getProgress(time);
        float fadeOutProgress = getProgress(time, FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

        var stack = GUIs.getStack(guiGraphics);

        if (bannerAppearProgress > 0) {
            GUIs.push(stack);
            float alpha = 1;
            float x = 0;
            if (bannerAppearProgress != 1) {
                alpha = MathEasing.easeOutLerp(0.0F, 1.0F, bannerAppearProgress);
                x = MathEasing.easeOutLerp(35, 0, bannerAppearProgress);
            }
            else if (fadeOutProgress > 0) {
                alpha = MathEasing.easeInLerp(1.0F, 0, fadeOutProgress);
            }
            float y = (float) (5 - Math.sin(time / 700.0F) * 2);
            GUIs.translate(stack, x, y);
            this.drawBanner(guiGraphics, alpha);
            GUIs.pop(stack);
        }

        if (backgroundAppearProgress > 0) {
            GUIs.push(stack);
            float alpha = 1;
            int x = 0;
            if (backgroundAppearProgress != 1) {
                alpha = MathEasing.easeOutLerp(0.0F, 1.0F, backgroundAppearProgress);
                x = MathEasing.easeOutLerp(35, 0, backgroundAppearProgress);
            }
            else if (fadeOutProgress > 0) {
                alpha = MathEasing.easeInLerp(1.0F, 0, fadeOutProgress);
            }
            GUIs.translate(stack, x, 0);
            this.drawBackground(guiGraphics, alpha);
            GUIs.pop(stack);
        }

        if (iconAppearProgress > 0) {
            GUIs.push(stack);
            float alpha = 1;
            int x = 77;
            float scale = 1;
            if (iconAppearProgress != 1) {
                alpha = MathEasing.easeOutLerp(0.0F, 1.0F, iconAppearProgress);
                x = MathEasing.easeOutLerp(115, 77, iconAppearProgress);
            }
            else if (fadeOutProgress > 0) {
                alpha = MathEasing.easeInLerp(1.0F, 0, fadeOutProgress);
                scale = MathEasing.easeInLerp(1.0F, 0, fadeOutProgress);
            }
            GUIs.translate(stack, x, 11);
            GUIs.rotateAround(stack, (float) (Math.cos(time / 500.0) * 0.2F), 68 + 13, 14);
            GUIs.scaleAround(stack, scale, 68 + 13, 14);
            this.drawIcon(guiGraphics, alpha);
            GUIs.pop(stack);
        }

        if (textAppearProgress > 0) {
            float secondTextAppearProgress = Math.max(0.0F, -0.1F + (textAppearProgress * 1.1F));
            int a;
            int a2;
            if (fadeOutProgress <= 0) {
                a = Mth.floor(textAppearProgress * 255.0F);
                a2 = Mth.floor(secondTextAppearProgress * 255.0F);
            } else {
                float fadeOutText = MathEasing.easeInLerp(1.0F, 0, fadeOutProgress) * 255.0F;
                a = Mth.floor(fadeOutText);
                a2 = Mth.floor(fadeOutText);
            }
            int titleColor = Colors.alpha(a, this.setup.titleColor());
            int toastColor = Colors.alpha(a2, this.setup.toastColor());
            int x = MathEasing.elasticEaseOutLerp(50, 0, textAppearProgress);
            int x2 = MathEasing.elasticEaseOutLerp(50, 0, secondTextAppearProgress);

            var font = minecraft.font;

            // Title
            guiGraphics.drawCenteredString(font, setup.display().getType().getDisplayName(), this.toast.getWidth() / 2 + x, 25, titleColor);

            // Description
            if (!DESCRIPTION.isEmpty()) {
                guiGraphics.drawString(font, DESCRIPTION.get(0), 8 + x2, 38, toastColor);
                if (DESCRIPTION.size() > 1) {
                    var descriptionSecondLine = DESCRIPTION.get(1);

                    guiGraphics.drawString(font, descriptionSecondLine, 8 + x2, 47, toastColor);
                    if (DESCRIPTION.size() > 2) {
                        guiGraphics.drawString(font, "...", 8 + font.width(descriptionSecondLine) + x2, 47, toastColor);
                    }
                }
            }
        }
    }

    @Override
    public int getDuration() {
        return DURATION;
    }

    @Override
    public int getToastSoundTiming() {
        return TEXT_APPEARANCE.startPoint() + TEXT_APPEARANCE.duration() / 5;
    }
}