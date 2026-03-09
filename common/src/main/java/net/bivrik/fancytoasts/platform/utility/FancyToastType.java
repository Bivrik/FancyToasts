package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.core.Color;
import net.minecraft.advancements.FrameType;
import net.minecraft.network.chat.Component;

public enum FancyToastType {
    TASK("task", Color.YELLOW, Color.WHITE),
    GOAL("goal", Color.CYAN, Color.WHITE),
    CHALLENGE("challenge", Color.PURPLE, Color.CYAN);

    private final String name;
    private final Color mainColor;
    private final Color secondaryColor;
    private final Component displayName;
    private final Component displayAnnouncement;

    FancyToastType(String name, Color mainColor, Color secondaryColor) {
        this.name = name;
        this.mainColor = mainColor;
        this.secondaryColor = secondaryColor;
        this.displayName = Components.of("toast_type." + this.name);
        this.displayAnnouncement = Component.translatable("advancements.toast." + this.name);
    }

    public static FancyToastType transferTypes(FrameType frameType) {
        var fancyToastType = FancyToastType.TASK;
        switch (frameType) {
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

    public Color getMainColor() {
        return mainColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }
}
