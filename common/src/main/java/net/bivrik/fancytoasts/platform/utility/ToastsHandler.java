package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.client.config.data.ToastConfigData;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.bivrik.fancytoasts.client.config.data.ToastsFilteringData;
import net.bivrik.fancytoasts.client.toast.IAdvancementAccessor;
import net.bivrik.fancytoasts.core.manager.ToastManager;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;

public record ToastsHandler(ToastsFilteringData filteringData, ToastConfigData toastData, ToastManager toastManager, CallbackInfo info) {

    public void handleAdvancementToasts(AdvancementToast advancementToast) {
        if (!filteringData.isAdvancementToastsEnabled()) {
            info.cancel();
            return;
        }

        AdvancementHolder advancementHolder = ((IAdvancementAccessor) advancementToast).getAdvancementHolder();
        DisplayInfo vanillaDisplayInfo = advancementHolder.value().display().orElse(null);
        if (vanillaDisplayInfo == null) {
            info.cancel();
            return;
        }

        FancyToastType toastType;
        switch (vanillaDisplayInfo.getType()) {
            case GOAL -> toastType = FancyToastType.GOAL;
            case CHALLENGE -> toastType = FancyToastType.CHALLENGE;
            default -> toastType = FancyToastType.TASK;
        }

        if (filteringData.isTypeIgnored(toastType) || filteringData.isToastIgnored(advancementHolder.id())) {
            info.cancel();
            return;
        }

        if (filteringData.isFancyAdvancementToastsEnabled()) {
            info.cancel();

            ResourceLocation soundId = toastData.getSoundIdByType(toastType);
            if (Services.AETHER_HELPER.isLoaded()) {
                ResourceLocation aetherSoundOverrideId = Services.AETHER_HELPER.getOverrideId(advancementHolder);
                if (aetherSoundOverrideId != null) {
                    soundId = aetherSoundOverrideId;
                }
            }

            AdvancementDisplay display = new AdvancementDisplay(
                    vanillaDisplayInfo.getIcon(),
                    vanillaDisplayInfo.getTitle(), vanillaDisplayInfo.getDescription(), toastType.getDisplayAnnouncement(),
                    toastType.getTitleColor(), toastType.getDescriptionColor(),
                    toastType.getConventionalType());

            toastManager.addAdvancement(display, soundId);
        }
    }

    public void handleFTBQuestsToasts(Toast toast) {
        if (!filteringData.isFancyQuestToastsEnabled()) {
            return;
        }

        QuestAdvancementDisplay display = (QuestAdvancementDisplay) Services.FTB_QUESTS.getDisplayInfo(toast);
        if (display == null) {
            return;
        }

        if (filteringData.isQuestTypeIgnored(display.getQuestType())) {
            return;
        }

        info.cancel();
        toastManager.addAdvancement(display, toastData.getSoundIdByQuestType(display.getQuestType()));
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
