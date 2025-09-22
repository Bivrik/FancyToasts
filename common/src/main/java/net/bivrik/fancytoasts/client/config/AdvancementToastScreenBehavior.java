package net.bivrik.fancytoasts.client.config;

import net.minecraft.network.chat.Component;

public enum AdvancementToastScreenBehavior {
    TOP("top"),
    BEHIND("behind"),
    TRANSPARENT("transparent");

    private final String name;
    private final Component displayName;

    AdvancementToastScreenBehavior(String name) {
        this.name = name;
        this.displayName = Component.translatable("fancytoasts.gui.label.screen_behavior_" + this.name);
    }

    public String getName() {
        return name;
    }

    public Component getDisplayName() {
        return displayName;
    }
}
