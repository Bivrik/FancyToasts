package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.core.Color;
import net.minecraft.network.chat.Component;

public enum FancyToastType {
    TASK("task", AdvancementType.TASK, Color.YELLOW, Color.WHITE),
    GOAL("goal", AdvancementType.GOAL, Color.CYAN, Color.WHITE),
    CHALLENGE("challenge", AdvancementType.CHALLENGE, Color.PURPLE, Color.CYAN);

    private final String name;
    private final AdvancementType conventionalType;
    private final Color titleColor;
    private final Color descriptionColor;
    private final Component displayName;
    private final Component displayAnnouncement;

    FancyToastType(String name, AdvancementType conventionalType, Color titleColor, Color descriptionColor) {
        this.name = name;
        this.conventionalType = conventionalType;
        this.titleColor = titleColor;
        this.descriptionColor = descriptionColor;
        this.displayName = Components.of("toast_type." + this.name);
        this.displayAnnouncement = Component.translatable("advancements.toast." + this.name);
    }

    public AdvancementType getConventionalType() {
        return conventionalType;
    }

    public String getName() {
        return name;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public Component getDisplayAnnouncement() {
        return displayAnnouncement;
    }

    public Color getTitleColor() {
        return titleColor;
    }

    public Color getDescriptionColor() {
        return descriptionColor;
    }
}
