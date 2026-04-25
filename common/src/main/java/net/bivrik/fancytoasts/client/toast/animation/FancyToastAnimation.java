package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.config.DisplayTextType;
import net.bivrik.fancytoasts.client.config.ToastScreenBehavior;
import net.bivrik.fancytoasts.client.toast.AnimationSetup;
import net.bivrik.fancytoasts.core.Color;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.core.event.GeneralConfigDataEvent;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.platform.utility.ToastDisplayInfo;
import net.bivrik.fancytoasts.utility.TextureUV;
import net.bivrik.fancytoasts.utility.TypeBasedUVs;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public abstract class FancyToastAnimation {
    private final Consumer<GeneralConfigDataEvent> generalConfigDataEventConsumer;

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
    private Advancement advancement;

    private float guiAlpha = 1.0f;
    private boolean shouldTransparentToast;
    private float loopsStrength;
    private float loopsSpeed;
    private DisplayTextType titleDisplayTextType;
    private DisplayTextType descriptionDisplayTextType;
    protected Style titleStyle;
    protected Style descriptionStyle;

    FancyToastAnimation() {
        generalConfigDataEventConsumer = this::onGeneralConfigDataChanged;
        Managers.getEventManager().subscribeToEvent(GeneralConfigDataEvent.class, generalConfigDataEventConsumer);
    }

    public void setup(AnimationSetup setup, Minecraft minecraft, int toastWidth, int toastHeight, Advancement advancement) {
        var data = Managers.getConfigManager().getGeneralConfigData();
        this.shouldTransparentToast = data.getToastScreenBehavior().equals(ToastScreenBehavior.TRANSPARENT);
        this.loopsStrength = data.getLoopsStrength();
        this.loopsSpeed = data.getLoopsSpeed();
        this.titleDisplayTextType = data.getTitleDisplayTextType();
        this.descriptionDisplayTextType = data.getDescriptionDisplayTextType();

        this.minecraft = minecraft;
        this.toastWidth = toastWidth;
        this.toastHeight = toastHeight;
        this.advancement = advancement;
        this.textureLocation = setup.textureLocation();
        this.displayInfo = setup.displayInfo();
        this.typeBasedUVs = setup.typeBasedUVs();
        this.backgroundUV = setup.backgroundUV();
        this.plaqueUV = setup.plaqueUV();
    }

    public void unsubscribeFromGeneralConfigDataEvent() {
        Managers.getEventManager().unsubscribeFromEvent(GeneralConfigDataEvent.class, generalConfigDataEventConsumer);
    }

    private void onGeneralConfigDataChanged(GeneralConfigDataEvent event) {
        var data = event.generalConfigData();
        shouldTransparentToast = data.getToastScreenBehavior().equals(ToastScreenBehavior.TRANSPARENT);
        loopsStrength = data.getLoopsStrength();
        loopsSpeed = data.getLoopsSpeed();
    }

    protected void setLines(Component toastTitle, Component toastDescription) {
        Component title = titleDisplayTextType.getDisplayTextOrElse(displayInfo, toastTitle);
        Component description = descriptionDisplayTextType.getDisplayTextOrElse(displayInfo, toastDescription);

        titleStyle = title.getStyle();
        descriptionStyle = description.getStyle();

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
        if (Services.DAWN_ERA_HELPER.isLoaded() && advancement != null && Services.DAWN_ERA_HELPER.isCustomIcon(advancement)) {
            Services.DAWN_ERA_HELPER.drawCustomIcon(guiContext.guiGraphics(), advancement, 66, 0);
        } else {
            guiContext.guiGraphics().renderFakeItem(displayInfo.getIcon(), 73, 5);
        }
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
        int titleColorARGB = displayInfo.getAdvancementType().getMainColor().withAlpha(alpha).getARGB();
        FormattedCharSequence titleLine = titleLines.get(0);

        if (titleLines.size() == 1) {
            guiGraphics.drawCenteredString(minecraft.font, titleLine, toastCenterX, 25, titleColorARGB);
        } else {
            guiGraphics.drawCenteredString(minecraft.font, FormattedCharSequence.composite(titleLine, getDots(titleStyle)), toastCenterX , 25, titleColorARGB);
        }
    }

    protected void drawDescription(GuiGraphics guiGraphics, float alpha) {
        if (descriptionLines.isEmpty()) {
            return;
        }

        int descriptionColorARGB = displayInfo.getAdvancementType().getSecondaryColor().withAlpha(alpha).getARGB();

        guiGraphics.drawString(minecraft.font, descriptionLines.get(0), 8, 38, descriptionColorARGB);
        if (descriptionLines.size() > 1) {
            var descriptionSecondLine = descriptionLines.get(1);
            guiGraphics.drawString(minecraft.font, descriptionSecondLine, 8, 47, descriptionColorARGB);

            if (descriptionLines.size() > 2) {
                guiGraphics.drawString(minecraft.font, getDots(descriptionStyle), minecraft.font.width(descriptionSecondLine) + 8, 47, descriptionColorARGB);
            }
        }
    }

    protected FormattedCharSequence getDots(Style style) {
        return FormattedCharSequence.forward("...", style);
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
        return Color.WHITE.withAlpha(guiAlpha * alpha).getARGB();
    }
}
