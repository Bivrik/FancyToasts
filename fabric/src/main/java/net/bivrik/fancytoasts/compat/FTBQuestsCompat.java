package net.bivrik.fancytoasts.compat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.IconAnimation;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.ToastQuestObject;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import dev.ftb.mods.ftbquests.registry.ModItems;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.platform.utility.*;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class FTBQuestsCompat {
    private static final Map<Long, Long> REPEATABLE_QUESTS = new HashMap<>(3);
    private static final int MINUTE = 1000 * 60;
    private static final int TIME = MINUTE * 3;

    public static boolean isQuest(Toast toast) {
        return toast instanceof ToastQuestObject;
    }

    // OH MY GOD WHY IS THERE SO MANY THINGS
    // DEPENDING ON DIFFERENT THINGS?!
    // Chapters quests tasks, and they all KINDA
    // have the same properties in the editing in-game,
    // But realistically speaking they are all saved differently,
    // oh my God it will take A WHILE to catch it all here,
    // it's such a mess D:
    public static AdvancementDisplay getDisplayInfo(Toast toast) {
        ToastQuestObject questToast = (ToastQuestObject) toast;

        Component title = questToast.getSubtitle(); // title
        Component description = Component.empty(); // description
        Component announcement = questToast.getTitle(); // announcement
        List<ItemStack> icons = new ArrayList<>(1);

        // Try to find a description for quest. Not really efficient, but I can't think of any other solution
        Quest quest = null;
        for (QuestObjectBase questObject : ClientQuestFile.INSTANCE.getAllObjects()) {
            if (questObject.getTitle().equals(title)) {
                Quest foundQuest = ClientQuestFile.INSTANCE.getQuest(questObject.id);
                if (foundQuest != null) {
                    quest = foundQuest;
                    description = foundQuest.getSubtitle();
                    //Debug.error("title " + quest.getTitle().getString() + "; subtitle " + quest.getSubtitle().getString() + "; description first " + quest.getDescription().getFirst().getString() + "; alt title " + quest.getAltTitle().getString());
                    break;
                }
            }
        }

        // Handle repeatable quests
        long currentTime = System.currentTimeMillis();
        if (quest != null) {
            Long id = quest.id;
            REPEATABLE_QUESTS.entrySet().removeIf(entry -> currentTime - entry.getValue() > TIME);

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

        // If there is custom texture just replace it with a FTBQuests' book. Or just in general other edge cases. It's better to have something than nothing, I guess
        if (icons.isEmpty()) {
            Item item = ModItems.ITEMS.getRegistrar().get(ResourceLocations.withNamespaceAndPath(Constants.Compatibilities.FTB_QUESTS_ID, "book"));
            if (item != null) {
                ItemStack icon = new ItemStack(item);
                icons.add(icon);
            }
        }

        // Try to extract quest-type key from the announcement component (falls back to TASK)
        FancyQuestType questType = FancyQuestType.TASK;
        String announcementHolder = announcement.toString();
        String key = ToastsHandler.extractKey(announcementHolder);
        if (key != null) {
            for (FancyQuestType type : FancyQuestType.values()) {
                if (key.startsWith("ftbquests." + type.getName())) {
                    questType = type;
                    break;
                }
            }
        }

        // If a QUEST toast has a subtitle, swap display lines instead of 'Quest Completed'
        /*Component announcementDisplay = questAnnouncement;
        Component titleDisplay = title;
        if (questType == FancyQuestType.QUEST && quest != null) {
            Component questSubtitle = quest.getSubtitle();
            if (questSubtitle != null && !questSubtitle.getString().isEmpty()) {
                announcementDisplay = title; // quest title becomes main line
                titleDisplay = questSubtitle; // quest subtitle becomes secondary line
            }
        }*/

        return new QuestAdvancementDisplay(
                icons, title, description, announcement,
                questType.getTitleColor(), questType.getDescriptionColor(),
                questType.getConventionalType(), questType);
    }
}
