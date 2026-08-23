package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.client.config.DisplayTextType;
import net.bivrik.fancytoasts.client.config.ToastScreenBehavior;
import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.toast.AnimationSetup;
import net.bivrik.fancytoasts.core.Color;
import net.bivrik.fancytoasts.core.event.GeneralConfigDataEvent;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.platform.utility.AdvancementDisplay;
import net.bivrik.fancytoasts.utility.FastMath;
import net.bivrik.fancytoasts.utility.TextureUV;
import net.bivrik.fancytoasts.utility.TypeBasedUVs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
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

    protected AdvancementDisplay display;
    protected Font font;
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
    private DisplayTextType titleDisplayTextType;
    private DisplayTextType descriptionDisplayTextType;
    protected Style titleStyle;
    protected Style descriptionStyle;

    public FancyToastAnimation() {
        generalConfigDataEventConsumer = this::onGeneralConfigDataChanged;
        FancyToasts.EVENTS.subscribeToEvent(GeneralConfigDataEvent.class, generalConfigDataEventConsumer);
    }

    public void setup(AnimationSetup setup, GeneralConfigData generalConfig, Font font, int toastWidth, int toastHeight) {
        this.shouldTransparentToast = generalConfig.getToastScreenBehavior().equals(ToastScreenBehavior.TRANSPARENT);
        this.loopsStrength = generalConfig.getLoopsStrength();
        this.loopsSpeed = generalConfig.getLoopsSpeed();
        this.titleDisplayTextType = generalConfig.getTitleDisplayTextType();
        this.descriptionDisplayTextType = generalConfig.getDescriptionDisplayTextType();

        this.font = font;
        this.toastWidth = toastWidth;
        this.toastHeight = toastHeight;
        this.textureLocation = setup.getTextureId();
        this.display = setup.getDisplay();
        this.typeBasedUVs = this.display.getType().getUvs();
        this.backgroundUV = setup.getBackgroundUV();
        this.plaqueUV = setup.getPlaqueUV();
    }

    public void unsubscribeFromGeneralConfigDataEvent() {
        FancyToasts.EVENTS.unsubscribeFromEvent(GeneralConfigDataEvent.class, generalConfigDataEventConsumer);
    }

    private void onGeneralConfigDataChanged(GeneralConfigDataEvent event) {
        var data = event.generalConfigData();
        shouldTransparentToast = data.getToastScreenBehavior().equals(ToastScreenBehavior.TRANSPARENT);
        loopsStrength = data.getLoopsStrength();
        loopsSpeed = data.getLoopsSpeed();
    }

    protected void setLines(Component animationTitle, Component animationDescription) {
        Component title;
        Component description;
        if (!display.getDescription().equals(Component.empty())) {
            title = titleDisplayTextType.getDisplayTextOrElse(display, animationTitle);
            description = descriptionDisplayTextType.getDisplayTextOrElse(display, animationDescription);
        } else {
            title = display.getAnnouncement();
            description = display.getTitle();
        }

        titleStyle = title.getStyle();
        descriptionStyle = description.getStyle();

        titleLines = font.split(title, 142);
        descriptionLines = font.split(description, 142);
    }

    protected List<FormattedCharSequence> getTitleLines() {
        return titleLines;
    }

    protected List<FormattedCharSequence> getDescriptionLines() {
        return descriptionLines;
    }

    public final void renderWithTransparency(GuiGraphics graphics, float timeTicks, float partialTicks) {
        if (shouldTransparentToast && Objects.requireNonNull(FancyToasts.getInstance().getToastManager()).isScreenOpened()) {
            guiAlpha = 0.5f;
        }
        else if (guiAlpha != 1.0f) {
            guiAlpha = 1.0f;
        }

        render(new GuiContext(graphics), timeTicks + partialTicks);
    }

    protected final void drawIcon(GuiContext context, float alpha) {
        context.drawGUITexture(textureLocation, 68, 0, 26, 26, typeBasedUVs.frame(), getColor(alpha).getARGB());
        context.guiGraphics().renderFakeItem(display.getIcon(), 73, 5);
    }
    protected final void drawIcon(GuiContext context) {
        drawIcon(context, 1);
    }

    protected final void drawBanner(GuiContext context, float alpha) {
        context.drawGUITexture(textureLocation, 0, 5, 162, 14, typeBasedUVs.banner(), getColor(alpha).getARGB());
    }
    protected void drawBanner(GuiContext context) {
        drawBanner(context, 1);
    }

    protected final void drawBackground(GuiContext context, float alpha) {
        int color = getColor(alpha).getARGB();
        context.drawGUITexture(textureLocation, 0, 20, 162, 40, backgroundUV, color);
        context.drawGUITexture(textureLocation, 144, 54, 9, 14, plaqueUV, color);
    }
    protected final void drawBackground(GuiContext context) {
        drawBackground(context, 1);
    }

    protected void drawTitle(GuiGraphics graphics, float alpha) {
        if (titleLines.isEmpty()) {
            return;
        }

        int toastCenterX = toastWidth / 2;
        int titleColorARGB = getTitleColor(alpha).getARGB();
        FormattedCharSequence titleLine = titleLines.getFirst();

        if (titleLines.size() == 1) {
            graphics.drawCenteredString(font, titleLine, toastCenterX, 25, titleColorARGB);
        } else {
            graphics.drawCenteredString(font, FormattedCharSequence.composite(titleLine, getDots(titleStyle)), toastCenterX , 25, titleColorARGB);
        }
    }

    protected void drawDescription(GuiGraphics graphics, float alpha) {
        if (descriptionLines.isEmpty()) {
            return;
        }

        int descriptionColorARGB = getDescriptionColor(alpha).getARGB();

        graphics.drawString(font, descriptionLines.get(0), 8, 38, descriptionColorARGB);
        if (descriptionLines.size() > 1) {
            var descriptionSecondLine = descriptionLines.get(1);
            graphics.drawString(font, descriptionSecondLine, 8, 47, descriptionColorARGB);

            if (descriptionLines.size() > 2) {
                graphics.drawString(font, getDots(descriptionStyle), font.width(descriptionSecondLine) + 8, 47, descriptionColorARGB);
            }
        }
    }

    protected final FormattedCharSequence getDots(Style style) {
        return FormattedCharSequence.forward("...", style);
    }

    private Color getColor(float alpha) {
        return Color.WHITE.withAlpha(guiAlpha * alpha);
    }

    /**
     * Some weird transformation value I don't remember why, but it gets the job done I guess. 1/16 of tick time.
     */
    private final static float TIME_SCALE = 0.0625f;

    protected final float sinLoop(float time, float speed, float strength) {
        float scaledTime = time * TIME_SCALE;
        return (float) Math.sin(scaledTime * speed * loopsSpeed) * strength * loopsStrength;
    }

    protected final float cosLoop(float time, float speed, float strength) {
        float scaledTime = time * TIME_SCALE;
        return (float) Math.cos(scaledTime * speed * loopsSpeed) * strength * loopsStrength;
    }

    protected final Color getTitleColor(float alpha) {
        return display.getTitleColor().withAlpha(getTextSafeAlpha(alpha));
    }

    protected final Color getDescriptionColor(float alpha) {
        return display.getDescriptionColor().withAlpha(getTextSafeAlpha(alpha));
    }

    private float getTextSafeAlpha(float alpha) {
        return FastMath.clamp(alpha, 0.04f, 1.0f);
    }

    protected abstract void render(GuiContext context, float renderTicks);

    public abstract int getDuration();

    public abstract int getToastSoundTiming();
}
