package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.core.Constants;
import net.minecraft.network.chat.Component;

public class Components {
    private static final String DEFAULT_ID = Constants.MOD_ID;

    private static Component getTranslatableComponent(String modId, String name, TranslatableType type) {
        StringBuilder translationKey = new StringBuilder(DEFAULT_ID);
        if (modId != null && modId.compareTo(DEFAULT_ID) != 0) {
            translationKey.append(".").append(modId);
        }
        translationKey.append(".").append(type.getName()).append(".toast.").append(name);

        return Component.translatable(translationKey.toString());
    }

    public static Component of(String path) {
        return Component.translatable(stringOf(path));
    }

    public static String stringOf(String path) {
        return DEFAULT_ID + "." + path;
    }

    /**
     * Easier way to get translatable name for textures
     * @param modId is for texture's path. If 'null' or 'fancytoasts' then it returns default
     * @param name is for texture's name/identifier
     * @return fancytoasts.'modId'.textures.'displayName'
     */
    public static Component translatableTexture(String modId, String name) {
        return getTranslatableComponent(modId, name, TranslatableType.TEXTURE);
    }

    /**
     *Easier way to get translatable name for animations
     * @param modId is for animation's path. If 'null' or 'fancytoasts' then it returns default
     * @param name is for animation's name/identifier
     * @return fancytoasts.'modId'.animations.'displayName'
     */
    public static Component translatableAnimation(String modId, String name) {
        return getTranslatableComponent(modId, name, TranslatableType.ANIMATION);
    }

    private enum TranslatableType {
        ANIMATION("animations"),
        TEXTURE("textures");

        private final String name;

        TranslatableType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
