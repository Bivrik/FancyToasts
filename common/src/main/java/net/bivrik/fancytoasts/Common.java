package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.client.config.*;
import net.bivrik.fancytoasts.client.gui.SplashManager;
import net.bivrik.fancytoasts.client.toast.animation.FancyAdvancementToastAnimation;
import net.bivrik.fancytoasts.client.toast.animation.QuirkyAnimation;
import net.bivrik.fancytoasts.client.toast.animation.PlayfulAnimation;
import net.bivrik.fancytoasts.client.toast.animation.StandardAnimation;
import net.bivrik.fancytoasts.client.toast.registry.ToastAnimationRegistry;
import net.bivrik.fancytoasts.client.toast.registry.ToastTextureRegistry;
import net.bivrik.fancytoasts.client.util.DefaultLocations;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

// Only Vanilla code base
public class Common {
    private static SplashManager splashManager;
    private static ConfigManager configManager;

    public static void onMinecraftInitialization(Minecraft minecraft) {
        splashManager = new SplashManager(minecraft.getUser());
        splashManager.load(minecraft.getResourceManager());

        configManager = new ConfigManager();
    }

    public static SplashManager getSplashManager() {
        return splashManager;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static void onModInitialization() {
        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            Debug.info("Common init on {} in a {} environment.", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());

            Debug.info("Textures registration:");
            registerTextures();

            Debug.info("Animations registration:");
            registerAnimations();
        }
    }

    private static void registerTextures() {
        registerTexture(DefaultLocations.Textures.VANILLA, "vanilla");
        registerTexture(DefaultLocations.Textures.NATURE, "nature");
        registerTexture(DefaultLocations.Textures.OG, "og");
        registerTexture(DefaultLocations.Textures.MODERN, "modern");
        registerTexture(DefaultLocations.Textures.STEAMY, "steamy");
        registerTexture(DefaultLocations.Textures.TERRACRAFT, "terracraft");

        ConfigTextureManager.load();
    }

    private static void registerAnimations() {
        registerAnimation(DefaultLocations.Animations.STANDARD, "standard", StandardAnimation::new);
        registerAnimation(DefaultLocations.Animations.PLAYFUL, "playful", PlayfulAnimation::new);
        registerAnimation(DefaultLocations.Animations.QUIRKY, "quirky", QuirkyAnimation::new);
    }

    private static void registerTexture(ResourceLocation id, String name) {
        ToastTextureRegistry.register(
                id,
                Constants.MOD_ID,
                name,
                Constants.MOD_NAME
        );
    }

    private static void registerAnimation(ResourceLocation id, String name, Supplier<FancyAdvancementToastAnimation> animation) {
        ToastAnimationRegistry.register(
                id,
                animation,
                Constants.MOD_ID,
                name
        );
    }
}
