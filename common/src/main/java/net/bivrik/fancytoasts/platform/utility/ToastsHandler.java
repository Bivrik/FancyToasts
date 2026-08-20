package net.bivrik.fancytoasts.platform.utility;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.bivrik.fancytoasts.client.config.data.ToastsFilteringData;
import net.bivrik.fancytoasts.client.toast.IAdvancementAccessor;
import net.bivrik.fancytoasts.core.manager.ToastManager;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;

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

            toastManager.addToast(displayInfo, advancementHolder);
        }
    }

    public void handleFTBQuestsToasts(Toast toast) {
        if (!filteringData.isFancyQuestToastsEnabled()) {
            return;
        }

        ToastDisplayInfo displayInfo = Services.FTB_QUESTS.getDisplayInfo(toast);
        if (displayInfo == null) return;

        FancyQuestType questType = null;
        if (displayInfo instanceof QuestToastDisplayInfo qdi) {
            questType = qdi.getQuestType();
        } else {
            String announcement = displayInfo.getAnnouncement().toString();
            if (!announcement.startsWith("translation")) return;
            String key = extractKey(announcement);
            if (key == null) return;
            for (FancyQuestType fq : FancyQuestType.values()) {
                if (key.startsWith("ftbquests." + fq.getName())) {
                    questType = fq;
                    break;
                }
            }
        }

        if (questType != null && filteringData.isQuestTypeIgnored(questType)) return; // If ignored, do nothing

        info.cancel();
        toastManager.addToast(displayInfo, null);
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

    public static String extractKey(String s)
    {
        String marker = "key='";
        int startIndex = s.indexOf(marker);
        if (startIndex == -1) return null;

        startIndex += marker.length();
        int endIndex = s.indexOf("\'", startIndex);
        if (endIndex == -1) return null;

        return s.substring(startIndex, endIndex);
    }
}
