package net.bivrik.fancytoasts.client.util;

import net.bivrik.fancytoasts.Constants;
import net.minecraft.resources.ResourceLocation;

public class ResLoc {
    public static ResourceLocation of(String path) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
