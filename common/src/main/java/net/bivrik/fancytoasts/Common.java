package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.client.config.ConfigData;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.toast.registry.ToastAnimationRegistry;
import net.bivrik.fancytoasts.client.toast.animation.PlayfulAnimation;
import net.bivrik.fancytoasts.client.toast.animation.StandardAnimation;
import net.bivrik.fancytoasts.client.config.ConfigTextureManager;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.client.util.TextureLocations;
import net.bivrik.fancytoasts.client.toast.registry.ToastTextureRegistry;
import net.bivrik.fancytoasts.client.util.ResLoc;
import net.minecraft.resources.ResourceLocation;

// Only Vanilla code base
public class Common {
    public static ConfigData CONFIG;

    public static void init() {
        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            Debug.message("Common init on {} in a {} environment.", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());

            Debug.message("Textures registration:");
            registerTextures();
            ConfigTextureManager.load();

            Debug.message("Animations registration:");
            registerAnimations();

            CONFIG = ConfigHandler.load();
        }
    }

    private static void registerTextures() {
        registerTexture(
                TextureLocations.VANILLA,
                "vanilla"
        );
        registerTexture(
                TextureLocations.NATURE,
                "nature"
        );
        registerTexture(
                TextureLocations.OG,
                "og"
        );
        registerTexture(
                TextureLocations.MODERN,
                "modern"
        );
        registerTexture(
                TextureLocations.STEAMY,
                "steamy"
        );
        registerTexture(
                TextureLocations.TERRACRAFT,
                "terracraft"
        );
    }

    private static void registerTexture(ResourceLocation id, String name) {
        ToastTextureRegistry.register(
                id,
                Constants.MOD_ID,
                name,
                "Bivrik"
        );
    }

    private static void registerAnimations() {
        ToastAnimationRegistry.register(
                ResLoc.of("animation/standard"),
                StandardAnimation::new,
                Constants.MOD_ID,
                "standard"
        );

        ToastAnimationRegistry.register(
                ResLoc.of("animation/playful"),
                PlayfulAnimation::new,
                Constants.MOD_ID,
                "playful"
        );
    }
}
