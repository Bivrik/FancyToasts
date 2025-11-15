package net.bivrik.fancytoasts.client.toast.animation;

import net.bivrik.fancytoasts.client.toast.TextureUV;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.resources.ResourceLocation;

public record FancyAdvancementSetup(ResourceLocation texture, TextureUV.FrameUVS uvs, DisplayInfo display, int titleColor, int toastColor) {}
