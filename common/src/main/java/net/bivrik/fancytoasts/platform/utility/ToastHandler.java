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

        FancyAdvancementType type;
        switch (vanillaDisplay.getType()) {
            case GOAL -> type = FancyAdvancementType.GOAL;
            case CHALLENGE -> type = FancyAdvancementType.CHALLENGE;
            default -> type = FancyAdvancementType.TASK;
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
                vanillaDisplay.getTitle(), vanillaDisplay.getDescription(), vanillaDisplay.getType().getDisplayName(),
                type.getTitleColor(), type.getDescriptionColor(),
                type.getConventionalType());

        fancyToastManager.add(display, soundId);
    }

    public void handleFTBQuestsToast(Toast questToast, CallbackInfo info) {
        if (!filteringData.isFancyQuestToastsEnabled()) {
            return;
        }
        info.cancel();

        QuestDisplay display = (QuestDisplay) Services.FTB_QUESTS.getDisplayInfo(questToast);
        if (display == null) {
            return;
        }

        QuestType type = display.getQuestType();
        if (filteringData.isQuestTypeIgnored(type)) {
            return;
        }

        fancyToastManager.add(display, toastData.getSoundIdByQuestType(type));
    }

    public void handleQuestlogToast(Toast questlogToast, CallbackInfo info) {
        if (!filteringData.isFancyQuestlogToastsEnabled()) {
            return;
        }
        info.cancel();

        AdvancementDisplay display = Services.QUESTLOG_HELPER.getDisplay(questlogToast);
        if (display == null) {
            return;
        }

        FancyAdvancementType type;
        switch (display.getType()) {
            case GOAL -> type = FancyAdvancementType.GOAL;
            case CHALLENGE -> type = FancyAdvancementType.CHALLENGE;
            default -> type = FancyAdvancementType.TASK;
        }

        fancyToastManager.add(display, toastData.getSoundIdByType(type));
    }
}
