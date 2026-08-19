package net.bivrik.fancytoasts.client.gui;

import net.bivrik.fancytoasts.platform.utility.Components;
import net.minecraft.network.chat.Component;

public enum IdentifierFilter {
    A_Z("a_z"),
    Z_A("z_a"),
    BUILT_IN("built_in"),
    CUSTOM("custom");

    private final String name;

    IdentifierFilter(String name) {
        this.name = name;
    }

    public Component getDisplayName() {
        return Components.of("filter." + name);
    }
}
