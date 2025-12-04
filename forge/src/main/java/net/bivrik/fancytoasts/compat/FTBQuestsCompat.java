package net.bivrik.fancytoasts.compat;

import dev.ftb.mods.ftblibrary.icon.IconAnimation;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.ToastQuestObject;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.platform.utility.FancyToastType;
import net.bivrik.fancytoasts.platform.utility.QuestToastDisplayInfo;
import net.bivrik.fancytoasts.platform.utility.ToastDisplayInfo;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class FTBQuestsCompat {
    public static boolean isQuest(Toast toast) {
        return toast instanceof ToastQuestObject;
    }

    public static ToastDisplayInfo getDisplayInfo(Toast toast) {
        ToastQuestObject questToast = (ToastQuestObject) toast;

        Debug.info(questToast.getIcon().toString());

        var toastType = !questToast.isImportant() ? FancyToastType.TASK : FancyToastType.CHALLENGE;

        Component title = questToast.getSubtitle();
        Component description = toastType.getDisplayAnnouncement();
        Component questAnnouncement = questToast.getTitle();
        List<ItemStack> icons = new ArrayList<>(1);

        for (QuestObjectBase questObject : ClientQuestFile.INSTANCE.getAllObjects()) {
            if (questObject.getTitle().equals(title)) {
                var quest = ClientQuestFile.INSTANCE.getQuest(questObject.id);
                if (quest != null) {
                    description = quest.getSubtitle();
                }
                break;
            }
        }

        if (questToast.getIcon() instanceof IconAnimation iconAnimation) {
            for (var icon : iconAnimation.list) {
                icons.add(((ItemIcon) icon).getStack());
            }
        }

        if (questToast.getIcon() instanceof ItemIcon itemIcon) {
            icons.add(itemIcon.getStack());
        }

        return new QuestToastDisplayInfo(icons, title, description, toastType, questAnnouncement);
    }
}
