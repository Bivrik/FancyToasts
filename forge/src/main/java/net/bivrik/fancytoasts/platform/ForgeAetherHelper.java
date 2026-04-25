package net.bivrik.fancytoasts.platform;

import net.bivrik.fancytoasts.compat.AetherCompat;
import net.bivrik.fancytoasts.platform.services.IAetherHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;

public class ForgeAetherHelper implements IAetherHelper {
    @Override
    public ResourceLocation getOverrideId(Advancement advancement) {
        if (isLoaded()) {
            return AetherCompat.getOverrideId(advancement);
        }

        return IAetherHelper.super.getOverrideId(advancement);
    }
}
