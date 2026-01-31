package net.bivrik.fancytoasts.core.manager;

import net.bivrik.fancytoasts.client.config.data.GeneralConfigData;
import net.bivrik.fancytoasts.client.config.data.ToastConfigData;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.core.IManager;
import net.bivrik.fancytoasts.core.ITickableManager;
import net.bivrik.fancytoasts.core.event.GeneralConfigDataEvent;
import net.bivrik.fancytoasts.client.toast.FancyAdvancementToast;
import net.bivrik.fancytoasts.client.config.ToastScreenBehavior;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.core.event.ToastConfigDataEvent;
import net.bivrik.fancytoasts.platform.utility.GuiContext;
import net.bivrik.fancytoasts.platform.utility.ToastDisplayInfo;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.Util;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;
import org.joml.Vector2d;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ToastManager implements IManager {
    private final Deque<FancyAdvancementToast> toasts = new ConcurrentLinkedDeque<>();

    private Minecraft minecraft;

    private volatile FancyAdvancementToast currentToast;

    private CustomTextureManager customTextureManager;
    private GeneralConfigData generalConfigData;
    private ToastConfigData toastConfigData;

    @Override
    public void onMinecraftInit(Minecraft minecraft) {
        this.minecraft = minecraft;

        customTextureManager = Managers.getCustomTextureManager();
        ConfigManager configManager = Managers.getConfigManager();
        generalConfigData = configManager.getGeneralConfigData();
        toastConfigData = configManager.getToastConfigData();

        EventManager eventManager = Managers.getEventManager();
        eventManager.subscribeToEvent(GeneralConfigDataEvent.class, this::onGeneralConfigDataChanged);
        eventManager.subscribeToEvent(ToastConfigDataEvent.class, this::onToastConfigDataChanged);
    }

    private void onGeneralConfigDataChanged(GeneralConfigDataEvent event) {
        generalConfigData = event.generalConfigData();
    }

    private void onToastConfigDataChanged(ToastConfigDataEvent event) {
        toastConfigData = event.toastConfigData();
    }

    public void addToast(ToastDisplayInfo displayInfo) {
        if (displayInfo == null) return;

        FancyAdvancementToast fancyToast = new FancyAdvancementToast(minecraft, displayInfo, toastConfigData.getTextureId(), toastConfigData.getAnimationId());
        toasts.add(fancyToast);
        customTextureManager.addBeingUsed(toastConfigData.getTextureId(), fancyToast);

        if (generalConfigData.isJadeHiding()) {
            Services.JADE.tryDisable();
        }
    }

    public void update() {
        if (currentToast != null) {
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
        context.translate(xPos, yPos);
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
        float delta = minecraft.getDeltaTracker().getGameTimeDeltaTicks();
        currentToast.update(delta);
    }

    private void setNewCurrentToast() {
        FancyAdvancementToast nextToast = toasts.pollFirst();
        if (nextToast != null) {
            currentToast = nextToast;
        }
    }

    private boolean shouldRender() {
        return currentToast != null && !minecraft.options.hideGui;
    }

    public boolean isScreenOpened() {
        return minecraft.screen != null && !(minecraft.screen instanceof ChatScreen);
    }

    public boolean shouldRenderBehind() {
        return generalConfigData.getToastScreenBehavior() == ToastScreenBehavior.BEHIND && isScreenOpened();
    }
}
