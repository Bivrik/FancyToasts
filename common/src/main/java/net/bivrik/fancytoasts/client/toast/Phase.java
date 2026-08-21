package net.bivrik.fancytoasts.client.toast;

import net.bivrik.fancytoasts.utility.FastMath;

public record Phase(int durationTicks, int startTicks) {
    public static float getProgress(float timeTicks, int durationTicks, int startTicks) {
        float progress = (timeTicks - startTicks) / durationTicks;
        return FastMath.clamp(progress, 0.0f, 1.0f);
    }

    public float getProgress(float timeTicks) {
        float progress = (timeTicks - startTicks) / durationTicks;
        return FastMath.clamp(progress, 0.0f, 1.0f);
    }

    public boolean isStarted(float timeTicks) {
        return timeTicks > startTicks;
    }

    public boolean isActive(float timeTicks) {
        return timeTicks <= startTicks + durationTicks;
    }
}
