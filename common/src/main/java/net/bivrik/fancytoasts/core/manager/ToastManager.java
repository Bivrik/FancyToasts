package net.bivrik.fancytoasts.core.manager;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.client.config.ToastScreenBehavior;
import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.config.data.ToastConfigData;
import net.bivrik.fancytoasts.client.toast.FancyAdvancementToast;
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

public class ToastManager {
    private final Deque<FancyAdvancementToast> toasts = new ConcurrentLinkedDeque<>();

    private Minecraft minecraft;
    private DeltaTracker deltaTracker;

    private volatile FancyAdvancementToast currentToast;

    private final CustomTextureManager customTextureManager;
    private GeneralConfigData generalConfigData;
    private ToastConfigData toastConfigData;

    public ToastManager(Minecraft minecraft, CustomTextureManager customTextureManager, ConfigManager configManager) {
        this.minecraft = minecraft;
        this.deltaTracker = minecraft.getTimer();

        this.customTextureManager = customTextureManager;
        generalConfigData = configManager.getGeneralConfigData();
        toastConfigData = configManager.getToastConfigData();

        FancyToasts.EVENTS.subscribeToEvent(GeneralConfigDataEvent.class, this::onGeneralConfigDataChanged);
        FancyToasts.EVENTS.subscribeToEvent(ToastConfigDataEvent.class, this::onToastConfigDataChanged);
    }

    private void onGeneralConfigDataChanged(GeneralConfigDataEvent event) {
        generalConfigData = event.generalConfigData();
    }

    private void onToastConfigDataChanged(ToastConfigDataEvent event) {
        toastConfigData = event.toastConfigData();
    }

    public void addAdvancement(AdvancementDisplay display, ResourceLocation soundId) {
        if (display == null) return;

        FancyAdvancementToast toast = new FancyAdvancementToast(minecraft, generalConfigData, display,
                soundId, toastConfigData.getTextureId(), toastConfigData.getAnimationId());

        toasts.add(toast);
        customTextureManager.addBeingUsed(toastConfigData.getTextureId(), toast);

        if (generalConfigData.isJadeHiding()) {
            Services.JADE.tryDisable();
        }
    }

    public void update() {
        if (!isEmpty()) {
            if (generalConfigData.isJadeHiding() && Services.JADE.isEnabled()) {
                Services.JADE.tryEnable();
            }

            updateCurrentToast();

            if (currentToast.isEnded()) {
                removeCurrentToast();

                if (generalConfigData.isJadeHiding() && toasts.isEmpty()) {
                    Services.JADE.tryEnable();
                }
            }

            return;
        }

        if (!toasts.isEmpty()) {
            setNewCurrentToast();
        }
    }

    public void render(GuiGraphics guiGraphics) {
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
        context.translate(xPos, yPos, 4200);
        currentToast.draw(guiGraphics);
        context.pop();
    }

    public void clear() {
        toasts.clear();
        removeCurrentToast();
        customTextureManager.clear();

        if (generalConfigData.isJadeHiding()) {
            Services.JADE.tryEnable();
        }
    }

    private void removeCurrentToast() {
        customTextureManager.removeBeingUsed(currentToast);
        currentToast = null;
    }

    private void updateCurrentToast() {
        float delta = deltaTracker.getGameTimeDeltaTicks() * generalConfigData.getAnimationSpeed();
        currentToast.update(delta);
    }

    private void setNewCurrentToast() {
        FancyAdvancementToast nextToast = toasts.pollFirst();
        if (nextToast != null) {
            currentToast = nextToast;
        }
    }

    public boolean shouldRender() {
        return !isEmpty() && !minecraft.options.hideGui;
    }

    public boolean isScreenOpened() {
        return minecraft.screen != null && !(minecraft.screen instanceof ChatScreen);
    }

    public boolean shouldRenderBehind() {
        return generalConfigData.getToastScreenBehavior() == ToastScreenBehavior.BEHIND && isScreenOpened();
    }

    public boolean isEmpty() {
        return currentToast == null;
    }
}
