package net.bivrik.fancytoasts.mixin;

import net.bivrik.fancytoasts.client.toast.IAdvancementAccessor;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.gui.components.toasts.AdvancementToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AdvancementToast.class)
public abstract class AdvancementToastMixinAccessor implements IAdvancementAccessor {
    @Accessor("advancement")
    public abstract Advancement getAdvancement();
}
