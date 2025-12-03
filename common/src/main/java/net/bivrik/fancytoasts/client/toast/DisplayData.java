package net.bivrik.fancytoasts.client.toast;

import net.bivrik.fancytoasts.platform.utility.Components;
import net.minecraft.network.chat.Component;

public class DisplayData {
    private static final Component UNKNOWN_LABEL = Components.of("label.unknown");

    private final Component displayName;
    private final Component displayAuthor;
    private final Component displayDescription;

    // isTranslatableName is true only if it's from Fancy Toasts'
    public DisplayData(String name, String author, String description, boolean isTranslatableName) {
        // Either Fancy Toasts' key, translatable, or anything else, not translatable
        if (name != null) {
            this.displayName = isTranslatableName ? Component.translatable(name) : Component.literal(getVisualAppealingString(name));
        } else {
            this.displayName = UNKNOWN_LABEL;
        }

        // Usually just string so don't matter, it doesn't have translation at all
        this.displayAuthor = author != null ? Component.literal(getVisualAppealingString(author)) : UNKNOWN_LABEL;

        // Translatable for Fancy Toasts' in general, Minecraft's sounds, Mods' sounds and Resource Packs' sounds, but not the custom textures
        this.displayDescription = description != null ? Component.translatable(description) : UNKNOWN_LABEL;
    }

    public DisplayData(DTO dto) {
        this(dto.name, dto.author, dto.description, false);
    }

    public Component getDisplayName() {
        return displayName;
    }

    public Component getDisplayDescription() {
        return displayDescription;
    }

    public Component getAuthor() {
        return displayAuthor;
    }

    public static String getVisualAppealingString(String stringToConvert) {
        if (stringToConvert == null || stringToConvert.isEmpty()) {
            return "";
        }

        if (isVisualAppealing(stringToConvert)) {
            return stringToConvert;
        }

        String visualAppealingString = stringToConvert.replace('.', ' ').replace('_', ' ').trim();

        if (visualAppealingString.isBlank()) {
            return "";
        }

        String firstLetter = visualAppealingString.substring(0, 1).toUpperCase();
        String leftovers = visualAppealingString.substring(1);

        return firstLetter + leftovers;
    }

    private static boolean isVisualAppealing(String stringToCheck) {
        return !stringToCheck.contains(".") && Character.isUpperCase(stringToCheck.charAt(0)) && !stringToCheck.contains("_");
    }

    @Override
    public String toString() {
        return String.format("DisplayData{name='%s', author='%s', description='%s'}", displayAuthor.getString(), displayAuthor.getString(), displayDescription.getString());
    }

    public record DTO(String name, String author, String description) {}
}
