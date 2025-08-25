package net.bivrik.fancytoasts.client.toast.registry;

import net.bivrik.fancytoasts.Constants;
import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.toast.animation.FancyAdvancementToastAnimation;
import net.bivrik.fancytoasts.client.toast.animation.StandardAnimation;
import net.bivrik.fancytoasts.utility.ComponentHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ToastAnimationRegistry {
    private static final Map<ResourceLocation, ToastAnimationHandler> ANIMATIONS = new HashMap<>();

    public static void register(ResourceLocation id, Supplier<FancyAdvancementToastAnimation> animation, String modId, String name) {
        if (ANIMATIONS.containsKey(id)) {
            Debug.error("{} already exists! It needs to be unique", id);
            return;
        }

        Component translatableComponent = ComponentHelper.getTranslatableToastAnimation(modId, name);
        ANIMATIONS.put(id, new ToastAnimationHandler(translatableComponent, animation));

        Debug.message("Registered {}", id);
    }

    private static ToastAnimationHandler getAnimationHandler(ResourceLocation id) {
        return ANIMATIONS.computeIfAbsent(id, key -> {
            Debug.error("Animation {} is missing", key);
            return new ToastAnimationHandler(ComponentHelper.getTranslatableToastAnimation(Constants.MOD_ID, "animation/standard"), StandardAnimation::new);
        });
    }

    public static boolean isRegistered(ResourceLocation id) {
        return ANIMATIONS.getOrDefault(id, null) != null;
    }

    public record ToastAnimationHandler(Component name, Supplier<FancyAdvancementToastAnimation> animationFactory) {}

    public static Component getAnimationName(ResourceLocation id) {
        return getAnimationHandler(id).name;
    }

    public static Supplier<FancyAdvancementToastAnimation> getAnimation(ResourceLocation id) {
        return getAnimationHandler(id).animationFactory;
    }

    public static Collection<ResourceLocation> getIds() {
        return ANIMATIONS.keySet();
    }
}
