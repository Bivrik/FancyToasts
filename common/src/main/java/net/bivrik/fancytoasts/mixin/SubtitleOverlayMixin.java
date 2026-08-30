package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.client.sound.UISoundInstance;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.WeighedSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SubtitleOverlay.class)
public class SubtitleOverlayMixin {
    @Inject(method = "onPlaySound", at = @At("HEAD"), cancellable = true)
    private void onPlayedSound(SoundInstance sound, WeighedSoundEvents accessor, float range, CallbackInfo info) {
        if (sound instanceof UISoundInstance) {
            info.cancel();
        }
    }
}
