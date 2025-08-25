package net.bivrik.fancytoasts.client.toast.animation;

public record Appearance(int duration, int startPoint) {

    public static float getProgress(long time, int duration, int startPoint) {
        return Math.min(1.0f, Math.max(0.0f, (float) (time - startPoint) / duration));
    }
    public static float getProgress(long time, Appearance appearance) {
        return getProgress(time, appearance.duration(), appearance.startPoint());
    }
    public float getProgress(long time) {
        return getProgress(time, this.duration, this.startPoint);
    }
}
