package net.bivrik.fancytoasts.core.manager;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.client.config.ToastScreenBehavior;
import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.config.data.ToastConfigData;
import net.bivrik.fancytoasts.client.toast.FancyAdvancementToast;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.core.event.GeneralConfigDataEvent;
import net.bivrik.fancytoasts.core.event.ToastConfigDataEvent;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.AdvancementDisplay;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2d;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class FancyToastManager {
    private final Deque<FancyAdvancementToast> toasts = new ConcurrentLinkedDeque<>();
    private final Minecraft minecraft;

    private volatile FancyAdvancementToast currentToast;

    private final CustomTextureManager customTextureManager;
    private GeneralConfigData generalConfigData;
    private ToastConfigData toastConfigData;

    public FancyToastManager(Minecraft minecraft, CustomTextureManager customTextureManager, ConfigManager configManager) {
        this.minecraft = minecraft;

        this.customTextureManager = customTextureManager;
        this.generalConfigData = configManager.getGeneralConfigData();
        this.toastConfigData = configManager.getToastConfigData();

        FancyToasts.EVENTS.subscribeToEvent(GeneralConfigDataEvent.class, this::onGeneralConfigDataChanged);
        FancyToasts.EVENTS.subscribeToEvent(ToastConfigDataEvent.class, this::onToastConfigDataChanged);
    }

    private void onGeneralConfigDataChanged(GeneralConfigDataEvent event) {
        generalConfigData = event.generalConfigData();
    }

    private void onToastConfigDataChanged(ToastConfigDataEvent event) {
        toastConfigData = event.toastConfigData();
    }

    public void add(AdvancementDisplay display, ResourceLocation soundId) {
        if (display == null) {
            return;
        }

        FancyAdvancementToast toast = new FancyAdvancementToast(minecraft, generalConfigData, display,
                soundId, toastConfigData.getTextureId(), toastConfigData.getAnimationId());

        addToast(toast);

        if (generalConfigData.isJadeHiding()) {
            Services.JADE.tryDisable();
        }
    }

    public void tick() {
        if (isToastActive()) {
            if (generalConfigData.isJadeHiding() && Services.JADE.isEnabled()) {
                Services.JADE.tryEnable();
            }

            currentToast.tick();

            if (currentToast.isDead()) {
                removeCurrentToast();

                if (generalConfigData.isJadeHiding() && toasts.isEmpty()) {
                    Services.JADE.tryEnable();
                }
            }

            return;
        }

        if (!toasts.isEmpty()) {
            FancyAdvancementToast nextToast = toasts.pollFirst();
            if (nextToast != null) {
                currentToast = nextToast;
            }
        }
    }

    public void render(GuiGraphics guiGraphics, float partialTick) {
        if (!shouldRender()) {
            return;
        }

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        Vector2d toastPosition = generalConfigData.getToastAnchor().getPosition(screenWidth, screenHeight, generalConfigData.getOffsetX(), -generalConfigData.getOffsetY());
        int xPos = (int) toastPosition.x() - currentToast.getWidth() / 2;
        int yPos = (int) toastPosition.y() - currentToast.getHeight() / 2;

        GuiContext context = new GuiContext(guiGraphics);
        context.push();
        context.translate(xPos, yPos, 2400);
        currentToast.render(guiGraphics, partialTick);
        context.pop();
    }

    public void clear() {
        toasts.clear();
        customTextureManager.clear();
        removeCurrentToast();

        if (generalConfigData.isJadeHiding()) {
            Services.JADE.tryEnable();
        }
    }

    private void addToast(FancyAdvancementToast toast) {
        customTextureManager.addBeingUsed(toastConfigData.getTextureId(), toast);
        toasts.add(toast);
    }

    private void removeCurrentToast() {
        customTextureManager.removeBeingUsed(currentToast);
        currentToast = null;
    }

    public boolean isToastActive() {
        return currentToast != null;
    }

    public boolean shouldRender() {
        return isToastActive() && !minecraft.options.hideGui;
    }

    public boolean isScreenOpened() {
        return minecraft.screen != null && !(minecraft.screen instanceof ChatScreen);
    }

    public boolean shouldRenderBehind() {
        return generalConfigData.getToastScreenBehavior() == ToastScreenBehavior.BEHIND && isScreenOpened();
    }
}
