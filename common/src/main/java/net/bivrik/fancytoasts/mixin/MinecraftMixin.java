package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.Common;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(at = @At("TAIL"), method = "<init>")
    private void onInit(CallbackInfo info) {
        Common.onMinecraftInitialization(Minecraft.getInstance());
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void onTick(CallbackInfo info) {
        Common.onTick();
    }
}
