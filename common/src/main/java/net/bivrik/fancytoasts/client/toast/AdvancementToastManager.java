package net.bivrik.fancytoasts.client.toast;

import net.bivrik.fancytoasts.client.ConfigManager;
import net.bivrik.fancytoasts.client.config.AdvancementToastScreenBehavior;
import net.bivrik.fancytoasts.platform.IManager;
import net.bivrik.fancytoasts.platform.Managers;
import net.bivrik.fancytoasts.platform.utility.AdvancementToastDisplayInfo;
import net.bivrik.fancytoasts.platform.utility.GUIs;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.ChatScreen;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

public class AdvancementToastManager implements IManager {
    private final Deque<FancyAdvancementToast> ADVANCEMENT_TOASTS = new ConcurrentLinkedDeque<>();

    private Minecraft minecraft;
    private ConfigManager configManager;

    private volatile FancyAdvancementToast currentToast;
    private long startingTimeOfToast;

    @Override
    public void onMinecraftInit(Minecraft minecraft) {
        this.minecraft = minecraft;
        this.configManager = Managers.configManager();
    }

    public void addAdvancement(AdvancementToastDisplayInfo displayInfo) {
        FancyAdvancementToast fancyAdvancementToast = new FancyAdvancementToast(minecraft, displayInfo, configManager.toastConfig().getTextureId(), configManager.toastConfig().getAnimationId());
        ADVANCEMENT_TOASTS.add(fancyAdvancementToast);

        if (configManager.generalConfig().isJadeCompatEnabled()) {
            Services.JADE.tryDisable();
        }
    }

    public void addAdvancement(Advancement advancement) {
        DisplayInfo oldDisplayInfo = advancement.display().orElse(null);
        if (oldDisplayInfo == null) {
            return;
        }

        addAdvancement(new AdvancementToastDisplayInfo(oldDisplayInfo));
    }

    public void update() {
        if (currentToast != null) {
            updateCurrentToast();

            if (currentToast.isEnded()) {
                removeCurrentToast();

                if (configManager.generalConfig().isJadeCompatEnabled() && ADVANCEMENT_TOASTS.isEmpty()) {
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

        int xPos = configManager.generalConfig().getPosition().getX(currentToast.getWidth(), guiGraphics.guiWidth());

        var stack = GUIs.getStack(guiGraphics);
        GUIs.push(stack);
        GUIs.translate(stack, xPos, 20);
        currentToast.draw(guiGraphics);
        GUIs.pop(stack);
    }

    public void clear() {
        ADVANCEMENT_TOASTS.clear();
        removeCurrentToast();

        if (configManager.generalConfig().isJadeCompatEnabled()) {
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
        return configManager.generalConfig().getScreenBehavior() == AdvancementToastScreenBehavior.BEHIND;
    }
}
