package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.config.AdvancementToastScreenBehavior;
import net.bivrik.fancytoasts.client.toast.TypeBasedUVs;
import net.bivrik.fancytoasts.platform.Managers;
import net.bivrik.fancytoasts.platform.utility.AdvancementToastDisplayInfo;
import net.bivrik.fancytoasts.client.toast.TextureUV;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public abstract class FancyAdvancementToastAnimation {
    private List<FormattedCharSequence> titleLines;
    private List<FormattedCharSequence> descriptionLines;

    protected AdvancementToastDisplayInfo displayInfo;
    protected Minecraft minecraft;
    protected int toastWidth;
    protected int toastHeight;

    private ResourceLocation textureLocation;
    private TypeBasedUVs typeBasedUVs;
    private TextureUV backgroundUV;
    private TextureUV plaqueUV;

    private float guiAlpha;

    public void setup(AnimationSetup setup, Minecraft minecraft, int toastWidth, int toastHeight) {
        this.minecraft = minecraft;
        this.toastWidth = toastWidth;
        this.toastHeight = toastHeight;
        this.textureLocation = setup.textureLocation();
        this.displayInfo = setup.displayInfo();
        this.typeBasedUVs = setup.typeBasedUVs();
        this.backgroundUV = setup.backgroundUV();
        this.plaqueUV = setup.plaqueUV();
    }

    protected void setLines(Component title, Component description) {
        titleLines = minecraft.font.split(title, 142);
        descriptionLines = minecraft.font.split(description, 142);
    }

    protected List<FormattedCharSequence> getTitleLines() {
        return titleLines;
    }

    protected List<FormattedCharSequence> getDescriptionLines() {
        return descriptionLines;
    }

    public void draw(GuiGraphics guiGraphics, long time) {
        if (Managers.advancementToastManager().isScreenOpened()
                && Managers.configManager().generalConfig().getScreenBehavior() == AdvancementToastScreenBehavior.TRANSPARENT) {
            guiAlpha = 0.5F;
        }
        else {
            guiAlpha = 1.0F;
        }
    }

    public abstract int getDuration();

    public abstract int getToastSoundTiming();

    protected void drawIcon(GuiContext guiContext, float alpha) {
        guiContext.drawTexture(textureLocation, 68, 0, 26, 26, typeBasedUVs.frame(), getColor(alpha));
        guiContext.guiGraphics().renderFakeItem(displayInfo.getIcon(), 73, 5);
    }
    protected void drawIcon(GuiContext guiContext) {
        drawIcon(guiContext, 1);
    }

    protected void drawBanner(GuiContext guiContext, float alpha) {
        guiContext.drawTexture(textureLocation, 0, 5, 162, 14, typeBasedUVs.banner(), getColor(alpha));
    }
    protected void drawBanner(GuiContext guiContext) {
        drawBanner(guiContext, 1);
    }

    protected void drawBackground(GuiContext guiContext, float alpha) {
        int color = getColor(alpha);
        guiContext.drawTexture(textureLocation, 0, 20, 162, 40, backgroundUV, color);
        guiContext.drawTexture(textureLocation, 144, 56, 9, 14, plaqueUV, color);
    }
    protected void drawBackground(GuiContext guiContext) {
        drawBackground(guiContext, 1);
    }

    protected void drawTitle(GuiGraphics guiGraphics, float alpha) {
        if (titleLines.isEmpty()) {
            return;
        }

        int toastCenterX = toastWidth / 2;
        int titleColor = Colors.alpha(alpha, displayInfo.getAdvancementType().getMainColor());
        FormattedCharSequence titleLine = titleLines.getFirst();

        if (titleLines.size() == 1) {
            guiGraphics.drawCenteredString(minecraft.font, titleLine, toastCenterX, 25, titleColor);
        } else {
            guiGraphics.drawCenteredString(minecraft.font, titleLine, toastCenterX - minecraft.font.width("...") / 2, 25, titleColor);
            guiGraphics.drawCenteredString(minecraft.font, "...", toastCenterX + 1 + minecraft.font.width(titleLine) / 2, 25, titleColor);
        }
    }
    protected void drawTitle(GuiGraphics guiGraphics) {
        drawTitle(guiGraphics, 1);
    }

    protected void drawDescription(GuiGraphics guiGraphics, float alpha) {
        if (descriptionLines.isEmpty()) {
            return;
        }

        int descriptionColor = Colors.alpha(alpha, displayInfo.getAdvancementType().getSecondaryColor());

        guiGraphics.drawString(minecraft.font, descriptionLines.get(0), 8, 38, descriptionColor);
        if (descriptionLines.size() > 1) {
            var descriptionSecondLine = descriptionLines.get(1);
            guiGraphics.drawString(minecraft.font, descriptionSecondLine, 8, 47, descriptionColor);

            if (descriptionLines.size() > 2) {
                guiGraphics.drawString(minecraft.font, "...", 8 + minecraft.font.width(descriptionSecondLine), 47, descriptionColor);
            }
        }
    }
    protected void drawDescription(GuiGraphics guiGraphics) {
        drawDescription(guiGraphics, 1);
    }

    protected int getColor(float alpha) {
        return Colors.alpha(guiAlpha * alpha, Colors.WHITE);
    }
}
