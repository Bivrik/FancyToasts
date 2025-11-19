package net.bivrik.fancytoasts;

import net.bivrik.fancytoasts.client.gui.FancyToastConfigScreen;
import net.bivrik.fancytoasts.client.toast.animation.*;
import net.bivrik.fancytoasts.client.toast.DisplayData;
import net.bivrik.fancytoasts.client.registries.AnimationRegistry;
import net.bivrik.fancytoasts.client.registries.TextureRegistry;
import net.bivrik.fancytoasts.platform.Managers;
import net.bivrik.fancytoasts.platform.utility.Components;
import net.bivrik.fancytoasts.utility.DefaultLocations;
import net.bivrik.fancytoasts.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class Common {
    public static void onModInit() {
        if (!Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            return;
        }
        Debug.info("Initialization on {} in a {} environment", Services.PLATFORM.getName(), Services.PLATFORM.getEnvironmentName());

        registerTextures();
        registerAnimations();

        Managers.init();
        Managers.onModInit();

        Managers.keyBindingManager().registerKey("config_menu", GLFW.GLFW_KEY_K, () -> Minecraft.getInstance().setScreen(new FancyToastConfigScreen(null)));
    }

    public static void onMinecraftInit(Minecraft minecraft) {
        Managers.onMinecraftInit(minecraft);
    }

    public static void onTick() {
        Managers.onTick();
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
        String translationKeyName = Components.stringOf("textures.toast." + name);
        TextureRegistry.register(id, new DisplayData(
                translationKeyName, Constants.MOD_NAME, translationKeyName + ".description", true)
        );
    }

    private static void registerAnimations() {
        registerAnimation(DefaultLocations.Animations.STANDARD, "standard", StandardAnimation::new);
        registerAnimation(DefaultLocations.Animations.PLAYFUL, "playful", PlayfulAnimation::new);
        registerAnimation(DefaultLocations.Animations.QUIRKY, "quirky", QuirkyAnimation::new);
        registerAnimation(DefaultLocations.Animations.OLDLIKE, "oldlike", OldlikeAnimation::new);
    }

    private static void registerAnimation(ResourceLocation id, String name, Supplier<FancyAdvancementToastAnimation> animation) {
        String translationKeyName = Components.stringOf("animations.toast." + name);
        AnimationRegistry.register(id, animation, new DisplayData(
                translationKeyName, Constants.MOD_NAME, translationKeyName + ".description", true)
        );
    }
}
