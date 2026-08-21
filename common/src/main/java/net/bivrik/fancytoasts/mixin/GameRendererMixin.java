package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.core.manager.ToastManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/toasts/ToastComponent;render(Lnet/minecraft/client/gui/GuiGraphics;)V"
            )
    )
    private void onToastManagerRender(ToastComponent toastComponent, GuiGraphics guiGraphics) {
        toastComponent.render(guiGraphics);

        ToastManager toastManager = FancyToasts.getInstance().getToastManager();
        if (toastManager == null) return;

        toastManager.update();

        if (!toastManager.shouldRenderBehind()) {
            toastManager.render(guiGraphics);
        }
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;F)V"
            )
    )
    private void onGuiRender(Gui instance, GuiGraphics guiGraphics, float partialTicks) {
        instance.render(guiGraphics, partialTicks);

        ToastManager toastManager = FancyToasts.getInstance().getToastManager();
        if (toastManager == null) return;

        if (!toastManager.shouldRenderBehind()) {
            toastManager.render(guiGraphics);
        }
    }
}
