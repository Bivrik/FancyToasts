package net.bivrik.fancytoasts.platform.utility;

import net.minecraft.network.chat.Component;

public enum FancyQuestType {
    TASK("task", Colors.YELLOW, Colors.WHITE),
    QUEST("quest", Colors.CYAN, Colors.WHITE),
    CHAPTER("chapter", Colors.PURPLE, Colors.CYAN),
    BOOK("file", Colors.PURPLE, Colors.CYAN);

    private final String name;
    private final int mainColor;
    private final int secondaryColor;
    private final Component displayName;
    private final Component displayAnnouncement;

    FancyQuestType(String name, int mainColor, int secondaryColor) {
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

    public int getMainColor() {
        return mainColor;
    }

    public int getSecondaryColor() {
        return secondaryColor;
    }
}
