package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.core.manager.ToastManager;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.ToastsHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(net.minecraft.client.gui.components.toasts.ToastManager.class)
public class ToastManagerMixin {
    @Inject(at = @At("HEAD"), method = "addToast", cancellable = true)
    private void onAddToast(Toast toast, CallbackInfo info) {
        ToastsHandler toastsHandler = new ToastsHandler(Managers.getConfigManager().getToastsFilteringData(), Managers.getAdvancementToastManager(), info);

        if (toast instanceof AdvancementToast advancementToast) {
            toastsHandler.handleAdvancementToasts(advancementToast);
        }
        else if (Services.FTB_QUESTS.isQuest(toast)) {
            toastsHandler.handleFTBQuestsToasts(toast);
        }
        else if (toast instanceof RecipeToast) {
            toastsHandler.handleRecipeToasts();
        }
        else if (toast instanceof SystemToast) {
            toastsHandler.handleSystemToasts();
        }
        else if (toast instanceof TutorialToast) {
            toastsHandler.handleTutorialToasts();
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
