package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.core.manager.ConfigManager;
import net.bivrik.fancytoasts.core.manager.FancyToastManager;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.ToastHandler;
import net.minecraft.client.gui.components.toasts.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ToastComponent.class, priority = 2000)
public class ToastComponentMixin {
    @Inject(at = @At("HEAD"), method = "addToast", cancellable = true)
    private void onAddToast(Toast toast, CallbackInfo info) {
        FancyToasts fancyToasts = FancyToasts.getInstance();
        if (!fancyToasts.isMinecraftFinishedLoading()) {
            return;
        }

        ConfigManager configManager = fancyToasts.getConfigManager();
        FancyToastManager toastManager = fancyToasts.getFancyToastManager();

        ToastHandler toastHandler = new ToastHandler(configManager.getToastsFilteringData(), configManager.getToastConfigData(), toastManager);

        if (toast instanceof AdvancementToast advancementToast) {
            toastHandler.handleAdvancementToast(advancementToast, info);
        } else if (Services.FTB_QUESTS.isQuest(toast)) {
            toastHandler.handleFTBQuestsToast(toast, info);
        } else if (Services.QUESTLOG_HELPER.isQuest(toast)) {
            toastHandler.handleQuestlogToast(toast, info);
        }
    }

    @Inject(at = @At("HEAD"), method = "clear")
    private void onClear(CallbackInfo info) {
        FancyToasts fancyToasts = FancyToasts.getInstance();
        if (!fancyToasts.isMinecraftFinishedLoading()) {
            return;
        }

        FancyToastManager toastManager = fancyToasts.getFancyToastManager();
        toastManager.clear();
    }
}
