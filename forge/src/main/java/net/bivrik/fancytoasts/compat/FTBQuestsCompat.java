package net.bivrik.fancytoasts.compat;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.IconAnimation;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.ToastQuestObject;
import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.platform.utility.*;
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
    private static final ResourceLocation BOOK_ID = ResourceLocations.withNamespaceAndPath(Constants.Compatibilities.FTB_QUESTS_ID, "book");

    private static final Map<Long, Long> REPEATABLE_QUESTS = new HashMap<>();
    private static final int DELAY = 1000 * 60 * 3;

    public static boolean isQuest(Toast toast) {
        return toast instanceof ToastQuestObject;
    }

    public static AdvancementDisplay getDisplayInfo(Toast toast) {
        ToastQuestObject questToast = (ToastQuestObject) toast;

        Component title = questToast.getSubtitle(); // represents title (quest name or icon name)
        Component description = Component.empty(); // subtitle
        Component announcement = questToast.getTitle(); // announcement
        List<ItemStack> icons = new ArrayList<>(1);

        // Try to find quest and subtitle of it. Not really efficient, but I can't think of any other solution
        Quest quest = null;
        for (QuestObjectBase questObject : ClientQuestFile.INSTANCE.getAllObjects()) {
            if (questObject.getTitle().equals(title)) {
                Quest foundQuest = ClientQuestFile.INSTANCE.getQuest(questObject.id);
                if (foundQuest != null) {
                    quest = foundQuest;
                    description = foundQuest.getSubtitle();
                    break;
                }
            }
        }
        // Now we have 100% announcement, 100% title, and 50% description for display

        // Handle repeatable quests
        long currentTime = System.currentTimeMillis();
        if (quest != null) {
            Long id = quest.id;
            REPEATABLE_QUESTS.entrySet().removeIf(entry -> currentTime - entry.getValue() > DELAY);

            if (quest.canBeRepeated()) {
                if (REPEATABLE_QUESTS.containsKey(id)) {
                    return null;
                } else {
                    REPEATABLE_QUESTS.put(id, currentTime);
                }
            }
        }

        // Check icons and add them to the list
        Icon questIcon = questToast.getIcon();
        if (questIcon instanceof ItemIcon itemIcon) {
            icons.add(itemIcon.getStack());
        } else if (questIcon instanceof IconAnimation iconAnimation) {
            for (Icon icon : iconAnimation.list) {
                if (icon instanceof ItemIcon itemIcon) {
                    icons.add(itemIcon.getStack());
                }
            }
        }

        // If there were only custom texture just replace it with a FTBQuests book.
        // Or just in general any other edge cases. It's better
        // to have at least something rather than nothing, I guess
        if (icons.isEmpty()) {
            Item item = FTBQuestsItems.ITEMS.getRegistrar().get(BOOK_ID);
            if (item != null) {
                icons.add(new ItemStack(item));
            }
        }

        // Try to extract quest type key from the announcement component
        QuestType type = QuestType.TASK;
        String announcementKey = Components.extractKey(announcement);
        if (announcementKey != null) {
            for (QuestType questType : QuestType.values()) {
                if (announcementKey.startsWith(Constants.Compatibilities.FTB_QUESTS_ID + "." + questType.getName())) {
                    type = questType;
                    break;
                }
            }
        }

        return new QuestDisplay(
                icons, title, description, announcement,
                type.getTitleColor(), type.getDescriptionColor(),
                type.getConventionalType(), type);
    }
}
