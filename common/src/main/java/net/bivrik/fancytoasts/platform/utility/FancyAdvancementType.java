package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.core.Color;
import net.minecraft.network.chat.Component;

public enum FancyAdvancementType {
    TASK("task", AdvancementType.TASK, Color.YELLOW, Color.WHITE),
    GOAL("goal", AdvancementType.GOAL, Color.CYAN, Color.WHITE),
    CHALLENGE("challenge", AdvancementType.CHALLENGE, Color.PURPLE, Color.CYAN);

    private final String name;
    private final Component displayName;
    private final AdvancementType conventionalType;
    private final Color titleColor;
    private final Color descriptionColor;

    FancyAdvancementType(String name, AdvancementType conventionalType, Color titleColor, Color descriptionColor) {
        this.name = name;
        this.displayName = Components.of("toast_type." + name);
        this.conventionalType = conventionalType;
        this.titleColor = titleColor;
        this.descriptionColor = descriptionColor;
    }

    public String getName() {
        return name;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public AdvancementType getConventionalType() {
        return conventionalType;
    }

    public Color getTitleColor() {
        return titleColor;
    }

    public Color getDescriptionColor() {
        return descriptionColor;
    }
}
