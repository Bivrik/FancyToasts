package net.bivrik.fancytoasts.compat;

import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.ToastQuestObject;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import net.bivrik.fancytoasts.platform.utility.FancyToastType;
import net.bivrik.fancytoasts.platform.utility.QuestToastDisplayInfo;
import net.bivrik.fancytoasts.platform.utility.ToastDisplayInfo;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.network.chat.Component;

public class FTBQuestsCompat {
    public static boolean isQuest(Toast toast) {
        return toast instanceof ToastQuestObject;
    }

    public static ToastDisplayInfo getDisplayInfo(Toast toast) {
        ToastQuestObject questToast = (ToastQuestObject) toast;


        if (questToast.getIcon() instanceof ItemIcon itemIcon) {
            var toastType = !questToast.isImportant() ? FancyToastType.TASK : FancyToastType.CHALLENGE;

            Component title = questToast.getSubtitle();
            Component description = toastType.getDisplayAnnouncement();
            Component questAnnouncement = questToast.getTitle();

            for (QuestObjectBase questObject : ClientQuestFile.INSTANCE.getAllObjects()) {
                if (questObject.getTitle().equals(title)) {
                    var quest = ClientQuestFile.INSTANCE.getQuest(questObject.id);
                    if (quest != null) {
                        description = quest.getSubtitle();
                    }
                    break;
                }
            }

            return new QuestToastDisplayInfo(itemIcon.getStack(), title, description, toastType, questAnnouncement);
        }

        return null;
    }
}
