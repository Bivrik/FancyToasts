package net.bivrik.fancytoasts.platform.utility;

import net.minecraft.advancements.AdvancementType;
import net.minecraft.network.chat.Component;

public enum FancyAdvancementType {
    TASK("task", Colors.YELLOW, Colors.WHITE),
    GOAL("goal", Colors.CYAN, Colors.WHITE),
    CHALLENGE("challenge", Colors.PURPLE, Colors.CYAN);

    private final String name;
    private final int mainColor;
    private final int secondaryColor;
    private final Component displayName;
    private final Component displayAnnouncement;

    FancyAdvancementType(String name, int mainColor, int secondaryColor) {
        this.name = name;
        this.mainColor = mainColor;
        this.secondaryColor = secondaryColor;
        this.displayName = Components.of("gui.label." + "name");
        this.displayAnnouncement = Component.translatable("advancements.toast." + name);
    }

    public static FancyAdvancementType transferTypes(AdvancementType advancementType) {
        var fancyAdvancementType = FancyAdvancementType.TASK;
        switch (advancementType) {
            case GOAL -> fancyAdvancementType = FancyAdvancementType.GOAL;
            case CHALLENGE -> fancyAdvancementType = FancyAdvancementType.CHALLENGE;
        }
        return fancyAdvancementType;
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
