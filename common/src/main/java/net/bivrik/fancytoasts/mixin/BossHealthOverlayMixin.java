package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.core.manager.FancyToastManager;
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
        FancyToasts fancyToasts =  FancyToasts.getInstance();
        FancyToastManager fancyToastManager = fancyToasts.getFancyToastManager();
        boolean shouldHide = fancyToasts.getConfigManager().getGeneralConfigData().isBossBarHiding();
        if (fancyToastManager.shouldRender() && shouldHide) {
            info.cancel();
        }
    }
}
