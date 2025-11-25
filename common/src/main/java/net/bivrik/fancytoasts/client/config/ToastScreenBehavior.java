package net.bivrik.fancytoasts.client.config;

import net.bivrik.fancytoasts.platform.utility.Components;
import net.minecraft.network.chat.Component;

public enum ToastScreenBehavior {
    TRANSPARENT("transparent"),
    BEHIND("behind"),
    TOP("top");

    private final String name;
    private final Component displayName;

    ToastScreenBehavior(String name) {
        this.name = name;
        this.displayName = Components.of("gui.label.screen_behavior_" + this.name);
    }

    public String getName() {
        return name;
    }

    public Component getDisplayName() {
        return displayName;
    }
}
