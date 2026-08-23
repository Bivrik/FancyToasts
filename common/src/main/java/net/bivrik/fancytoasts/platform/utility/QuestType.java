package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.core.Color;
import net.minecraft.network.chat.Component;

public enum QuestType {
    TASK("task", AdvancementType.TASK, Color.YELLOW, Color.WHITE),
    QUEST("quest", AdvancementType.TASK, Color.CYAN, Color.WHITE),
    CHAPTER("chapter", AdvancementType.GOAL, Color.CYAN, Color.WHITE),
    BOOK("file", AdvancementType.CHALLENGE, Color.PURPLE, Color.WHITE);

    private final String name;
    private final Component displayName;
    private final AdvancementType conventionalType;
    private final Color titleColor;
    private final Color descriptionColor;

    QuestType(String name, AdvancementType conventionalType, Color titleColor, Color descriptionColor) {
        this.name = name;
        this.displayName = Components.of("quest_type." + name);
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
