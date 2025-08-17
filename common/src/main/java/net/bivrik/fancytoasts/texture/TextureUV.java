package net.bivrik.fancytoasts.texture;

public record TextureUV(int u, int v) {
    public record FrameUVS(TextureUV banner, TextureUV frame) {}

    public static final FrameUVS TASK_FRAME_UV = new FrameUVS(
            new TextureUV(0, 40),
            new TextureUV(0, 82)
    );

    public static final FrameUVS GOAL_FRAME_UV = new FrameUVS(
            new TextureUV(0, 54),
            new TextureUV(26, 82)
    );

    public static final FrameUVS CHALLENGE_FRAME_UV = new FrameUVS(
            new TextureUV(0, 68),
            new TextureUV(52, 82)
    );
}
