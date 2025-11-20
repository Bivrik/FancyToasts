package net.bivrik.fancytoasts.client.registry;

import net.bivrik.fancytoasts.core.Debug;
import net.bivrik.fancytoasts.client.toast.animation.FancyToastAnimation;
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

    public static boolean register(ResourceLocation id, Supplier<FancyToastAnimation> animation, DisplayData data) {
        if (ANIMATIONS.containsKey(id)) {
            LOGGER.warn("{} already exists! It has to be unique", id);
            return false;
        }

        addAnimation(id, new AnimationHandler(animation, data));

        LOGGER.info("Registered: {}", id);
        return true;
    }

    public static boolean register(ResourceLocation id, Supplier<FancyToastAnimation> animation, String name, String author, String description) {
        return register(id, animation, new DisplayData(name, author, description, true));
    }

    public static boolean isRegistered(ResourceLocation id) {
        return ANIMATIONS.getOrDefault(id, null) != null;
    }

    private static AnimationHandler getAnimationHandler(ResourceLocation id) {
        AnimationHandler data = ANIMATIONS.getOrDefault(id, null);
        if (data == null) {
            LOGGER.error("{} is missing, using default", id);
            return getDefaultAnimationHandler();
        }

        return data;
    }

    private static AnimationHandler getDefaultAnimationHandler() {
        return new AnimationHandler(StandardAnimation::new, TextureRegistry.getDefaultData());
    }

    public static DisplayData getData(ResourceLocation id) {
        return getAnimationHandler(id).data;
    }

    public static Supplier<FancyToastAnimation> getAnimation(ResourceLocation id) {
        return getAnimationHandler(id).animationFactory;
    }

    public static Collection<ResourceLocation> getIds() {
        return ANIMATIONS.keySet();
    }

    public record AnimationHandler(Supplier<FancyToastAnimation> animationFactory, DisplayData data) {}
}
