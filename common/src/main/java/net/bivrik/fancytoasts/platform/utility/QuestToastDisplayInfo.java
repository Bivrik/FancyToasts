package net.bivrik.fancytoasts.platform.utility;

import java.util.List;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class QuestToastDisplayInfo extends ToastDisplayInfo {
    private final Component announcementDisplay;
    private final List<ItemStack> icons;
    private final FancyQuestType questType;

    public QuestToastDisplayInfo(List<ItemStack> icons, Component title, Component description, FancyToastType advancementType, Component announcementDisplay, FancyQuestType questType) {
        super(null, title, description, advancementType);

        this.announcementDisplay = announcementDisplay;
        this.icons = icons;
        this.questType = questType;
    }

    @Override
    public Component getAnnouncement() {
        return announcementDisplay;
    }

    public FancyQuestType getQuestType() {
        return questType;
    }

    @Override
    public ItemStack getIcon() {
        if (icons.size() == 1) {
            return icons.get(0);
        } else if (!icons.isEmpty()) {
            return getOrderedIcon();
        }

        return ItemStack.EMPTY;
    }

    private ItemStack getOrderedIcon() {
        return icons.get((int) (System.currentTimeMillis() / 1000L % icons.size()));
    }
}
