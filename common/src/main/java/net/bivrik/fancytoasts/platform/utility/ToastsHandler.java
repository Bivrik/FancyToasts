package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.client.config.data.ToastsFilteringData;
import net.bivrik.fancytoasts.client.toast.IAdvancementAccessor;
import net.bivrik.fancytoasts.core.manager.ToastManager;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public record ToastsHandler(ToastsFilteringData filteringData, ToastManager toastManager, CallbackInfo info) {

    public void handleAdvancementToasts(AdvancementToast advancementToast) {
        if (!filteringData.isAdvancementToastsEnabled()) {
            info.cancel();
            return;
        }

        AdvancementHolder advancementHolder = ((IAdvancementAccessor) advancementToast).getAdvancementHolder();
        DisplayInfo oldDisplayInfo = advancementHolder.value().display().orElse(null);
        if (oldDisplayInfo == null) {
            info.cancel();
            return;
        }
        ToastDisplayInfo displayInfo = new ToastDisplayInfo(oldDisplayInfo);

        if (filteringData.isTypeIgnored(displayInfo.getAdvancementType())
                || filteringData.isToastIgnored(advancementHolder.id())) {
            info.cancel();
            return;
        }

        if (filteringData.isFancyAdvancementToastsEnabled()) {
            info.cancel();

            toastManager.addToast(displayInfo);
        }
    }

    public void handleFTBQuestsToasts(Toast toast) {
        if (filteringData.isFancyQuestToastsEnabled()) {
            info.cancel();

            ToastDisplayInfo displayInfo = Services.FTB_QUESTS.getDisplayInfo(toast);
            toastManager.addToast(displayInfo);
        }
    }

    public void handleRecipeToasts() {
        if (!filteringData.isRecipeToastsEnabled()) {
            info.cancel();
        }
    }

    public void handleSystemToasts() {
        if (!filteringData.isSystemToastsEnabled()) {
            info.cancel();
        }
    }

    public void handleTutorialToasts() {
        if (!filteringData.isTutorialToastsEnabled()) {
            info.cancel();
        }
    }
}
