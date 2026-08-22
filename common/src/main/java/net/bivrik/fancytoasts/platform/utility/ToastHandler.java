package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.client.config.data.ToastConfigData;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.bivrik.fancytoasts.client.config.data.ToastsFilteringData;
import net.bivrik.fancytoasts.client.toast.IAdvancementAccessor;
import net.bivrik.fancytoasts.core.manager.FancyToastManager;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import net.minecraft.client.gui.components.toasts.Toast;

public record ToastHandler(ToastsFilteringData filteringData, ToastConfigData toastData, FancyToastManager fancyToastManager) {
    public void handleAdvancementToast(AdvancementToast advancementToast, CallbackInfo info) {
        if (!filteringData.isFancyAdvancementToastsEnabled()) {
            return;
        }
        info.cancel();

        AdvancementHolder advancementHolder = ((IAdvancementAccessor) advancementToast).getAdvancementHolder();
        DisplayInfo vanillaDisplay = advancementHolder.value().display().orElse(null);
        if (vanillaDisplay == null) {
            return;
        }

        FancyToastType type;
        switch (vanillaDisplay.getType()) {
            case GOAL -> type = FancyToastType.GOAL;
            case CHALLENGE -> type = FancyToastType.CHALLENGE;
            default -> type = FancyToastType.TASK;
        }

        if (filteringData.isTypeIgnored(type) || filteringData.isToastIgnored(advancementHolder.id())) {
            return;
        }

        ResourceLocation soundId = toastData.getSoundIdByType(type);
        if (Services.AETHER_HELPER.isLoaded()) {
            ResourceLocation aetherSoundOverrideId = Services.AETHER_HELPER.getOverrideId(advancementHolder);
            if (aetherSoundOverrideId != null) {
                soundId = aetherSoundOverrideId;
            }
        }

        AdvancementDisplay display = new AdvancementDisplay(
                vanillaDisplay.getIcon(),
                vanillaDisplay.getTitle(), vanillaDisplay.getDescription(), type.getDisplayAnnouncement(),
                type.getTitleColor(), type.getDescriptionColor(),
                type.getConventionalType());

        fancyToastManager.add(display, soundId);
    }

    public void handleFTBQuestsToast(Toast questToast, CallbackInfo info) {
        if (!filteringData.isFancyQuestToastsEnabled()) {
            return;
        }
        info.cancel();

        QuestAdvancementDisplay display = (QuestAdvancementDisplay) Services.FTB_QUESTS.getDisplayInfo(questToast);
        if (filteringData.isQuestTypeIgnored(display.getQuestType())) {
            return;
        }

        fancyToastManager.add(display, toastData.getSoundIdByQuestType(display.getQuestType()));
    }
}
