package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.client.ConfigManager;
import net.bivrik.fancytoasts.client.CustomTextureManager;
import net.bivrik.fancytoasts.client.KeyBindingManager;
import net.bivrik.fancytoasts.client.gui.FancyToastConfigScreen;
import net.bivrik.fancytoasts.client.toast.animation.*;
import net.bivrik.fancytoasts.client.toast.DisplayData;
import net.bivrik.fancytoasts.client.ui.CreditsManager;
import net.bivrik.fancytoasts.client.ui.SplashManager;
import net.bivrik.fancytoasts.client.toast.AdvancementToastManager;
import net.bivrik.fancytoasts.client.registries.AnimationRegistry;
import net.bivrik.fancytoasts.client.registries.TextureRegistry;
import net.bivrik.fancytoasts.utility.DefaultLocations;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class Common {
    private static SplashManager splashManager;
    private static ConfigManager configManager;
    private static AdvancementToastManager advancementToastManager;
    private static KeyBindingManager keyBindingManager;
    private static CreditsManager creditsManager;
    private static CustomTextureManager customTextureManager;

    public static SplashManager getSplashManager() {
        return splashManager;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static AdvancementToastManager getAdvancementToastManager() {
        return advancementToastManager;
    }

    public static KeyBindingManager getKeyBindingManager() {
        return keyBindingManager;
    }

    public static CreditsManager getCreditsManager() {
        return creditsManager;
    }

    public static CustomTextureManager getCustomTextureManager() {
        return customTextureManager;
    }

    public static void onModInit() {
        if (!Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            return;
        }
        Debug.info("Initialization on {} in a {} environment", Services.PLATFORM.getName(), Services.PLATFORM.getEnvironmentName());

        keyBindingManager = new KeyBindingManager();
        configManager = new ConfigManager();
        creditsManager = new CreditsManager();

        registerTextures();
        registerAnimations();
        keyBindingManager.registerKey("config_menu", GLFW.GLFW_KEY_K, () -> Minecraft.getInstance().setScreen(new FancyToastConfigScreen(null)));
        configManager.loadConfigs();
        creditsManager.loadCredits();
    }

    public static void onMinecraftInit(Minecraft minecraft) {
        splashManager = new SplashManager(minecraft.getUser());
        splashManager.load(minecraft.getResourceManager());

        advancementToastManager = new AdvancementToastManager(minecraft);

        customTextureManager = new CustomTextureManager(minecraft);
        customTextureManager.load();
    }

    public static void onTick() {
        keyBindingManager.tick();
    }

    private static void registerTextures() {
        registerTexture(DefaultLocations.Textures.VANILLA, "vanilla");
        registerTexture(DefaultLocations.Textures.NATURE, "nature");
        registerTexture(DefaultLocations.Textures.OG, "og");
        registerTexture(DefaultLocations.Textures.MODERN, "modern");
        registerTexture(DefaultLocations.Textures.STEAMY, "steamy");
        registerTexture(DefaultLocations.Textures.TERRACRAFT, "terracraft");
    }

    private static void registerTexture(ResourceLocation id, String name) {
        TextureRegistry.register(id, new DisplayData(
                Constants.MOD_ID + ".textures.toast." + name, Constants.MOD_NAME, Constants.MOD_ID + ".textures.toast." + name + ".description", true)
        );
    }

    private static void registerAnimations() {
        registerAnimation(DefaultLocations.Animations.STANDARD, "standard", StandardAnimation::new);
        registerAnimation(DefaultLocations.Animations.PLAYFUL, "playful", PlayfulAnimation::new);
        registerAnimation(DefaultLocations.Animations.QUIRKY, "quirky", QuirkyAnimation::new);
        registerAnimation(DefaultLocations.Animations.OLDLIKE, "oldlike", OldlikeAnimation::new);
    }

    private static void registerAnimation(ResourceLocation id, String name, Supplier<FancyAdvancementToastAnimation> animation) {
        AnimationRegistry.register(id, animation, new DisplayData(
                Constants.MOD_ID + ".animations.toast." + name, Constants.MOD_NAME, Constants.MOD_ID + ".animations.toast." + name + ".description", true)
        );
    }
}
