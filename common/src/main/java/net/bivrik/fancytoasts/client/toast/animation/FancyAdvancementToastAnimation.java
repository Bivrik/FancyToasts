package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.config.AdvancementToastScreenBehavior;
import net.bivrik.fancytoasts.client.toast.TypeBasedUVs;
import net.bivrik.fancytoasts.platform.utility.AdvancementToastDisplayInfo;
import net.bivrik.fancytoasts.platform.utility.GUIs;
import net.bivrik.fancytoasts.client.toast.TextureUV;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public abstract class FancyAdvancementToastAnimation {
    private List<FormattedCharSequence> TITLE_LINES;
    private List<FormattedCharSequence> DESCRIPTION_LINES;

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
        TITLE_LINES = minecraft.font.split(title, 142);
        DESCRIPTION_LINES = minecraft.font.split(description, 142);
    }

    protected List<FormattedCharSequence> getTitleLines() {
        return new ArrayList<>(TITLE_LINES);
    }

    protected List<FormattedCharSequence> getDescriptionLines() {
        return new ArrayList<>(DESCRIPTION_LINES);
    }

    public void draw(GuiGraphics guiGraphics, long time) {
        if (Common.getAdvancementToastManager().isScreenOpened()
                && Common.getConfigManager().getGeneralConfig().getScreenBehavior() == AdvancementToastScreenBehavior.TRANSPARENT) {
            guiAlpha = 0.5F;
        }
        else {
            guiAlpha = 1.0F;
        }
    }

    public abstract int getDuration();

    public abstract int getToastSoundTiming();

    protected void drawIcon(GuiGraphics guiGraphics, float alpha) {
        TextureUV iconFrameUV = typeBasedUVs.frame();
        GUIs.drawTexture(guiGraphics, textureLocation, 68, 0, iconFrameUV.u(), iconFrameUV.v(), 26, 26, getColor(alpha));

        guiGraphics.renderFakeItem(displayInfo.getIcon(), 73, 5);
    }
    protected void drawIcon(GuiGraphics guiGraphics) {
        drawIcon(guiGraphics, 1);
    }

    protected void drawBanner(GuiGraphics guiGraphics, float alpha) {
        TextureUV bannerUV = typeBasedUVs.banner();
        GUIs.drawTexture(guiGraphics, textureLocation, 0, 5, bannerUV.u(), bannerUV.v(), 162, 14, getColor(alpha));
    }
    protected void drawBanner(GuiGraphics guiGraphics) {
        drawBanner(guiGraphics, 1);
    }

    protected void drawBackground(GuiGraphics guiGraphics, float alpha) {
        GUIs.drawTexture(guiGraphics, textureLocation, 0, 20, backgroundUV.u(), backgroundUV.v(), 162, 40, getColor(alpha));
        GUIs.drawTexture(guiGraphics, textureLocation, 144, 56, plaqueUV.u(), plaqueUV.v(), 9, 14, getColor(alpha));
    }
    protected void drawBackground(GuiGraphics guiGraphics) {
        drawBackground(guiGraphics, 1);
    }

    protected void drawTitle(GuiGraphics guiGraphics, float alpha) {
        if (TITLE_LINES.isEmpty()) {
            return;
        }

        int toastCenterX = toastWidth / 2;
        int titleColor = Colors.alpha(alpha, displayInfo.getAdvancementType().getMainColor());
        FormattedCharSequence titleLine = TITLE_LINES.getFirst();

        if (TITLE_LINES.size() == 1) {
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
        if (DESCRIPTION_LINES.isEmpty()) {
            return;
        }

        int descriptionColor = Colors.alpha(alpha, displayInfo.getAdvancementType().getSecondaryColor());

        guiGraphics.drawString(minecraft.font, DESCRIPTION_LINES.get(0), 8, 38, descriptionColor);
        if (DESCRIPTION_LINES.size() > 1) {
            var descriptionSecondLine = DESCRIPTION_LINES.get(1);
            guiGraphics.drawString(minecraft.font, descriptionSecondLine, 8, 47, descriptionColor);

            if (DESCRIPTION_LINES.size() > 2) {
                guiGraphics.drawString(minecraft.font, "...", 8 + minecraft.font.width(descriptionSecondLine), 47, descriptionColor);
            }
        }
    }
    protected void drawDescription(GuiGraphics guiGraphics) {
        drawTitle(guiGraphics, 1);
    }

    protected int getColor(float alpha) {
        return Colors.alpha(guiAlpha * alpha, Colors.WHITE);
    }
}
