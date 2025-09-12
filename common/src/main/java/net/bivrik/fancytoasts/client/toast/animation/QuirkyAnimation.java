package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.renderer.GUIHelper;
import net.bivrik.fancytoasts.client.toast.FancyAdvancementToast;
import net.bivrik.fancytoasts.utility.MathEasing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.Random;

import static net.bivrik.fancytoasts.client.toast.animation.Appearance.getProgress;

public class QuirkyAnimation extends FancyAdvancementToastAnimation {
    private static final Appearance ICON_APPEARANCE = new Appearance(2000, 0);
    private static final Appearance ICON_SCALE = new Appearance(3500, 0);
    private static final Appearance BANNER_APPEARANCE = new Appearance(900, 1200);
    private static final Appearance BACKGROUND_APPEARANCE = new Appearance(1000, 1000);
    private static final Appearance TEXT_APPEARANCE = new Appearance(1000, 1800);

    private static final int FADE_OUT_DURATION = 1500;
    private static final int DURATION = 5000 + FADE_OUT_DURATION;

    private static final Random random = new Random();
    private static float randomRotation;

    @Override
    public void setup(FancyAdvancementSetup setup) {
        super.setup(setup);

        randomRotation = random.nextFloat(-0.4f, 0.4f);
    }

    @Override
    public void draw(GuiGraphics graphics, Minecraft minecraft, FancyAdvancementToast fancyToast, long time) {
        float iconAppearProgress = ICON_APPEARANCE.getProgress(time);
        float iconScaleProgress = ICON_SCALE.getProgress(time);
        float bannerAppearProgress = BANNER_APPEARANCE.getProgress(time);
        float backgroundAppearProgress = BACKGROUND_APPEARANCE.getProgress(time);
        float textAppearProgress = TEXT_APPEARANCE.getProgress(time);
        float fadeOutProgress = getProgress(time, FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

        var setup = this.getSetup();
        var pose = GUIHelper.get(graphics);

        float sinX = (float) (Math.sin(time / 800.0) * 7);
        float sinY = (float) (Math.sin(time / 400.0) * 5);

        GUIHelper.push(pose);
        GUIHelper.translate(pose, sinX, sinY);

        if (fadeOutProgress > 0) {
            float fadeOutScaleX = MathEasing.easeInLerp(1f, 0f, fadeOutProgress);
            float fadeOutRotation = MathEasing.easeInLerp(0f, randomRotation, fadeOutProgress);
            float toastCenterX = (float) fancyToast.getWidth() / 2;
            float toastCenterY = (float) fancyToast.getHeight() / 2;

            GUIHelper.push(pose);
            GUIHelper.translate(pose, toastCenterX, toastCenterY);
            GUIHelper.scale(pose, fadeOutScaleX);
            GUIHelper.rotate(pose, fadeOutRotation);
            GUIHelper.translate(pose, -toastCenterX, -toastCenterY);
        }

        if (bannerAppearProgress > 0) {
            GUIHelper.push(pose);
            float y = 32;
            if (bannerAppearProgress != 1) {
                y = MathEasing.easeOutLerp(-40f, 32f, bannerAppearProgress);
            }
            GUIHelper.translate(pose, 0, y);
            this.drawBanner(graphics);
            GUIHelper.pop(pose);
        }

        if (backgroundAppearProgress > 0) {
            GUIHelper.push(pose);
            int y = -25;
            if (backgroundAppearProgress != 1) {
                y = MathEasing.easeOutLerp(-120, -25, backgroundAppearProgress);
            }
            GUIHelper.translate(pose, 0, y);
            this.drawBackground(graphics);
            GUIHelper.pop(pose);
        }

        if (iconAppearProgress > 0) {
            GUIHelper.push(pose);
            float posY = 29;
            if (iconAppearProgress != 1) {
                posY = MathEasing.easeOutLerp(-120, 29, iconAppearProgress);
            }
            if (iconScaleProgress != 1 && iconScaleProgress > 0) {
                float scale = MathEasing.easeOutLerp(3f, 1f, iconScaleProgress);

                GUIHelper.translate(pose, 68 + 13, 17);
                GUIHelper.scale(pose, scale);
                GUIHelper.translate(pose, -68 - 13, -17);
            }
            GUIHelper.translate(pose, 0, (float) (Math.sin(time / 400.0) * -1.2) + posY);
            this.drawIcon(graphics);
            GUIHelper.pop(pose);
        }

        if (textAppearProgress > 0) {
            int a = Mth.floor(textAppearProgress * 255.0F) << 24 | 67108864;
            int titleColor = setup.titleColor() | a;
            int toastColor = setup.toastColor() | a;

            var font = minecraft.font;
            var display = setup.display();

            int toastCenterX = fancyToast.getWidth() / 2;

            // Title
            graphics.drawCenteredString(font, display.getType().getDisplayName(), toastCenterX, 0, titleColor);

            // Description
            List<FormattedCharSequence> descriptionList = font.split(display.getDescription(), fancyToast.getWidth() - 16);
            if (!descriptionList.isEmpty()) {
                graphics.drawCenteredString(font, descriptionList.get(0), toastCenterX, 12, toastColor);
                if (descriptionList.size() > 1) {
                    var descriptionSecondLine = descriptionList.get(1);

                    if (descriptionList.size() > 2) {
                        graphics.drawCenteredString(font, descriptionSecondLine, toastCenterX - font.width("...") / 2, 21, toastColor);
                        graphics.drawCenteredString(font, "...", toastCenterX + font.width(descriptionSecondLine) / 2, 21, toastColor);
                    } else {
                        graphics.drawCenteredString(font, descriptionSecondLine, toastCenterX, 21, toastColor);
                    }
                }
            }
        }

        if (fadeOutProgress > 0) {
            GUIHelper.pop(pose);
        }

        GUIHelper.pop(pose);
    }

    @Override
    public int getDuration() {
        return DURATION;
    }

    @Override
    public int getToastSoundTiming() {
        return 1000;
    }
}