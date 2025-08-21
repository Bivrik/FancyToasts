package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.toast.animation.preset.PlayfulAnimation;
import net.bivrik.fancytoasts.client.toast.animation.preset.StandardAnimation;

import java.util.Map;
import java.util.function.Supplier;

public enum AnimationType {
    STANDARD,
    PLAYFUL;

    public static final Map<AnimationType, Supplier<FancyAdvancementToastAnimation>> ANIMATIONS = Map.of(
            AnimationType.STANDARD, StandardAnimation::new,
            AnimationType.PLAYFUL, PlayfulAnimation::new
    );
}
