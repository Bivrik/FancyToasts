package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.core.Color;
import net.minecraft.network.chat.Component;

public enum FancyQuestType {
    TASK("task", Color.YELLOW, Color.WHITE),
    QUEST("quest", Color.CYAN, Color.WHITE),
    CHAPTER("chapter", Color.CYAN, Color.WHITE),
    BOOK("file", Color.PURPLE, Color.WHITE);

    private final String name;
    private final Color mainColor;
    private final Color secondaryColor;
    private final Component displayName;
    private final Component displayAnnouncement;

    FancyQuestType(String name, Color mainColor, Color secondaryColor) {
        this.name = name;
        this.mainColor = mainColor;
        this.secondaryColor = secondaryColor;
        this.displayName = Components.of("quest_type." + this.name);
        this.displayAnnouncement = Component.translatable("ftbquests." + this.name);
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

    public Color getMainColor() {
        return mainColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }
}
