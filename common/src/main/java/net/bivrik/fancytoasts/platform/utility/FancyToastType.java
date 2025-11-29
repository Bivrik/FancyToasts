package net.bivrik.fancytoasts.platform.utility;

import net.minecraft.advancements.AdvancementType;
import net.minecraft.network.chat.Component;

public enum FancyToastType {
    TASK("task", Colors.YELLOW, Colors.WHITE),
    GOAL("goal", Colors.CYAN, Colors.WHITE),
    CHALLENGE("challenge", Colors.PURPLE, Colors.CYAN);

    private final String name;
    private final int mainColor;
    private final int secondaryColor;
    private final Component displayName;
    private final Component displayAnnouncement;

    FancyToastType(String name, int mainColor, int secondaryColor) {
        this.name = name;
        this.mainColor = mainColor;
        this.secondaryColor = secondaryColor;
        this.displayName = Components.of("toast_type." + this.name);
        this.displayAnnouncement = Component.translatable("advancements.toast." + this.name);
    }

    public static FancyToastType transferTypes(AdvancementType advancementType) {
        var fancyToastType = FancyToastType.TASK;
        switch (advancementType) {
            case GOAL -> fancyToastType = FancyToastType.GOAL;
            case CHALLENGE -> fancyToastType = FancyToastType.CHALLENGE;
        }
        return fancyToastType;
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
