package net.bivrik.fancytoasts.platform;

import net.bivrik.fancytoasts.compat.FTBQuestsCompat;
import net.bivrik.fancytoasts.platform.services.IFTBQuestsHelper;
import net.bivrik.fancytoasts.platform.utility.ToastDisplayInfo;
import net.minecraft.client.gui.components.toasts.Toast;

public class ForgeFTBQuestsHelper implements IFTBQuestsHelper {
    @Override
    public boolean isQuest(Toast toast) {
        if (isLoaded()) {
            return FTBQuestsCompat.isQuest(toast);
        }

        return IFTBQuestsHelper.super.isQuest(toast);
    }

    @Override
    public ToastDisplayInfo getDisplayInfo(Toast toast) {
        if (isLoaded()) {
            var toastDisplayInfo = FTBQuestsCompat.getDisplayInfo(toast);
            if (toastDisplayInfo == null) {
                return IFTBQuestsHelper.super.getDisplayInfo(toast);
            }
            return toastDisplayInfo;
        }

        return IFTBQuestsHelper.super.getDisplayInfo(toast);
    }
}
