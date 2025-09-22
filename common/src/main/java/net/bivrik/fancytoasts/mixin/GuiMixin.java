package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.Common;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(at = @At("HEAD"), method = "render")
    private void onRender(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo info) {
        var toastManager = Common.getAdvancementToastManager();

        if (toastManager.isScreenOpened() && toastManager.isRenderUnder()) {
            Common.getAdvancementToastManager().render(guiGraphics);
        }
    }
}
