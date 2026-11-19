package net.bivrik.fancytoasts.client.toast;

import net.bivrik.fancytoasts.utility.FastMath;

public record Phase(int durationMs, int startTimeMs) {
    public static float getProgress(float timeMs, int durationMs, int startTimeMs) {
        float progress = (timeMs - startTimeMs) / durationMs;
        return FastMath.clamp(progress, 0.0f, 1.0f);
    }

    public float getProgress(float timeMs) {
        float progress = (timeMs - startTimeMs) / durationMs;
        return FastMath.clamp(progress, 0.0f, 1.0f);
    }

    public boolean isStarted(float timeMs) {
        return timeMs > startTimeMs;
    }

    public boolean isActive(float timeMs) {
        return timeMs <= startTimeMs + durationMs;
    }
}
