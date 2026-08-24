package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.FancyToasts;
import net.bivrik.fancytoasts.core.manager.FancyToastManager;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = Gui.class, priority = 2000)
public class GuiMixin {
    @Redirect(method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Hud;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"
            )
    )
    private void beforeHudRender(Hud hud, GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        hud.extractRenderState(graphics, deltaTracker);

        FancyToastManager fancyToastManager = FancyToasts.getInstance().getToastManager();
        if (fancyToastManager == null) return;

        if (fancyToastManager.shouldRenderBehind()) {
            float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
            fancyToastManager.render(graphics, partialTick);
        }
    }

    @Redirect(method = "extractRenderState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/Hud;extractSavingIndicator(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"
            )
    )
    private void afterHudRender(Hud hud, GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        hud.extractSavingIndicator(graphics, deltaTracker);

        FancyToastManager fancyToastManager = FancyToasts.getInstance().getToastManager();
        if (fancyToastManager == null) return;

        if (!fancyToastManager.shouldRenderBehind()) {
            float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
            fancyToastManager.render(graphics, partialTick);
        }
    }
}
