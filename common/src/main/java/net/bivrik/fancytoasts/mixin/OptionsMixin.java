package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.KeyBindingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {
    @Mutable
    @Final
    @Shadow
    public KeyMapping[] keyMappings;

    @Inject(at = @At("HEAD"), method = "load()V")
    private void onLoad(CallbackInfo info) {
        Common.registerKeyBindings();

        keyMappings = KeyBindingRegistry.getExtendedKeys(keyMappings);
    }
}
