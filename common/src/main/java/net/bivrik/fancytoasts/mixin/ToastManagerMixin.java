package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.toast.IAdvancementAccessor;
import net.bivrik.fancytoasts.client.toast.FancyAdvancementToast;
import net.bivrik.fancytoasts.client.renderer.GUIHelper;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.Util;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayDeque;
import java.util.Deque;

@Mixin(ToastManager.class)
public class ToastManagerMixin {
    @Shadow @Final
    Minecraft minecraft;

    @Unique
    private static final Deque<FancyAdvancementToast> ADVANCEMENT_TOASTS = new ArrayDeque<>();

    @Unique
    private FancyAdvancementToast fancyToasts$current;
    @Unique
    private long fancyToasts$startTime;

    @Inject(at = @At("HEAD"), method = "addToast", cancellable = true)
    private void onAddToast(Toast toast, CallbackInfo info) {
        if (toast instanceof AdvancementToast) {
            info.cancel();
            Advancement advancement = ((IAdvancementAccessor) toast).getAdvancementHolder().value();
            FancyAdvancementToast fancyAdvancement = new FancyAdvancementToast(advancement, Common.CONFIG.getTextureId(), Common.CONFIG.getAnimationId());
            ADVANCEMENT_TOASTS.add(fancyAdvancement);

            //if (Common.CONFIG.getJadeCompatibility()) {
                Services.PLATFORM.tryDisableJade();
            //}
        }
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void onRender(GuiGraphics graphics, CallbackInfo info) {
        if (minecraft.options.hideGui) {
            return;
        }

        if (fancyToasts$current == null) {
            if (!ADVANCEMENT_TOASTS.isEmpty()) {
                fancyToasts$setCurrentAdvancement();
            }
            else {
                //
                Services.PLATFORM.tryEnableJade();
            }

            return;
        }
        else {
            if (!fancyToasts$updateCurrentAdvancement()) {
                return;
            }
        }

        int xPos = (graphics.guiWidth() / 2) - fancyToasts$current.getWidth() / 2;
        var matrix = GUIHelper.get(graphics);

        GUIHelper.push(matrix);
        GUIHelper.translate(matrix, xPos, 20);
        fancyToasts$current.draw(graphics, minecraft);
        GUIHelper.pop(matrix);
    }

    @Unique
    private void fancyToasts$setCurrentAdvancement() {
        fancyToasts$current = ADVANCEMENT_TOASTS.getFirst();
        ADVANCEMENT_TOASTS.removeFirst();

        fancyToasts$current.playSounds(minecraft.getSoundManager());
        fancyToasts$startTime = Util.getMillis();
    }

    @Unique
    private boolean fancyToasts$updateCurrentAdvancement() {
        if (fancyToasts$current.isEnded()) {
            fancyToasts$current = null;
            return false;
        }

        long time = Util.getMillis() - fancyToasts$startTime;
        fancyToasts$current.update(time);

        return true;
    }

    @Inject(at = @At("HEAD"), method = "clear")
    private void onClear(CallbackInfo info) {
        ADVANCEMENT_TOASTS.clear();
        fancyToasts$current = null;
    }
}
