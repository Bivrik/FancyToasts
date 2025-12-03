package net.bivrik.fancytoasts.platform.utility;

import net.bivrik.fancytoasts.core.Constants;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocations {
    public static ResourceLocation withNamespaceAndPath(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }

    public static ResourceLocation of(String path) {
        return withNamespaceAndPath(Constants.MOD_ID, path);
    }

    public static ResourceLocation parse(String location) {
        return ResourceLocation.tryParse(location);
    }

    public static ResourceLocation fromMinecraft(String path) {
        return new ResourceLocation(ResourceLocation.DEFAULT_NAMESPACE, path);
    }
}
