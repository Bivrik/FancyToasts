package net.bivrik.fancytoasts.client.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.config.AdvancementToastScreenBehavior;
import net.bivrik.fancytoasts.client.renderer.GUIHelper;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import java.util.ArrayDeque;
import java.util.Deque;

public class AdvancementToastManager {
    private static final Deque<FancyAdvancementToast> ADVANCEMENT_TOASTS = new ArrayDeque<>();

    private final Minecraft minecraft;

    private FancyAdvancementToast current;
    private long startTime;

    public AdvancementToastManager(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void addAdvancement(Advancement advancement) {
        var toastConfig = Common.getConfigManager().getToastConfig();
        FancyAdvancementToast fancyAdvancement = new FancyAdvancementToast(advancement, toastConfig.getTextureId(), toastConfig.getAnimationId());
        ADVANCEMENT_TOASTS.add(fancyAdvancement);

        if (Common.getConfigManager().getGeneralConfig().isJadeCompatEnabled()) {
            Services.PLATFORM.tryDisableJade();
        }
    }

    public void update() {
        if (current != null) {
            long time = Util.getMillis() - startTime;
            current.update(time);

            if (current.isEnded()) {
                current = null;

                if (!Services.PLATFORM.isJadeEnabled() && Common.getConfigManager().getGeneralConfig().isJadeCompatEnabled()) {
                    Services.PLATFORM.tryEnableJade();
                }
            }

            return;
        }

        if (!ADVANCEMENT_TOASTS.isEmpty()) {
            current = ADVANCEMENT_TOASTS.getFirst();
            current.trySetSoundManager(minecraft.getSoundManager());
            ADVANCEMENT_TOASTS.removeFirst();

            startTime = Util.getMillis();
        }
    }

    public boolean isScreenOpened() {
        return minecraft.screen != null;
    }

    public boolean isRenderUnder() {
        return Common.getConfigManager().getGeneralConfig().getScreenBehavior() == AdvancementToastScreenBehavior.BEHIND;
    }

    public void render(GuiGraphics graphics) {
        if (current == null || minecraft.options.hideGui) {
            return;
        }

        int xPos = Common.getConfigManager().getGeneralConfig().getPosition().getX(current.getWidth(), graphics.guiWidth());

        var matrix = GUIHelper.get(graphics);
        GUIHelper.push(matrix);
        GUIHelper.translate(matrix, xPos, 20);
        current.draw(graphics, minecraft);
        GUIHelper.pop(matrix);
    }

    public void clear() {
        ADVANCEMENT_TOASTS.clear();
        current = null;

        if (!Services.PLATFORM.isJadeEnabled() && Common.getConfigManager().getGeneralConfig().isJadeCompatEnabled()) {
            Services.PLATFORM.tryEnableJade();
        }
    }
}
