package net.bivrik.fancytoasts.platform;

import net.bivrik.fancytoasts.compat.QuestlogCompat;
import net.bivrik.fancytoasts.platform.services.IQuestlogHelper;
import net.bivrik.fancytoasts.platform.utility.AdvancementDisplay;
import net.minecraft.client.gui.components.toasts.Toast;

public class NeoForgeQuestlogHelper implements IQuestlogHelper {
    @Override
    public boolean isQuest(Toast toast) {
        if (isLoaded()) {
            return QuestlogCompat.isQuest(toast);
        }

        return IQuestlogHelper.super.isQuest(toast);
    }

    @Override
    public AdvancementDisplay getDisplay(Toast toast) {
        if (isLoaded()) {
            return QuestlogCompat.getDisplay(toast);
        }

        return IQuestlogHelper.super.getDisplay(toast);
    }
}
