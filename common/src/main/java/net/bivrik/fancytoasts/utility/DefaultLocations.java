package net.bivrik.fancytoasts.utility;

import net.bivrik.fancytoasts.platform.utility.ResourceLocations;
import net.minecraft.resources.ResourceLocation;

public class DefaultLocations {
    public static class Textures {
        public static final ResourceLocation VANILLA = ResourceLocations.of("textures/toast/vanilla.png");
        public static final ResourceLocation NATURE = ResourceLocations.of("textures/toast/nature.png");
        public static final ResourceLocation OG = ResourceLocations.of("textures/toast/og.png");
        public static final ResourceLocation MODERN = ResourceLocations.of("textures/toast/modern.png");
        public static final ResourceLocation STEAMY = ResourceLocations.of("textures/toast/steamy.png");
        public static final ResourceLocation TERRACRAFT = ResourceLocations.of("textures/toast/terracraft.png");
    }

    public static class Animations {
        public static final ResourceLocation STANDARD = ResourceLocations.of("animations/toast/standard");
        public static final ResourceLocation PLAYFUL = ResourceLocations.of("animations/toast/playful");
        public static final ResourceLocation QUIRKY = ResourceLocations.of("animations/toast/quirky");
        public static final ResourceLocation OLDLIKE = ResourceLocations.of("animations/toast/oldlike");
    }
}
