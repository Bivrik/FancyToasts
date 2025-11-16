package net.bivrik.fancytoasts.platform.utility;

import net.minecraft.advancements.DisplayInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class AdvancementToastDisplayInfo {
    private final ItemStack icon;
    private final Component title;
    private final Component description;
    private final FancyAdvancementType advancementType;

    public AdvancementToastDisplayInfo(ItemStack icon, Component title, Component description, FancyAdvancementType advancementType) {
        this.icon = icon;
        this.title = title;
        this.description = description;
        this.advancementType = advancementType;
    }

    public AdvancementToastDisplayInfo(DisplayInfo displayInfo) {
        this(displayInfo.getIcon(), displayInfo.getTitle(), displayInfo.getDescription(), FancyAdvancementType.transferTypes(displayInfo.getType()));
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

    public FancyAdvancementType getAdvancementType() {
        return advancementType;
    }
}
