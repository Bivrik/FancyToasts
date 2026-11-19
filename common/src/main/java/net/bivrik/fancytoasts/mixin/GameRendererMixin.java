package net.bivrik.fancytoasts.mixin;

import com.mojang.blaze3d.platform.Window;
import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.core.manager.FancyToastManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void beforeGuiRender(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo info,
                                 boolean isGameFinishedLoading, int mouseX, int mouseY, Window window,
                                 Matrix4f matrix, Matrix4fStack stack, GuiGraphics graphics) {
        FancyToasts fancyToasts = FancyToasts.getInstance();

        if (!fancyToasts.isMinecraftFinishedLoading()) {
            return;
        }

        FancyToastManager toastManager = fancyToasts.getFancyToastManager();
        if (toastManager.shouldRenderBehind()) {
            toastManager.update();
            toastManager.render(graphics);
        }
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;flush()V"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void afterGuiRender(DeltaTracker deltaTracker, boolean renderLevel, CallbackInfo info,
                                boolean isGameFinishedLoading, int mouseX, int mouseY, Window window,
                                Matrix4f matrix, Matrix4fStack stack, GuiGraphics graphics) {
        FancyToasts fancyToasts = FancyToasts.getInstance();

        if (!fancyToasts.isMinecraftFinishedLoading()) {
            return;
        }

        FancyToastManager toastManager = fancyToasts.getFancyToastManager();
        if (!toastManager.shouldRenderBehind()) {
            toastManager.update();
            toastManager.render(graphics);
        }
    }
}
