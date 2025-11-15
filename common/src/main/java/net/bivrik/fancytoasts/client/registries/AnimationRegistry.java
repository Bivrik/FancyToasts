package net.bivrik.fancytoasts.client.registries;

import net.bivrik.fancytoasts.Debug;
import net.bivrik.fancytoasts.client.toast.animation.FancyAdvancementToastAnimation;
import net.bivrik.fancytoasts.client.toast.animation.StandardAnimation;
import net.bivrik.fancytoasts.client.toast.DisplayData;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class AnimationRegistry {
    private static final Logger LOGGER = Debug.getLogger(AnimationRegistry.class);

    private static final Map<ResourceLocation, AnimationHandler> ANIMATIONS = new HashMap<>();

    private static void addAnimation(ResourceLocation id, AnimationHandler handler) {
        ANIMATIONS.put(id, handler);
    }

    public static boolean register(ResourceLocation id, Supplier<FancyAdvancementToastAnimation> animation, DisplayData data) {
        if (ANIMATIONS.containsKey(id)) {
            LOGGER.warn("{} already exists! It has to be unique", id);
            return false;
        }

        addAnimation(id, new AnimationHandler(animation, data));

        LOGGER.info("Registered {}", id);
        return true;
    }

    public static boolean register(ResourceLocation id, Supplier<FancyAdvancementToastAnimation> animation, String name, String author, String description) {
        return register(id, animation, new DisplayData(name, author, description, true));
    }

    public static boolean isRegistered(ResourceLocation id) {
        return ANIMATIONS.getOrDefault(id, null) != null;
    }

    private static AnimationHandler getAnimationHandler(ResourceLocation id) {
        return ANIMATIONS.computeIfAbsent(id, key -> {
            LOGGER.error("{} is missing", key);
            return getDefaultAnimationHandler();
        });
    }

    private static AnimationHandler getDefaultAnimationHandler() {
        return new AnimationHandler(StandardAnimation::new, TextureRegistry.getDefaultData());
    }

    public static DisplayData getData(ResourceLocation id) {
        return getAnimationHandler(id).data;
    }

    public static Supplier<FancyAdvancementToastAnimation> getAnimation(ResourceLocation id) {
        return getAnimationHandler(id).animationFactory;
    }

    public static Collection<ResourceLocation> getIds() {
        return ANIMATIONS.keySet();
    }

    public record AnimationHandler(Supplier<FancyAdvancementToastAnimation> animationFactory, DisplayData data) {}
}
