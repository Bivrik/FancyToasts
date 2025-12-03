package net.bivrik.fancytoasts.platform.utility;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class QuestToastDisplayInfo extends ToastDisplayInfo {
    private final Component announcementDisplay;

    public QuestToastDisplayInfo(ItemStack icon, Component title, Component description, FancyToastType advancementType, Component announcementDisplay) {
        super(icon, title, description, advancementType);

        this.announcementDisplay = announcementDisplay;
    }

    @Override
    public Component getAnnouncement() {
        return announcementDisplay;
    }
}
