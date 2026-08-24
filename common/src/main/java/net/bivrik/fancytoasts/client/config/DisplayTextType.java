package net.bivrik.fancytoasts.client.config;

import net.bivrik.fancytoasts.platform.utility.AdvancementDisplay;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public enum DisplayTextType {
    DEFAULT("default", displayInfo -> null),
    TITLE("title", AdvancementDisplay::getTitle),
    DESCRIPTION("description", AdvancementDisplay::getDescription),
    ANNOUNCEMENT("announcement", AdvancementDisplay::getAnnouncement);

    private final String name;
    private final Component displayName;
    private final Function<AdvancementDisplay, Component> displayGetter;

    DisplayTextType(String name, Function<AdvancementDisplay, Component> displayGetter) {
        this.name = name;
        this.displayName = Components.of("text_type." + this.name);
        this.displayGetter = displayGetter;
    }

    public String getName() {
        return name;
    }

    public Component getDisplayName() {
        return displayName;
    }

    public Component getDisplayTextOrElse(AdvancementDisplay displayInfo, Component fallback) {
        return this == DEFAULT ? fallback : displayGetter.apply(displayInfo);
    }
}
