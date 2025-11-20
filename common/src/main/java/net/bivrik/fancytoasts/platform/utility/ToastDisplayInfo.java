package net.bivrik.fancytoasts.platform.utility;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ToastDisplayInfo {
    private final ItemStack icon;
    private final Component title;
    private final Component description;
    private final FancyToastType advancementType;

    public ToastDisplayInfo(ItemStack icon, Component title, Component description, FancyToastType advancementType) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.advancementType = advancementType;
    }

    public ToastDisplayInfo(DisplayInfo displayInfo) {
        this(displayInfo.getIcon(), displayInfo.getTitle(), displayInfo.getDescription(), FancyToastType.transferTypes(displayInfo.getType()));
    }

    public ItemStack getIcon() {
        return icon;
    }

    public Component getTitle() {
        return title;
    }

    public Component getDescription() {
        return description;
    }

    public Component getAdvancementsAnnouncement() {
        return advancementType.getDisplayAnnouncement();
    }

    public FancyToastType getAdvancementType() {
        return advancementType;
    }
}
