package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.client.registry.KeyBindingRegistry;
import net.bivrik.fancytoasts.FancyToasts;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public abstract class OptionsMixin {
    @Mutable
    @Final
    @Shadow
    public KeyMapping[] keyMappings;

    @Inject(at = @At("HEAD"), method = "load()V")
    private void onLoad(CallbackInfo info) {
        FancyToasts.registerKeyBindings();
        this.keyMappings = KeyBindingRegistry.mergeKeys(this.keyMappings);
    }
}
