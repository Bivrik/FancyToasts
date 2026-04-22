package net.bivrik.fancytoasts.compat;

import com.aetherteam.aether.api.AetherAdvancementSoundOverrides;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class AetherCompat {
    public static ResourceLocation getOverrideId(AdvancementHolder holder) {
        ResourceLocation id = null;

        if (holder != null) {
            SoundEvent override = AetherAdvancementSoundOverrides.retrieveOverride(holder);
            if (override != null && override != SoundEvents.EMPTY) {
                id = override.getLocation();
            }
        }

        return id;
    }
}
