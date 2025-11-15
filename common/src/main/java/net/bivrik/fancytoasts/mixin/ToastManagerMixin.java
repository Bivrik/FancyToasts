package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.toast.AdvancementToastManager;
import net.bivrik.fancytoasts.client.toast.IAdvancementAccessor;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public class ToastManagerMixin {
    @Inject(at = @At("HEAD"), method = "addToast", cancellable = true)
    private void onAddToast(Toast toast, CallbackInfo info) {
        if (toast instanceof AdvancementToast) {
            info.cancel();

            Advancement advancement = ((IAdvancementAccessor) toast).getAdvancementHolder().value();
            Common.getAdvancementToastManager().addAdvancement(advancement);
        }
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void onRender(GuiGraphics guiGraphics, CallbackInfo info) {
        AdvancementToastManager toastManager = Common.getAdvancementToastManager();
        if (toastManager == null) {
            return;
        }

        if (!toastManager.isScreenOpened() || !toastManager.isScreenBehaviourUnder()) {
            toastManager.render(guiGraphics);
        }
    }

    @Inject(at = @At("TAIL"), method = "update")
    private void onUpdate(CallbackInfo info) {
        AdvancementToastManager toastManager = Common.getAdvancementToastManager();
        if (toastManager == null) {
            return;
        }

        toastManager.update();
    }

    @Inject(at = @At("HEAD"), method = "clear")
    private void onClear(CallbackInfo info) {
        Common.getAdvancementToastManager().clear();
    }
}
