package net.bivrik.fancytoasts.mixin;

/*import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftbquests.client.gui.CustomToast;
import net.bivrik.fancytoasts.Common;
import net.bivrik.fancytoasts.platform.utility.AdvancementToastDisplayInfo;
import net.bivrik.fancytoasts.platform.utility.FancyAdvancementType;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public class FabricToastManagerMixin {
    @Inject(at = @At("HEAD"), method = "addToast", cancellable = true)
    private void onAddAdvancement(Toast toast, CallbackInfo info) {
        if (toast instanceof CustomToast customToast) {
            info.cancel();

            if (customToast.getIcon() instanceof ItemIcon itemIcon) {
                var displayInfo = new AdvancementToastDisplayInfo(itemIcon.getStack(), customToast.getTitle(), customToast.getSubtitle(), FancyAdvancementType.TASK);
                Common.getAdvancementToastManager().addAdvancement(displayInfo);
            }
        }
    }
}*/
