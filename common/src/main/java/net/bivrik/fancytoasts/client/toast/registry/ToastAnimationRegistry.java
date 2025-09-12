package net.bivrik.fancytoasts.client.toast.registry;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.toast.animation.FancyAdvancementToastAnimation;
import net.bivrik.fancytoasts.client.toast.animation.StandardAnimation;
import net.bivrik.fancytoasts.client.toast.texture.DisplayData;
import net.bivrik.fancytoasts.utility.ComponentHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ToastAnimationRegistry {
    private static final Map<ResourceLocation, ToastAnimationHandler> ANIMATIONS = new HashMap<>();

    public static void register(ResourceLocation id, Supplier<FancyAdvancementToastAnimation> animation, String modId, String name, String description) {
        if (ANIMATIONS.containsKey(id)) {
            Debug.error("{} already exists! It needs to be unique", id);
            return;
        }

        Component translatableName = ComponentHelper.getTranslatableToastAnimation(modId, name);
        Component translatableDescription = Component.translatable(description);

        ANIMATIONS.put(id, new ToastAnimationHandler(animation, new DisplayData(translatableName, Constants.MOD_NAME, translatableDescription)));

        Debug.info("Registered {}", id);
    }

    public static void register(ResourceLocation id, Supplier<FancyAdvancementToastAnimation> animation, String modId, String name) {
        register(id, animation, modId, name, modId + ".animations.toast." + name + ".description");
    }

    private static ToastAnimationHandler getAnimationHandler(ResourceLocation id) {
        return ANIMATIONS.computeIfAbsent(id, key -> {
            Debug.error("Animation {} is missing", key);
            return new ToastAnimationHandler(StandardAnimation::new, new DisplayData(Component.translatable("fancytoasts.animations.toast.standard"), Constants.MOD_NAME, Component.translatable("fancytoasts.animations.toast.standard.description")));
        });
    }

    public static boolean isRegistered(ResourceLocation id) {
        return ANIMATIONS.getOrDefault(id, null) != null;
    }

    public record ToastAnimationHandler(Supplier<FancyAdvancementToastAnimation> animationFactory, DisplayData data) {}

    public static DisplayData getData(ResourceLocation id) {
        return getAnimationHandler(id).data;
    }

    public static Component getAnimationName(ResourceLocation id) {
        return getAnimationHandler(id).data.getName();
    }

    public static Supplier<FancyAdvancementToastAnimation> getAnimation(ResourceLocation id) {
        return getAnimationHandler(id).animationFactory;
    }

    public static Collection<ResourceLocation> getIds() {
        return ANIMATIONS.keySet();
    }
}
