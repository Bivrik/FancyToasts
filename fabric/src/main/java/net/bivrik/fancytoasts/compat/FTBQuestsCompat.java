package net.bivrik.fancytoasts.compat;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.IconAnimation;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.ToastQuestObject;
import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.platform.utility.FancyToastType;
import net.bivrik.fancytoasts.platform.utility.QuestToastDisplayInfo;
import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.bivrik.fancytoasts.platform.utility.ToastDisplayInfo;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FTBQuestsCompat {
    public static boolean isQuest(Toast toast) {
        return toast instanceof ToastQuestObject;
    }

    public static ToastDisplayInfo getDisplayInfo(Toast toast) {
        ToastQuestObject questToast = (ToastQuestObject) toast;
        FancyToastType toastType = !questToast.isImportant() ? FancyToastType.TASK : FancyToastType.CHALLENGE;

        Component title = questToast.getSubtitle();
        Component description = toastType.getDisplayAnnouncement(); // fallback as a standard advancement announcement for description
        Component questAnnouncement = questToast.getTitle();
        List<ItemStack> icons = new ArrayList<>(1);

        // Try to find a description for quest. Not really efficient, but I can't think of any other solution
        for (QuestObjectBase questObject : ClientQuestFile.INSTANCE.getAllObjects()) {
            if (questObject.getTitle().equals(title)) {
                Quest quest = ClientQuestFile.INSTANCE.getQuest(questObject.id);
                if (quest != null) {
                    description = quest.getSubtitle();
                    break;
                }
            }
        }

        // Check icons and add them to the list
        Icon questIcon = questToast.getIcon();
        if (questIcon instanceof ItemIcon itemIcon) {
            icons.add(itemIcon.getStack());
        } else if (questIcon instanceof IconAnimation iconAnimation) {
            for (Icon icon : iconAnimation.list) {
                icons.add(((ItemIcon) icon).getStack());
            }
        }

        // If there is custom texture just replace it with a FTBQuests' book. Or just in general other edge cases. It's better to have something than nothing, I guess
        if (icons.isEmpty()) {
            Item item = FTBQuestsItems.ITEMS.getRegistrar().get(ResourceLocations.withNamespaceAndPath("ftbquests", "book"));
            if (item != null) {
                ItemStack icon = new ItemStack(item);
                icons.add(icon);
            }
        }

        return new QuestToastDisplayInfo(icons, title, description, toastType, questAnnouncement);
    }
}
