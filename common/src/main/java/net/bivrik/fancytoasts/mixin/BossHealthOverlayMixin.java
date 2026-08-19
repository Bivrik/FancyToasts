package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.core.manager.ToastManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossHealthOverlay.class)
public class BossHealthOverlayMixin {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void onRender(GuiGraphics guiGraphics, CallbackInfo info) {
        ToastManager toastManager = Managers.getToastManager();
        if (toastManager == null) return;

        boolean shouldHide = Managers.getConfigManager().getGeneralConfigData().isBossBarHiding();
        if (toastManager.shouldRender() && shouldHide) {
            info.cancel();
        }
    }
}
