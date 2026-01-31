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
        this.title = fixUnicode(title);
        this.description = fixUnicode(description);
        this.advancementType = advancementType;
    }

    public ToastDisplayInfo(DisplayInfo displayInfo) {
        this(displayInfo.getIcon(), displayInfo.getTitle(), displayInfo.getDescription(), FancyToastType.transferTypes(displayInfo.getType()));
    }

    private Component fixUnicode(Component message) {
        String temp = message.getString();
        if (temp.contains("§")) {
            for (int i = 0; i + 1 < temp.length(); i++) {
                char c = temp.toCharArray()[i + 1];

                if (c == '§') {
                    return Component.literal(temp.substring(0, i));
                }
            }
        }
        return message;
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
