package net.bivrik.fancytoasts.mixin;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.core.manager.FancyToastManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiGraphics;flush()V"
            ),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void onGuiRendered(float partialTick, long nanoTime, boolean renderLevel, CallbackInfo info,
                               int mouseX, int MouseY, Window window,
                               Matrix4f matrix, PoseStack stack, GuiGraphics graphics) {
        FancyToastManager fancyToastManager = FancyToasts.getInstance().getToastManager();
        if (fancyToastManager == null) return;

        if (!fancyToastManager.shouldRenderBehind()) {
            fancyToastManager.render(graphics, partialTick);
        }
    }


    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;F)V"
            )
    )
    private void onGuiRender(Gui gui, GuiGraphics graphics, float partialTick) {
        gui.render(graphics, partialTick);

        FancyToastManager fancyToastManager = FancyToasts.getInstance().getToastManager();
        if (fancyToastManager == null) return;

        if (fancyToastManager.shouldRenderBehind()) {
            fancyToastManager.render(graphics, partialTick);
        }
    }
}
