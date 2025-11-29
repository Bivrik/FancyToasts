package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.core.Managers;
import net.bivrik.fancytoasts.core.manager.ToastManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/toasts/ToastManager;render(Lnet/minecraft/client/gui/GuiGraphics;)V"
            )
    )
    private void onToastManagerRender(net.minecraft.client.gui.components.toasts.ToastManager minecraftToastManager, GuiGraphics guiGraphics) {
        minecraftToastManager.render(guiGraphics);

        ToastManager toastManager = Managers.getToastManager();
        if (toastManager == null) return;

        if (!toastManager.shouldRenderBehind()) {
            toastManager.render(guiGraphics);
        }
    }
}
