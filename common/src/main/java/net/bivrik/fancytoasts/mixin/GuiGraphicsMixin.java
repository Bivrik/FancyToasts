package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.client.gui.IClientTextTooltipAccessor;
import net.bivrik.fancytoasts.core.Managers;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(GuiGraphics.class)
public class GuiGraphicsMixin {
    @Redirect(
            method = "renderTooltipInternal",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;renderText(Lnet/minecraft/client/gui/Font;IILorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;)V"
            )
    )
    private void redirectRenderText(ClientTooltipComponent instance, Font font, int x, int y, Matrix4f matrix, MultiBufferSource.BufferSource bufferSource) {
        if (!Objects.requireNonNull(Managers.getToastManager()).isEmpty()) {
            if (instance instanceof ClientTextTooltip clientTextTooltip) {
                GuiGraphics guiGraphics = (GuiGraphics) (Object) this;
                FormattedCharSequence text = ((IClientTextTooltipAccessor) clientTextTooltip).getText();
                guiGraphics.drawString(font, text, x, y, -1);
                return;
            }
        }

        instance.renderText(font, x, y, matrix, bufferSource);
    }
}
