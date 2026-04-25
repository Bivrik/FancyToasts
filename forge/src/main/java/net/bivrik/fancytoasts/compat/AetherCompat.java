package net.bivrik.fancytoasts.compat;

import com.aetherteam.aether.api.AetherAdvancementSoundOverrides;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class AetherCompat {
    public static ResourceLocation getOverrideId(Advancement advancement) {
        ResourceLocation id = null;

        if (advancement != null) {
            SoundEvent override = AetherAdvancementSoundOverrides.retrieveOverride(advancement);
            if (override != null && override != SoundEvents.EMPTY) {
                id = override.getLocation();
            }
        }

        return id;
    }
}
