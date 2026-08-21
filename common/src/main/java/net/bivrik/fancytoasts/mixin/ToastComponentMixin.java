package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.core.manager.ConfigManager;
import net.bivrik.fancytoasts.core.manager.FancyToastManager;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.ToastsHandler;
import net.minecraft.client.gui.components.toasts.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ToastComponent.class, priority = 5000)
public class ToastComponentMixin {
    @Inject(at = @At("HEAD"), method = "addToast", cancellable = true)
    private void onAddToast(Toast toast, CallbackInfo info) {
        FancyToastManager fancyToastManager = FancyToasts.getInstance().getToastManager();
        if (fancyToastManager == null) return;

        ConfigManager configManager = FancyToasts.getInstance().getConfigManager();
        ToastsHandler toastsHandler = new ToastsHandler(configManager.getToastsFilteringData(), configManager.getToastConfigData(), fancyToastManager, info);

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

    @Inject(at = @At("HEAD"), method = "clear")
    private void onClear(CallbackInfo info) {
        FancyToastManager fancyToastManager = FancyToasts.getInstance().getToastManager();
        if (fancyToastManager == null) return;

        fancyToastManager.clear();
    }
}
