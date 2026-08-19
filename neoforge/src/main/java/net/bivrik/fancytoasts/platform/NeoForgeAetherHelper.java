package net.bivrik.fancytoasts.platform;

import net.bivrik.fancytoasts.compat.AetherCompat;
import net.bivrik.fancytoasts.platform.services.IAetherHelper;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;

public class NeoForgeAetherHelper implements IAetherHelper {
    @Override
    public ResourceLocation getOverrideId(AdvancementHolder holder) {
        if (isLoaded()) {
            return AetherCompat.getOverrideId(holder);
        }

        return IAetherHelper.super.getOverrideId(holder);
    }
}
