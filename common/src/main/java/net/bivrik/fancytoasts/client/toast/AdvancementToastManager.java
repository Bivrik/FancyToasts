package net.bivrik.fancytoasts.client.toast;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.config.AdvancementToastScreenBehavior;
import net.bivrik.fancytoasts.client.config.GeneralConfigData;
import net.bivrik.fancytoasts.platform.utility.GUIs;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class AdvancementToastManager {
    private final Deque<FancyAdvancementToast> ADVANCEMENT_TOASTS = new ConcurrentLinkedDeque<>();
    private final Minecraft minecraft;

    private volatile FancyAdvancementToast currentToast;
    private long startingTimeOfToast;

    public AdvancementToastManager(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    private GeneralConfigData getGeneralConfig() {
        return Common.getConfigManager().getGeneralConfig();
    }

    public void addAdvancement(Advancement advancement) {
        var toastConfig = Common.getConfigManager().getToastConfig();

        FancyAdvancementToast fancyAdvancementToast = new FancyAdvancementToast(advancement, toastConfig.getTextureId(), toastConfig.getAnimationId());
        ADVANCEMENT_TOASTS.add(fancyAdvancementToast);

        if (getGeneralConfig().isJadeCompatEnabled()) {
            Services.JADE.tryDisable();
        }
    }

    public void update() {
        if (currentToast != null) {
            updateCurrentToast();

            if (currentToast.isEnded()) {
                removeCurrentToast();

                if (getGeneralConfig().isJadeCompatEnabled() && ADVANCEMENT_TOASTS.isEmpty()) {
                    Services.JADE.tryEnable();
                }
            }

            return;
        }

        if (!ADVANCEMENT_TOASTS.isEmpty()) {
            setNewCurrentToast();
        }
    }

    public void render(GuiGraphics guiGraphics) {
        if (!shouldRender()) {
            return;
        }

        int xPos = getGeneralConfig().getPosition().getX(currentToast.getWidth(), guiGraphics.guiWidth());

        var stack = GUIs.getStack(guiGraphics);
        GUIs.push(stack);
        GUIs.translate(stack, xPos, 20);
        currentToast.draw(guiGraphics, minecraft);
        GUIs.pop(stack);
    }

    public void clear() {
        ADVANCEMENT_TOASTS.clear();
        removeCurrentToast();

        if (getGeneralConfig().isJadeCompatEnabled()) {
            Services.JADE.tryEnable();
        }
    }

    private void removeCurrentToast() {
        currentToast = null;
    }

    private void updateCurrentToast() {
        long time = Util.getMillis() - startingTimeOfToast;
        currentToast.update(time);
    }

    private void setNewCurrentToast() {
        FancyAdvancementToast nextToast = ADVANCEMENT_TOASTS.pollFirst();
        if (nextToast != null) {
            currentToast = nextToast;
            currentToast.trySetSoundManager(minecraft.getSoundManager()); // change?

            startingTimeOfToast = Util.getMillis();
        }
    }

    private boolean shouldRender() {
        return currentToast != null && !minecraft.options.hideGui;
    }

    public boolean isScreenOpened() {
        return minecraft.screen != null && !(minecraft.screen instanceof ChatScreen);
    }

    public boolean isScreenBehaviourUnder() {
        return getGeneralConfig().getScreenBehavior() == AdvancementToastScreenBehavior.BEHIND;
    }
}
