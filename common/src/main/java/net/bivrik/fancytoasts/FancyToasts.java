package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.client.gui.screen.FancyToastsScreen;
import net.bivrik.fancytoasts.client.registry.AnimationRegistry;
import net.bivrik.fancytoasts.client.registry.KeyBindingRegistry;
import net.bivrik.fancytoasts.client.registry.TextureRegistry;
import net.bivrik.fancytoasts.client.toast.DisplayData;
import net.bivrik.fancytoasts.client.toast.animation.*;
import net.bivrik.fancytoasts.core.Constants;
import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.core.manager.*;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.utility.DefaultLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public final class FancyToasts {
    private FancyToasts() {}

    public static final EventManager EVENTS = new EventManager();
    private static final FancyToasts INSTANCE = new FancyToasts();

    private ConfigManager configManager;
    private CreditsManager creditsManager;
    private CustomTextureManager customTextureManager;
    private KeyBindingManager keyBindingManager;
    private SplashManager splashManager;
    private ToastManager toastManager;

    /**
     * Only uses in specific cases, when there is no way of DI, like mixins or screens.
     * @return {@link FancyToasts}
     */
    public static FancyToasts getInstance() {
        return INSTANCE;
    }

    public void onModInit() {
        if (!Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            return;
        }
        Debug.info(Constants.MOD_NAME + " initialized on {} ({})", Services.PLATFORM.getName(), Services.PLATFORM.getEnvironmentName());

        configManager = new ConfigManager();
        creditsManager = new CreditsManager();
    }

    public void onMinecraftInit(Minecraft minecraft) {
        Debug.info("Minecraft initialized");

        customTextureManager = new CustomTextureManager(minecraft, configManager);
        keyBindingManager = new KeyBindingManager();
        splashManager = new SplashManager(minecraft);
        toastManager = new ToastManager(minecraft, customTextureManager, configManager);
    }

    public void onTick() {
        keyBindingManager.tick();
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CreditsManager getCreditsManager() {
        return creditsManager;
    }

    public CustomTextureManager getCustomTextureManager() {
        return customTextureManager;
    }

    public KeyBindingManager getKeyBindingManager() {
        return keyBindingManager;
    }

    public SplashManager getSplashManager() {
        return splashManager;
    }

    /**
     * Some mods trigger Minecraft's ToastManager {@code update()} or {@code render()} on Minecraft initialization. Therefore, it can return null during this phase, and to avoid immediate crash, always check for null.
     * @return {@link ToastManager}
     */
    public @Nullable ToastManager getToastManager() {
        if (toastManager == null) {
            Debug.warn("You cannot access ToastManager, it is null");
        }
        return toastManager;
    }

    // Registrations
    public static void registerKeyBindings() {
        KeyBindingRegistry.register("config_menu", GLFW.GLFW_KEY_K, () -> Minecraft.getInstance().setScreen(new FancyToastsScreen(null)));
    }

    private static void registerTexture(ResourceLocation id, String name) {
        String translationKeyName = Components.stringOf("toast.texture." + name);
        TextureRegistry.register(id, new DisplayData(
                translationKeyName, Constants.MOD_NAME, translationKeyName + ".description", true)
        );
    }

    private static void registerAnimation(ResourceLocation id, String name, Supplier<FancyToastAnimation> animation) {
        String translationKeyName = Components.stringOf("toast.animation." + name);
        AnimationRegistry.register(id, animation, new DisplayData(
                translationKeyName, Constants.MOD_NAME, translationKeyName + ".description", true)
        );
    }

    static {
        registerKeyBindings();

        registerTexture(DefaultLocations.Textures.VANILLA, "vanilla");
        registerTexture(DefaultLocations.Textures.NATURE, "nature");
        registerTexture(DefaultLocations.Textures.OG, "og");
        registerTexture(DefaultLocations.Textures.MODERN, "modern");
        registerTexture(DefaultLocations.Textures.STEAMY, "steamy");
        registerTexture(DefaultLocations.Textures.TERRACRAFT, "terracraft");
        registerTexture(DefaultLocations.Textures.LANDSPAPER, "landspaper");
        registerTexture(DefaultLocations.Textures.NEON, "neon");

        registerAnimation(DefaultLocations.Animations.STANDARD, "standard", StandardAnimation::new);
        registerAnimation(DefaultLocations.Animations.PLAYFUL, "playful", PlayfulAnimation::new);
        registerAnimation(DefaultLocations.Animations.QUIRKY, "quirky", QuirkyAnimation::new);
        registerAnimation(DefaultLocations.Animations.OLDLIKE, "oldlike", OldlikeAnimation::new);
    }
}
