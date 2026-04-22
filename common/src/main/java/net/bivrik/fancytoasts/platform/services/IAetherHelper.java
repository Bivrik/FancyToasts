package net.bivrik.fancytoasts.platform.services;

import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;

public interface IAetherHelper {
    default ResourceLocation getOverrideId(AdvancementHolder holder) {
        return null;
    }

    default boolean isLoaded() {
        return Services.PLATFORM.isModLoaded(Constants.Compatibilities.AETHER_ID);
    }
}
