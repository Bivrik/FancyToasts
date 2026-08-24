package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.core.Color;
import net.bivrik.fancytoasts.core.Debug;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

public class AdvancementDisplay {
    private final ItemStackTemplate icon;
    private final Component title;
    private final Component description;
    private final Component announcement;
    private final Color titleColor;
    private final Color descriptionColor;
    private final AdvancementType type;

    /**
     * Description can be {@link Component#empty()}, but title and announcement must be always filled
     */
    public AdvancementDisplay(ItemStackTemplate icon, Component title, Component description, Component announcement, Color titleColor, Color descriptionColor, AdvancementType type) {
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
        return icon.create();
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