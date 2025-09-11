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

import java.util.HashMap;
import java.util.function.Supplier;

// Only Vanilla code base
public class Common {
    private static SplashManager splashManager;

    private static final HashMap<Class<? extends ConfigData>, ConfigData> CONFIGS = new HashMap<>();

    public static void onMinecraftInitialization(Minecraft minecraft) {
        splashManager = new SplashManager(minecraft.getUser());
        splashManager.load(minecraft.getResourceManager());
    }

    public static SplashManager getSplashManager() {
        return splashManager;
    }

    public static void onModInitialization() {
        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            Debug.info("Common init on {} in a {} environment.", Services.PLATFORM.getPlatformName(), Services.PLATFORM.getEnvironmentName());

            Debug.info("Textures registration:");
            registerTextures();

            Debug.info("Animations registration:");
            registerAnimations();

            loadConfig(ToastConfigData.class);
            loadConfig(GeneralConfigData.class);
        }
    }

    public static <T extends ConfigData> void loadConfig(Class<T> configDataClass) {
        CONFIGS.remove(configDataClass);
        var temp = ConfigHandler.load(configDataClass);
        CONFIGS.put(configDataClass, temp);
    }

    public static <T extends ConfigData> void updateConfig(T configData) {
        CONFIGS.remove(configData.getClass());
        CONFIGS.put(configData.getClass(), configData);
    }

    public static GeneralConfigData getGeneralConfig() {
        return (GeneralConfigData) CONFIGS.get(GeneralConfigData.class).get();
    }

    public static ToastConfigData getToastConfig() {
        return (ToastConfigData) CONFIGS.get(ToastConfigData.class).get();
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

    private static void registerTexture(ResourceLocation id, String name) {
        ToastTextureRegistry.register(
                id,
                Constants.MOD_ID,
                name,
                Constants.MOD_NAME
        );
    }

    private static void registerAnimations() {
        registerAnimation(DefaultLocations.Animations.STANDARD, "standard", StandardAnimation::new);
        registerAnimation(DefaultLocations.Animations.PLAYFUL, "playful", PlayfulAnimation::new);
        registerAnimation(DefaultLocations.Animations.QUIRKY, "quirky", QuirkyAnimation::new);
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
