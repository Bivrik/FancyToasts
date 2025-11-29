package net.bivrik.fancytoasts.core;

import net.bivrik.fancytoasts.client.gui.screen.FancyToastsConfigScreen;
import net.bivrik.fancytoasts.client.registry.KeyBindingRegistry;
import net.bivrik.fancytoasts.client.gui.screen.CreditsScreen;
import net.bivrik.fancytoasts.client.toast.animation.*;
import net.bivrik.fancytoasts.client.toast.DisplayData;
import net.bivrik.fancytoasts.client.registry.AnimationRegistry;
import net.bivrik.fancytoasts.client.registry.TextureRegistry;
import net.bivrik.fancytoasts.platform.Services;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.utility.DefaultLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class Common {
    // Mod lifecycle
    public static void onModInit() {
        if (!Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            return;
        }
        Debug.info("Initialization on {} in a {} environment", Services.PLATFORM.getName(), Services.PLATFORM.getEnvironmentName());

        Managers.init();
        Managers.onModInit();
    }

    public static void onMinecraftInit(Minecraft minecraft) {
        Managers.onMinecraftInit(minecraft);
    }

    public static void onTick() {
        Managers.onTick();
    }

    // Registrations
    public static void registerKeyBindings() {
        KeyBindingRegistry.register("config_menu", GLFW.GLFW_KEY_K, () -> Minecraft.getInstance().setScreen(new FancyToastsConfigScreen(null)));
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
        registerTexture(DefaultLocations.Textures.LANDSPAPER, "paper");

        registerAnimation(DefaultLocations.Animations.STANDARD, "standard", StandardAnimation::new);
        registerAnimation(DefaultLocations.Animations.PLAYFUL, "playful", PlayfulAnimation::new);
        registerAnimation(DefaultLocations.Animations.QUIRKY, "quirky", QuirkyAnimation::new);
        registerAnimation(DefaultLocations.Animations.OLDLIKE, "oldlike", OldlikeAnimation::new);
    }
}
