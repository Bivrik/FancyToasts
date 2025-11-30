package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.config.ToastScreenBehavior;
import net.bivrik.fancytoasts.client.toast.AnimationSetup;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.core.event.GeneralConfigDataEvent;
import net.bivrik.fancytoasts.utility.TypeBasedUVs;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.platform.utility.ToastDisplayInfo;
import net.bivrik.fancytoasts.utility.TextureUV;
import net.bivrik.fancytoasts.platform.utility.Colors;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.Objects;

public abstract class FancyToastAnimation {
    private List<FormattedCharSequence> titleLines;
    private List<FormattedCharSequence> descriptionLines;

    protected ToastDisplayInfo displayInfo;
    protected Minecraft minecraft;
    protected int toastWidth;
    protected int toastHeight;

    private ResourceLocation textureLocation;
    private TypeBasedUVs typeBasedUVs;
    private TextureUV backgroundUV;
    private TextureUV plaqueUV;

    private float guiAlpha = 1.0f;
    private boolean shouldTransparentToast;
    private float loopsStrength;
    private float loopsSpeed;

    public void setup(AnimationSetup setup, Minecraft minecraft, int toastWidth, int toastHeight) {
        Managers.getEventManager().subscribe(GeneralConfigDataEvent.class, this::onGeneralConfigDataChanged);
        var data = Managers.getConfigManager().getGeneralConfigData();
        this.shouldTransparentToast = data.getToastScreenBehavior().equals(ToastScreenBehavior.TRANSPARENT);
        this.loopsStrength = data.getLoopsStrength();
        this.loopsSpeed = data.getLoopsSpeed();

        this.minecraft = minecraft;
        this.toastWidth = toastWidth;
        this.toastHeight = toastHeight;
        this.textureLocation = setup.textureLocation();
        this.displayInfo = setup.displayInfo();
        this.typeBasedUVs = setup.typeBasedUVs();
        this.backgroundUV = setup.backgroundUV();
        this.plaqueUV = setup.plaqueUV();
    }

    private void onGeneralConfigDataChanged(GeneralConfigDataEvent event) {
        var data = event.generalConfigData();
        shouldTransparentToast = data.getToastScreenBehavior().equals(ToastScreenBehavior.TRANSPARENT);
        loopsStrength = data.getLoopsStrength();
        loopsSpeed = data.getLoopsSpeed();
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
        if (shouldTransparentToast && Objects.requireNonNull(Managers.getToastManager()).isScreenOpened()) {
            guiAlpha = 0.5f;
        }
        else if (guiAlpha != 1.0f) {
            guiAlpha = 1.0f;
        }
    }

    public abstract int getDuration();

    public abstract int getToastSoundTiming();

    protected void drawIcon(GuiContext guiContext, float alpha) {
        guiContext.drawGUITexture(textureLocation, 68, 0, 26, 26, typeBasedUVs.frame(), getColor(alpha));
        guiContext.guiGraphics().renderFakeItem(displayInfo.getIcon(), 73, 5);
    }
    protected void drawIcon(GuiContext guiContext) {
        drawIcon(guiContext, 1);
    }

    protected void drawBanner(GuiContext guiContext, float alpha) {
        guiContext.drawGUITexture(textureLocation, 0, 5, 162, 14, typeBasedUVs.banner(), getColor(alpha));
    }
    protected void drawBanner(GuiContext guiContext) {
        drawBanner(guiContext, 1);
    }

    protected void drawBackground(GuiContext guiContext, float alpha) {
        int color = getColor(alpha);
        guiContext.drawGUITexture(textureLocation, 0, 20, 162, 40, backgroundUV, color);
        guiContext.drawGUITexture(textureLocation, 144, 56, 9, 14, plaqueUV, color);
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

    private final static float TIME_SCALE = 0.00125f;

    protected float sinusoidLoop(long time, float speed, float strength) {
        float scaledTime = time * TIME_SCALE * speed * loopsSpeed;
        return (float) Math.sin(scaledTime) * strength * loopsStrength;
    }

    protected float cosineLoop(long time, float speed, float strength) {
        float scaledTime = time * TIME_SCALE * speed * loopsSpeed;
        return (float) Math.cos(scaledTime) * strength * loopsStrength;
    }

    protected int getColor(float alpha) {
        return Colors.alpha(guiAlpha * alpha, Colors.WHITE);
    }
}
