package net.bivrik.fancytoasts.utility;

public class DefaultUVs {
    public static final TextureUV BACKGROUND = new TextureUV(0, 0);
    public static final TextureUV PLAQUE = new TextureUV(0, 108);

    public static final TypeBasedUVs TASK = new TypeBasedUVs(
            new TextureUV(0, 40),
            new TextureUV(0, 82)
    );

    public static final TypeBasedUVs GOAL = new TypeBasedUVs(
            new TextureUV(0, 54),
            new TextureUV(26, 82)
    );

    public static final TypeBasedUVs CHALLENGE = new TypeBasedUVs(
            new TextureUV(0, 68),
            new TextureUV(52, 82)
    );
}
