package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.toast.TextureUV;
import net.bivrik.fancytoasts.client.toast.TypeBasedUVs;
import net.bivrik.fancytoasts.platform.utility.AdvancementToastDisplayInfo;
import net.minecraft.resources.ResourceLocation;

public class AnimationSetup {
    private final ResourceLocation textureLocation;
    private final AdvancementToastDisplayInfo displayInfo;
    private final TextureUV backgroundUV;
    private final TextureUV plaqueUV;
    private TypeBasedUVs typeBasedUVs;

    public AnimationSetup(ResourceLocation textureLocation, AdvancementToastDisplayInfo displayInfo, TypeBasedUVs typeBasedUVs, TextureUV backgroundUV, TextureUV plaqueUV) {
        this.textureLocation = textureLocation;
        this.displayInfo = displayInfo;
        this.backgroundUV = backgroundUV;
        this.plaqueUV = plaqueUV;
        this.typeBasedUVs = typeBasedUVs;
    }

    public void setTypeBasedUVs(TypeBasedUVs typeBasedUVs) {
        this.typeBasedUVs = typeBasedUVs;
    }

    public TypeBasedUVs typeBasedUVs() {
        return typeBasedUVs;
    }

    public ResourceLocation textureLocation() {
        return textureLocation;
    }

    public AdvancementToastDisplayInfo displayInfo() {
        return displayInfo;
    }

    public TextureUV backgroundUV() {
        return backgroundUV;
    }

    public TextureUV plaqueUV() {
        return plaqueUV;
    }
}
