package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.core.manager.ToastManager;
import net.bivrik.fancytoasts.client.toast.IAdvancementAccessor;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.ToastDisplayInfo;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(net.minecraft.client.gui.components.toasts.ToastManager.class)
public class ToastManagerMixin {
    @Inject(at = @At("HEAD"), method = "addToast", cancellable = true)
    private void onAddToast(Toast toast, CallbackInfo info) {
        if (toast instanceof AdvancementToast) {
            info.cancel();

            Advancement advancement = ((IAdvancementAccessor) toast).getAdvancementHolder().value();
            Objects.requireNonNull(Managers.getAdvancementToastManager()).addToast(advancement);
        }
        else if (Services.FTB_QUESTS.isQuest(toast)) {
            info.cancel();

            ToastDisplayInfo displayInfo = Services.FTB_QUESTS.getDisplayInfo(toast);
            Objects.requireNonNull(Managers.getAdvancementToastManager()).addToast(displayInfo);
        }
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void onRender(GuiGraphics guiGraphics, CallbackInfo info) {
        ToastManager toastManager = Managers.getAdvancementToastManager();
        if (toastManager == null) return;

        if (!toastManager.isScreenOpened() || !toastManager.isScreenBehaviourBehind()) {
            toastManager.render(guiGraphics);
        }
    }

    @Inject(at = @At("TAIL"), method = "update")
    private void onUpdate(CallbackInfo info) {
        ToastManager toastManager = Managers.getAdvancementToastManager();
        if (toastManager == null) return;

        toastManager.update();
    }

    @Inject(at = @At("HEAD"), method = "clear")
    private void onClear(CallbackInfo info) {
        Objects.requireNonNull(Managers.getAdvancementToastManager()).clear();
    }
}
