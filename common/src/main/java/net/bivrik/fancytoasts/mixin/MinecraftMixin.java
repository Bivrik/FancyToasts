package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.client.config.ConfigTextureManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(at = @At("RETURN"), method = "<init>")
    private void onInit(CallbackInfo info)
    {
        var id = Common.CONFIG.getTextureId();
        if (id.toString().contains("config")) {
            ConfigTextureManager.registerInMinecraft(id);
        }
    }
}
