package net.bivrik.fancytoasts.client.gui;

import net.minecraft.network.chat.Component;

public enum ResourceLocationFilter {
    A_Z,
    Z_A,
    BUILT_IN,
    CUSTOM;

    public Component getDisplayName() {
        return Component.literal(this.name().replace('_', '-'));
    }
}
