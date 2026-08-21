package net.bivrik.fancytoasts.client.toast;

import net.bivrik.fancytoasts.platform.utility.AdvancementDisplay;
import net.bivrik.fancytoasts.utility.TextureUV;
import net.minecraft.resources.ResourceLocation;

public class AnimationSetup {
    private final ResourceLocation textureLocation;
    private final AdvancementDisplay display;
    private final TextureUV backgroundUV;
    private final TextureUV plaqueUV;

    public AnimationSetup(ResourceLocation textureLocation, AdvancementDisplay display, TextureUV backgroundUV, TextureUV plaqueUV) {
        this.textureLocation = textureLocation;
        this.display = display;
        this.backgroundUV = backgroundUV;
        this.plaqueUV = plaqueUV;
    }

    public ResourceLocation getTextureId() {
        return textureLocation;
    }

    public AdvancementDisplay getDisplay() {
        return display;
    }

    public TextureUV getBackgroundUV() {
        return backgroundUV;
    }

    public TextureUV getPlaqueUV() {
        return plaqueUV;
    }
}
