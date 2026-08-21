package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.core.Color;
import net.minecraft.network.chat.Component;

public enum FancyQuestType {
    TASK("task", AdvancementType.TASK, Color.YELLOW, Color.WHITE),
    QUEST("quest", AdvancementType.TASK, Color.CYAN, Color.WHITE),
    CHAPTER("chapter", AdvancementType.GOAL, Color.CYAN, Color.WHITE),
    BOOK("file", AdvancementType.CHALLENGE, Color.PURPLE, Color.WHITE);

    private final String name;
    private final AdvancementType conventionalType;
    private final Color titleColor;
    private final Color descriptionColor;
    private final Component displayName;
    private final Component displayAnnouncement;

    FancyQuestType(String name, AdvancementType conventionalType, Color titleColor, Color descriptionColor) {
        this.name = name;
        this.conventionalType = conventionalType;
        this.titleColor = titleColor;
        this.descriptionColor = descriptionColor;
        this.displayName = Components.of("quest_type." + this.name);
        this.displayAnnouncement = Component.translatable("ftbquests." + this.name);
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
