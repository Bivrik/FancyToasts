package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.core.manager.ToastManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class HudMixin {
    @Inject(at = @At("HEAD"), method = "extractRenderState")
    private void onRender(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        ToastManager toastManager = Managers.getToastManager();
        if (toastManager == null) return;

        if (toastManager.shouldRenderBehind()) {
            toastManager.render(graphics);
        }
    }
}
