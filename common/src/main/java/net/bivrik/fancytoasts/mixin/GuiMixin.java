package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.core.manager.ToastManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Gui.class)
public class GuiMixin {
    @Redirect(
            method = "update",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/toasts/ToastManager;update()V")
    )
    private void onRunTick(net.minecraft.client.gui.components.toasts.ToastManager minecraftToastManager) {
        minecraftToastManager.update();

        ToastManager toastManager = FancyToasts.getInstance().getToastManager();
        if (toastManager == null) return;

        toastManager.update();
    }

    @Redirect(
            method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/toasts/ToastManager;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V"
            )
    )
    private void onToastManagerRender(net.minecraft.client.gui.components.toasts.ToastManager minecraftToastManager, GuiGraphicsExtractor graphics) {
        minecraftToastManager.extractRenderState(graphics);

        ToastManager toastManager = FancyToasts.getInstance().getToastManager();
        if (toastManager == null) return;

        if (!toastManager.shouldRenderBehind()) {
            toastManager.render(graphics);
        }
    }
}
