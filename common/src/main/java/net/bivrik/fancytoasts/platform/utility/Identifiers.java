package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.core.Constants;
import net.minecraft.resources.Identifier;

public class Identifiers {
    public static Identifier withNamespaceAndPath(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    public static Identifier of(String path) {
        return withNamespaceAndPath(Constants.MOD_ID, path);
    }

    public static Identifier parse(String location) {
        return Identifier.parse(location);
    }

    public static Identifier fromMinecraft(String path) {
        return Identifier.withDefaultNamespace(path);
    }
}
