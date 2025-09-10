package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.client.config.ConfigData;
import net.bivrik.fancytoasts.client.config.ConfigHandler;
import net.bivrik.fancytoasts.client.config.ConfigTextureManager;
import net.bivrik.fancytoasts.client.gui.SplashManager;
import net.bivrik.fancytoasts.client.toast.animation.QuirkyAnimation;
import net.bivrik.fancytoasts.client.toast.animation.PlayfulAnimation;
import net.bivrik.fancytoasts.client.toast.animation.StandardAnimation;
import net.bivrik.fancytoasts.client.toast.registry.ToastAnimationRegistry;
import net.bivrik.fancytoasts.client.toast.registry.ToastTextureRegistry;
import net.bivrik.fancytoasts.client.util.ResLoc;
import net.bivrik.fancytoasts.client.util.TextureLocations;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

// Only Vanilla code base
public class Common {
    private static SplashManager splashManager;
    public static ConfigData CONFIG;

    public static void onMinecraftInitialization(Minecraft minecraft) {
        var id = CONFIG.getTextureId();
        if (id.toString().contains("config")) {
            ConfigTextureManager.registerInMinecraft(id);
        }

        splashManager = new SplashManager(minecraft.getUser());
        splashManager.load(minecraft.getResourceManager());
    }

    public static SplashManager getSplashManager() {
        return splashManager;
    }

    public static void onModInitialization() {
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
                "vanilla",
                "fancytoasts.texture.vanilla.description"
        );
        registerTexture(
                TextureLocations.NATURE,
                "nature",
                "fancytoasts.texture.nature.description"
        );
        registerTexture(
                TextureLocations.OG,
                "og",
                "fancytoasts.texture.og.description"
        );
        registerTexture(
                TextureLocations.MODERN,
                "modern",
                "fancytoasts.texture.modern.description"
        );
        registerTexture(
                TextureLocations.STEAMY,
                "steamy",
                "fancytoasts.texture.steamy.description"
        );
        registerTexture(
                TextureLocations.TERRACRAFT,
                "terracraft",
                "fancytoasts.texture.terracraft.description"
        );
    }

    private static void registerTexture(ResourceLocation id, String name, String description) {
        ToastTextureRegistry.register(
                id,
                Constants.MOD_ID,
                name,
                Constants.MOD_NAME,
                description
        );
    }

    private static void registerAnimations() {
        ToastAnimationRegistry.register(
                ResLoc.of("animation/standard"),
                StandardAnimation::new,
                Constants.MOD_ID,
                "standard",
                "fancytoasts.animation.standard.description"
        );

        ToastAnimationRegistry.register(
                ResLoc.of("animation/playful"),
                PlayfulAnimation::new,
                Constants.MOD_ID,
                "playful",
                "fancytoasts.animation.playful.description"
        );

        ToastAnimationRegistry.register(
                ResLoc.of("animation/quirky"),
                QuirkyAnimation::new,
                Constants.MOD_ID,
                "quirky",
                "fancytoasts.animation.quirky.description"
        );
    }
}
