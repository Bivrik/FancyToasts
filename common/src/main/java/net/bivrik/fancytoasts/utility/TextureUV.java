package net.bivrik.fancytoasts.utility;

public record TextureUV(int u, int v) {
    public static final TextureUV ZERO = new TextureUV(0, 0);
}
