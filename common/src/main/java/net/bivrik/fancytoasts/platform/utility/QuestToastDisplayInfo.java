package net.bivrik.fancytoasts.platform.utility;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class QuestToastDisplayInfo extends ToastDisplayInfo {
    private final Component announcementDisplay;
    private final List<ItemStack> icons;

    public QuestToastDisplayInfo(List<ItemStack> icons, Component title, Component description, FancyToastType advancementType, Component announcementDisplay) {
        super(null, title, description, advancementType);

        this.announcementDisplay = announcementDisplay;
        this.icons = icons;
    }

    @Override
    public Component getAnnouncement() {
        return announcementDisplay;
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
        int currentIdForIcon = (int) (System.currentTimeMillis() / 1000L % icons.size());
        return icons.get(currentIdForIcon);
    }
}
