package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.core.Color;
import net.bivrik.fancytoasts.core.Debug;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class AdvancementDisplay {
    private final ItemStack icon;
    private final Component title;
    private final Component description;
    private final Component announcement;
    private final Color titleColor;
    private final Color descriptionColor;
    private final AdvancementType type;

    /**
     * Description can be {@link Component#empty()}, but title and announcement must be always filled
     */
    public AdvancementDisplay(ItemStack icon, Component title, Component description, Component announcement, Color titleColor, Color descriptionColor, AdvancementType type) {
        this.icon = icon;
        this.title = fixUnicode(title);
        this.description = fixUnicode(description);
        this.announcement = announcement;
        this.titleColor = titleColor;
        this.descriptionColor = descriptionColor;
        this.type = type;
    }

    private Component fixUnicode(Component message) {
        String temp = message.getString();
        Debug.error("BEFORE {}", temp);
        if (temp.contains("§")) {
            for (int i = 0; i + 1 < temp.length(); i++) {
                char c = temp.toCharArray()[i + 1];

                if (c == '§') {
                    Debug.error("AFTER {}", temp.substring(0, i));
                    return Component.literal(temp.substring(0, i));
                }
            }
        }
        Debug.error("Did not change");
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

    public Component getAnnouncement() {
        return announcement;
    }

    public Color getTitleColor() {
        return titleColor;
    }

    public Color getDescriptionColor() {
        return descriptionColor;
    }

    public final AdvancementType getType() {
        return type;
    }
}