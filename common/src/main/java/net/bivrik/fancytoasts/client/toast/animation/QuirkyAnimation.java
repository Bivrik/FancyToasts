package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.toast.AnimationSetup;
import net.bivrik.fancytoasts.client.toast.Appearance;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.utility.MathEasing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import java.util.Random;

public class QuirkyAnimation extends FancyToastAnimation {
    private final Appearance ICON_APPEARANCE = new Appearance(2000, 0);
    private final Appearance ICON_SCALE = new Appearance(3500, 0);
    private final Appearance BANNER_APPEARANCE = new Appearance(900, 1200);
    private final Appearance BACKGROUND_APPEARANCE = new Appearance(1000, 1000);
    private final Appearance TEXT_APPEARANCE = new Appearance(1000, 1800);

    private final int FADE_OUT_DURATION = 1500;
    private final int DURATION = 5000 + FADE_OUT_DURATION;

    private float randomRotation;

    @Override
    public void setup(AnimationSetup setup, Minecraft minecraft, int toastWidth, int toastHeight) {
        super.setup(setup, minecraft, toastWidth, toastHeight);

        this.setLines(displayInfo.getAdvancementsAnnouncement(), displayInfo.getDescription());
        randomRotation = new Random().nextFloat(-0.4f, 0.4f);
    }

    @Override
    public void draw(GuiGraphics guiGraphics, long time) {
        super.draw(guiGraphics, time);

        float iconAppearProgress = ICON_APPEARANCE.getProgress(time);
        float iconScaleProgress = ICON_SCALE.getProgress(time);
        float bannerAppearProgress = BANNER_APPEARANCE.getProgress(time);
        float backgroundAppearProgress = BACKGROUND_APPEARANCE.getProgress(time);
        float textAppearProgress = TEXT_APPEARANCE.getProgress(time);
        float fadeOutProgress = Appearance.getProgress(time, FADE_OUT_DURATION, DURATION - FADE_OUT_DURATION);

        GuiContext context = new GuiContext(guiGraphics);
        float globalSinX = this.sinusoidLoop(time, 1.0F, 7.0F);
        float globalSinY = this.sinusoidLoop(time, 2.0F, 5.0F);

        context.push();
        context.translate(globalSinX, globalSinY - 20);

        if (fadeOutProgress > 0) {
            float fadeOutScaleX = MathEasing.easeInLerp(1f, 0f, fadeOutProgress);
            float fadeOutRotation = MathEasing.easeInLerp(0f, randomRotation, fadeOutProgress);
            int toastCenterX = toastWidth / 2;
            int toastCenterY = toastHeight / 2;

            context.push();
            context.scaleAround(fadeOutScaleX, toastCenterX, toastCenterY);
            context.rotateAround(fadeOutRotation, toastCenterX, toastCenterY);
        }

        if (bannerAppearProgress > 0) {
            context.push();
            float y = 58;
            if (bannerAppearProgress != 1) {
                y = MathEasing.easeOutLerp(-24.0F, 58.0F, bannerAppearProgress);
            }
            context.translate(0, y);
            this.drawBanner(context);
            context.pop();
        }

        if (backgroundAppearProgress > 0) {
            context.push();
            if (backgroundAppearProgress != 1) {
                float y = MathEasing.easeOutLerp(-95.0F, 0, backgroundAppearProgress);
                context.translate(0, y);
            }
            this.drawBackground(context);
            context.pop();
        }

        if (iconAppearProgress > 0) {
            context.push();
            float posY = 55;
            if (iconAppearProgress != 1) {
                posY = MathEasing.easeOutLerp(-95.0F, 55.0F, iconAppearProgress);
            }
            if (iconScaleProgress > 0 && iconScaleProgress != 1) {
                float scale = MathEasing.easeOutLerp(3.0f, 1.0f, iconScaleProgress);
                context.scaleAround(scale, 68 + 13, 17);
            }
            float sinY = this.sinusoidLoop(time, 2.0F, -1.2F);
            context.translate(0, sinY + posY);
            this.drawIcon(context);
            context.pop();
        }

        if (textAppearProgress > 0) {
            this.drawTitle(guiGraphics, textAppearProgress);
            this.drawDescription(guiGraphics, textAppearProgress);
        }

        if (fadeOutProgress > 0) {
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

        int centerToastX = toastWidth / 2;
        int descriptionColor = Colors.alpha(alpha, displayInfo.getAdvancementType().getSecondaryColor());

        guiGraphics.drawCenteredString(minecraft.font, descriptionLines.get(0), centerToastX, 38, descriptionColor);
        if (descriptionLines.size() > 1) {
            var descriptionSecondLine = descriptionLines.get(1);
            if (descriptionLines.size() == 2) {
                guiGraphics.drawCenteredString(minecraft.font, descriptionSecondLine, centerToastX, 47, descriptionColor);
            } else {
                guiGraphics.drawCenteredString(minecraft.font, descriptionSecondLine, centerToastX - minecraft.font.width("...") / 2, 47, descriptionColor);
                guiGraphics.drawCenteredString(minecraft.font, "...", centerToastX + minecraft.font.width(descriptionSecondLine) / 2, 47, descriptionColor);
            }
        }
    }

    @Override
    public int getDuration() {
        return DURATION;
    }

    @Override
    public int getToastSoundTiming() {
        return TEXT_APPEARANCE.startPoint() + 180;
    }
}