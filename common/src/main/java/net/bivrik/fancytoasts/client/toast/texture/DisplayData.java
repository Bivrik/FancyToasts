package net.bivrik.fancytoasts.client.toast.texture;

import net.minecraft.network.chat.Component;

public class DisplayData {
    private final String name;
    private final String author;
    private final String description;

    public DisplayData(Component name, String author, Component description) {
        this.name = name.getString();
        this.author = author;
        this.description = description.getString();
    }

    public Component getName() {
        return name != null ? Component.translatable(name) : Component.translatable("fancytoasts.gui.unknown");
    }

    public Component getAuthor() {
        return author != null ? Component.translatable(author) : Component.translatable("fancytoasts.gui.unknown");
    }

    public Component getDescription() {
        return description != null ? Component.translatable(description) : Component.translatable("fancytoasts.gui.unknown");
    }
}
