package net.bivrik.fancytoasts.platform.services;

import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.AdvancementDisplay;
import net.minecraft.client.gui.components.toasts.Toast;

public interface IQuestlogHelper {
    /**
     * Checks the instance of a toast, comparing it to QuestCompletedToast
     * @param toast abstract interface of every toast
     * @return true if quest is an instance of toast type, false otherwise
     */
    default boolean isQuest(Toast toast) {
        return false;
    }

    /**
     * Gets a display info for toast from a QuestCompletedToast. Not entirely accurate, but does the best job it can
     * @param toast abstract interface of every toast
     * @return {@link AdvancementDisplay} gathered from QuestCompletedToast
     */
    default AdvancementDisplay getDisplay(Toast toast) {
        return null;
    }

    default boolean isLoaded() {
        return Services.PLATFORM.isModLoaded(Constants.Compatibilities.QUESTLOG_ID);
    }
}
