package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.core.Common;
import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.core.manager.ToastManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(at = @At("TAIL"), method = "<init>")
    private void onInit(CallbackInfo info) {
        Common.onMinecraftInit(Minecraft.getInstance());
    }

    @Inject(at = @At("RETURN"), method = "tick")
    private void onTick(CallbackInfo info) {
        Common.onTick();
    }

    @Redirect(
            method = "runTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/toasts/ToastManager;update()V")
    )
    private void onRunTick(net.minecraft.client.gui.components.toasts.ToastManager minecraftToastManager) {
        minecraftToastManager.update();

        ToastManager toastManager = Managers.getToastManager();
        if (toastManager == null) return;

        toastManager.update();
    }
}
