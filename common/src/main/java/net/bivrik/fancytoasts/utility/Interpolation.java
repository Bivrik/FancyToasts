package net.bivrik.fancytoasts.utility;

public final class Interpolation {
    private Interpolation() {}

    private static final Easing DEFAULT = Easing.LINEAR;

    private static float innerLerp(float start, float end, float delta) {
        return start + (end - start) * delta;
    }

    private static int innerLerp(int start, int end, float delta) {
        return start + FastMath.round((end - start) * delta);
    }

    public static float lerp(float start, float end, float delta, Easing easing) {
        if (start == end) return end;

        if (easing == null) easing = DEFAULT;
        float easedDelta = easing.applyEasing(delta);

        return innerLerp(start, end, easedDelta);
    }

    public static float lerp(float start, float end, float delta) {
        return lerp(start, end, delta, DEFAULT);
    }

    public static int lerp(int start, int end, float delta, Easing easing) {
        if (start == end) return end;

        if (easing == null) easing = DEFAULT;
        float easedDelta = easing.applyEasing(delta);

        return innerLerp(start, end, easedDelta);
    }

    public static int lerp(int start, int end, float delta) {
        return lerp(start, end, delta, DEFAULT);
    }
}
