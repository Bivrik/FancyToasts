package net.bivrik.fancytoasts.platform.services;

import net.bivrik.fancytoasts.platform.utility.AdvancementToastDisplayInfo;
import net.minecraft.client.gui.components.toasts.Toast;

public interface IFTBQuestsHelper {
    /**
     * Checks the instance of a toast, comparing it to a FTB Quests QuestObjectToast
     * @param toast abstract interface of every toast
     * @return true if quest is an instance of QuestObjectToast, false otherwise
     */
    default boolean isQuest(Toast toast) {
        return false;
    }

    /**
     * Gets a display info for toast from a QuestObjectToast. Not entirely accurate, but does the best job it can
     * @param toast abstract interface of every toast
     * @return {@link AdvancementToastDisplayInfo} gathered from a QuestObjectToast
     */
    default AdvancementToastDisplayInfo getDisplayInfo(Toast toast) {
        return null;
    }
}
