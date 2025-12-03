package net.bivrik.fancytoasts.utility;

public class MathEasing {
    private static float easeOut(float t) {
        return (float) (1 - Math.pow(1 - t, 8));
    }
    private static float easeIn(float t) {
        return (float) (Math.pow(t, 8));
    }
    private static float easeInOut(float t) {
        return (float) (Math.pow(t, 2) * (3.0f - 2.0f * t));
    }
    private static float elasticEaseOut(float t) {
        return (float) (1 - Math.pow(2, -10 * t) * Math.cos(t * Math.PI * 4));
    }

    public static float easeOutLerp(float start, float end, float t) {
        return start + (end - start) * easeOut(t);
    }
    public static int easeOutLerp(int start, int end, float t) {
        return (int) (start + (end - start) * easeOut(t));
    }

    public static float easeInLerp(float start, float end, float t) {
        return start + (end - start) * easeIn(t);
    }
    public static int easeInLerp(int start, int end, float t) {
        return (int) (start + (end - start) * easeIn(t));
    }

    public static float easeInOutLerp(float start, float end, float t) {
        return start + (end - start) * easeInOut(t);
    }
    public static int easeInOutLerp(int start, int end, float t) {
        return (int) (start + (end - start) * easeInOut(t));
    }

    public static float elasticEaseOutLerp(float start, float end, float t) {
        return start + (end - start) * elasticEaseOut(t);
    }
    public static int elasticEaseOutLerp(int start, int end, float t) {
        return (int) (start + (end - start) * elasticEaseOut(t));
    }
}
