package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.config.ConfigData;
import net.bivrik.fancytoasts.config.ConfigHandler;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.client.toast.texture.TextureLocations;
import net.bivrik.fancytoasts.client.toast.texture.ToastTextureRegistry;
import net.bivrik.fancytoasts.utility.FancyResourceLocation;

// Only Vanilla code base
public class Common {
    public static ConfigData CONFIG;

    public static void init() {
        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            Debug.message("Common init on {} in a {} environment.", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());

            registerTextureTypes();

            CONFIG = ConfigHandler.load();
        }
    }

    private static void registerTextureTypes() {
        ToastTextureRegistry.register(
                FancyResourceLocation.of("toast/vanilla"),
                TextureLocations.VANILLA,
                Constants.MOD_ID,
                "vanilla"
        );

        ToastTextureRegistry.register(
                FancyResourceLocation.of("toast/nature"),
                TextureLocations.NATURE,
                Constants.MOD_ID,
                "nature"
        );

        ToastTextureRegistry.register(
                FancyResourceLocation.of("toast/og"),
                TextureLocations.OG,
                Constants.MOD_ID,
                "og"
        );

        ToastTextureRegistry.register(
                FancyResourceLocation.of("toast/modern"),
                TextureLocations.MODERN,
                Constants.MOD_ID,
                "modern"
        );
    }
}
