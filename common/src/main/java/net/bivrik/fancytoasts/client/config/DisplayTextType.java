package net.bivrik.fancytoasts.client.config;

import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.platform.utility.ToastDisplayInfo;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

public enum DisplayTextType {
    DEFAULT("default", displayInfo -> null),
    TITLE("title", ToastDisplayInfo::getTitle),
    DESCRIPTION("description", ToastDisplayInfo::getDescription),
    ANNOUNCEMENT("announcement", ToastDisplayInfo::getAdvancementsAnnouncement);

    private final String name;
    private final Component displayName;
    private final Function<ToastDisplayInfo, Component> displayGetter;

    DisplayTextType(String name, Function<ToastDisplayInfo, Component> displayGetter) {
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

    public Component getDisplayTextOrElse(ToastDisplayInfo displayInfo, Component fallback) {
        return this == DEFAULT ? fallback : displayGetter.apply(displayInfo);
    }
}
