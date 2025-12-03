package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.client.gui.IClientTextTooltipAccessor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientTextTooltip.class)
public abstract class ClientTextTooltipMixinAccessor implements IClientTextTooltipAccessor {
    @Accessor("text")
    public abstract FormattedCharSequence getText();
}
