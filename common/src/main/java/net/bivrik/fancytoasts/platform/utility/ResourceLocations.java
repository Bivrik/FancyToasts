package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.Constants;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocations {
    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }

    public static ResourceLocation fromMinecraft(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }

    public static ResourceLocation parse(String location) {
        return ResourceLocation.parse(location);
    }

    public static ResourceLocation withNamespaceAndPath(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
}
