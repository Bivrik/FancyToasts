package net.bivrik.fancytoasts.client.toast.texture;

import net.minecraft.network.chat.Component;
import net.minidev.json.annotate.JsonIgnore;

public class DisplayData {
    private final String name;
    private final String description;
    private final String author;
    @JsonIgnore
    private final Component displayName;
    @JsonIgnore
    private final Component displayDescription;
    @JsonIgnore
    private final Component displayAuthor;

    public DisplayData(String name, String author, String description, boolean isTranslatableName) { // isTranslatable is true only if it's from Fancy Toasts'
        this.name = name;
        this.description = description;
        this.author = author;

        this.displayName = isTranslatableName ? Component.translatable(name) : Component.literal(getVisualAppealingString(name)); // Either Fancy Toasts' key, translatable, or anything else, not translatable
        this.displayDescription = Component.translatable(description); // Translatable for Fancy Toasts' in general, Minecraft's sounds, Mods' sounds and Resource Packs' sounds, but not the custom textures
        this.displayAuthor = Component.literal(getVisualAppealingString(author)); // Usually just string so don't matter, it doesn't have translation at all
    }

    public Component getDisplayName() {
        return name != null ? displayName : Component.translatable("fancytoasts.gui.unknown");
    }

    public Component getDisplayDescription() {
        return description != null ? displayDescription : Component.translatable("fancytoasts.gui.unknown");
    }

    public Component getAuthor() {
        return author != null ? displayAuthor : Component.translatable("fancytoasts.gui.unknown");
    }

    public static String getVisualAppealingString(String stringToConvert) {
        if (isVisualAppealing(stringToConvert)) {
            return stringToConvert;
        }

        String visualAppealingString = stringToConvert.replace('.', ' ');
        return visualAppealingString.substring(0, 1).toUpperCase() + visualAppealingString.substring(1);
    }

    public static boolean isVisualAppealing(String stringToCheck) {
        return !stringToCheck.contains(".") && Character.isUpperCase(stringToCheck.charAt(0));
    }
}
