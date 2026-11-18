package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.core.Color;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

public class AdvancementDisplay {
    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern LEFTOVERS = Pattern.compile("(?:\\s|§[0-9a-fk-or])+$", Pattern.CASE_INSENSITIVE);

    private final ItemStack icon;
    private final Component title;
    private final Component description;
    private final Component announcement;
    private final Color titleColor;
    private final Color descriptionColor;
    private final AdvancementType type;

    /**
     * Description can be {@link Component#empty()}, but title and announcement must be always filled
     */
    public AdvancementDisplay(ItemStack icon, Component title, Component description, Component announcement, Color titleColor, Color descriptionColor, AdvancementType type) {
        this.icon = icon;
        this.title = fixComponent(title);
        this.description = fixComponent(description);
        this.announcement = announcement;
        this.titleColor = titleColor;
        this.descriptionColor = descriptionColor;
        this.type = type;
    }

    private Component fixComponent(Component text) {
        List<Part> parts = new ArrayList<>();

        text.visit((style, content) -> {
            String formatted = removeUnsupportedCharacters(content);
            formatted = WHITESPACE.matcher(formatted).replaceAll(" ");
            if (!formatted.isEmpty()) {
                parts.add(new Part(formatted, style));
            }
            return Optional.empty();
        }, Style.EMPTY);

        while (!parts.isEmpty()) {
            int last = parts.size() - 1;
            Part part = parts.get(last);
            String stripped = LEFTOVERS.matcher(part.content()).replaceAll("");
            if (stripped.isEmpty()) {
                parts.remove(last);
                continue;
            }
            parts.set(last, new Part(stripped, part.style()));
            break;
        }

        MutableComponent result = Component.empty();
        parts.forEach(part -> {
            result.append(Component.literal(part.content()).withStyle(part.style()));
        });

        return result;
    }

    private String removeUnsupportedCharacters(String source) {
        StringBuilder output = new StringBuilder(source.length());
        for (char c : source.toCharArray()) {
            if (!Character.isSurrogate(c)) {
                output.append(c);
            }
        }
        return output.toString();
    }

    public ItemStack getIcon() {
        return icon;
    }

    public Component getTitle() {
        return title;
    }

    public Component getDescription() {
        return description;
    }

    public Component getAnnouncement() {
        return announcement;
    }

    public Color getTitleColor() {
        return titleColor;
    }

    public Color getDescriptionColor() {
        return descriptionColor;
    }

    public final AdvancementType getType() {
        return type;
    }

    private record Part(String content, Style style) {}
}
