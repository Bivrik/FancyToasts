package net.bivrik.fancytoasts.client.toast.texture;

import net.minecraft.network.chat.Component;

public class ToastTextureData {
    private final String name;
    private final String author;

    public ToastTextureData(Component name, String author) {
        this.name = name.getString();
        this.author = author;
    }

    public Component name() {
        return name != null ? Component.translatable(name) : Component.translatable("fancytoasts.gui.unknown");
    }

    public Component author() {
        return author != null ? Component.literal(author) : Component.translatable("fancytoasts.gui.unknown");
    }
}
