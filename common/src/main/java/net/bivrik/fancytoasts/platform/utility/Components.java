package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.core.Constants;
import net.minecraft.network.chat.Component;

public class Components {
    public static Component of(String key) {
        return Component.translatable(stringOf(key));
    }

    public static String stringOf(String key) {
        return Constants.MOD_ID + "." + key;
    }

    /**
     * Extracts key for translation from {@link Component}.
     * @param translatable translatable component
     * @return string key used for translation
     * @author Furglitch
     */
    public static String extractKey(Component translatable)
    {
        String s = translatable.toString();
        String marker = "key='";
        int startIndex = s.indexOf(marker);
        if (startIndex == -1) return null;

        startIndex += marker.length();
        int endIndex = s.indexOf("\'", startIndex);
        if (endIndex == -1) return null;

        return s.substring(startIndex, endIndex);
    }
}
