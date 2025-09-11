package net.bivrik.fancytoasts.utility;

import net.bivrik.fancytoasts.Constants;
import net.minecraft.network.chat.Component;

public class ComponentHelper {
    private static final String DEFAULT_ID = Constants.MOD_ID;

    private static Component getTranslatableComponent(String modId, String name, TranslatableType type) {
        StringBuilder translationKey = new StringBuilder(DEFAULT_ID);
        if (modId != null && modId.compareTo(DEFAULT_ID) != 0) {
            translationKey.append(".").append(modId);
        }
        translationKey.append(".").append(type.getName()).append(".toast.").append(name);

        return Component.translatable(translationKey.toString());
    }

    /*
    Returns:
    > fancytoasts.`modid`.texture.`displayName`
    or
    > fancytoasts.texture.`displayName`
    */

    public static Component getTranslatableToastTexture(String modId, String name) {
        return getTranslatableComponent(modId, name, TranslatableType.TEXTURE);
    }

    /*
    Returns:
    > fancytoasts.`modid`.animation.`displayName`
    or
    > fancytoasts.animation.`displayName`
    */
    public static Component getTranslatableToastAnimation(String modId, String name) {
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
