package net.bivrik.fancytoasts.utility;

import net.bivrik.fancytoasts.Constants;
import net.minecraft.resources.ResourceLocation;

public class FancyResourceLocation {
    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
