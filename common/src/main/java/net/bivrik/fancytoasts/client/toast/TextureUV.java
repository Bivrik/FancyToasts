package net.bivrik.fancytoasts.client.toast;

public record TextureUV(int u, int v) {
    public static final TextureUV ZERO = new TextureUV(0, 0);
}
