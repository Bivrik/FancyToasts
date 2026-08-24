package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.client.credits.CreditsManager;
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
import net.bivrik.fancytoasts.utility.DefaultIdentifiers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
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
    private FancyToastManager fancyToastManager;

    /**
     * Only uses in specific cases, when there is no way of DI, like mixins or screens.
     * @return {@link FancyToasts}
     */
    public static FancyToasts getInstance() {
        return INSTANCE;
    }

    public void onModInit() {
        Debug.info(Constants.MOD_NAME + " initialized on {} ({})", Services.PLATFORM.getName(), Services.PLATFORM.getEnvironmentName());

        configManager = new ConfigManager();
        configManager.init();
        creditsManager = new CreditsManager();
    }

    public void onMinecraftInit(Minecraft minecraft) {
        Debug.info("Minecraft initialized");

        customTextureManager = new CustomTextureManager(minecraft, configManager);
        keyBindingManager = new KeyBindingManager();
        splashManager = new SplashManager(minecraft);
        fancyToastManager = new FancyToastManager(minecraft, customTextureManager, configManager);
    }

    public void onTick() {
        keyBindingManager.tick();
        fancyToastManager.tick();
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
     * Some mods trigger Minecraft's {@link net.minecraft.client.gui.components.toasts.ToastManager#extractRenderState(GuiGraphicsExtractor)} on Minecraft initialization. Therefore, it can return null during this phase, and to avoid immediate crash, always check for null.
     * @return {@link FancyToastManager}
     */
    public @Nullable FancyToastManager getToastManager() {
        if (fancyToastManager == null) {
            Debug.warn("You cannot access FancyToastManager, it is null");
        }
        return fancyToastManager;
    }

    public static void registerKeyBindings() {
        KeyBindingRegistry.register("config_menu", GLFW.GLFW_KEY_K, () -> Minecraft.getInstance().gui.setScreen(new FancyToastsScreen(null)));
    }

    // Registration for Textures and Animations
    static {
        registerTexture(DefaultIdentifiers.Textures.VANILLA, "vanilla");
        registerTexture(DefaultIdentifiers.Textures.NATURE, "nature");
        registerTexture(DefaultIdentifiers.Textures.OG, "og");
        registerTexture(DefaultIdentifiers.Textures.MODERN, "modern");
        registerTexture(DefaultIdentifiers.Textures.STEAMY, "steamy");
        registerTexture(DefaultIdentifiers.Textures.TERRACRAFT, "terracraft");
        registerTexture(DefaultIdentifiers.Textures.LANDSPAPER, "landspaper");
        registerTexture(DefaultIdentifiers.Textures.NEON, "neon");

        registerAnimation(DefaultIdentifiers.Animations.STANDARD, "standard", StandardAnimation::new);
        registerAnimation(DefaultIdentifiers.Animations.PLAYFUL, "playful", PlayfulAnimation::new);
        registerAnimation(DefaultIdentifiers.Animations.QUIRKY, "quirky", QuirkyAnimation::new);
        registerAnimation(DefaultIdentifiers.Animations.OLDLIKE, "oldlike", OldlikeAnimation::new);
    }

    private static void registerTexture(Identifier id, String name) {
        String translationKeyName = Components.stringOf("toast.texture." + name);
        TextureRegistry.register(id, new DisplayData(
                translationKeyName, Constants.MOD_NAME, translationKeyName + ".description", true)
        );
    }

    private static void registerAnimation(Identifier id, String name, Supplier<FancyToastAnimation> animation) {
        String translationKeyName = Components.stringOf("toast.animation." + name);
        AnimationRegistry.register(id, animation, new DisplayData(
                translationKeyName, Constants.MOD_NAME, translationKeyName + ".description", true)
        );
    }
}
