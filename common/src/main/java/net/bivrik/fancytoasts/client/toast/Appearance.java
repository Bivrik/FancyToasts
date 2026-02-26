package net.bivrik.fancytoasts.client.toast;

import net.bivrik.fancytoasts.utility.FastMath;

public record Appearance(int duration, int startPoint) {
    public static float getProgress(long time, int duration, int startPoint) {
        float progress = ((float) time - startPoint) / duration;
        return FastMath.clamp(progress, 0.0f, 1.0f);
    }

    public float getProgress(long time) {
        return getProgress(time, this.duration, this.startPoint);
    }
}
