package net.bivrik.fancytoasts.core;

import net.bivrik.fancytoasts.utility.FastMath;

public enum Easing {
    LINEAR(t -> t),
    SINE_IN(t -> (float) (1 - Math.cos(t * Math.PI * 0.5f))),
    SINE_OUT(t -> (float) Math.sin(t * Math.PI * 0.5f)),
    SINE_IN_OUT(t -> (float) ((1 - Math.cos(t * Math.PI)) * 0.5f)),
    OCT_EASE_IN(t -> {
        float t2 = t * t;
        float t4 = t2 * t2;
        return t4 * t4;
    }),
    OCT_EASE_OUT(t -> {
        float x = 1 - t;
        float x2 = x * x;
        float x4 = x2 * x2;
        return 1 - (x4 * x4);
    }),
    OCT_EASE_IN_OUT(t -> {
        if (t < 0.5f) {
            float x = t * 2;
            float x2 = x * x;
            float x4 = x2 * x2;
            return (x4 * x4) / 2;
        } else {
            float x = t * -2 + 2;
            float x2 = x * x;
            float x4 = x2 * x2;
            return 1 - (x4 * x4) / 2;
        }
    }),
    ELASTIC_IN(t -> {
        if (t == 0) return 0;
        if (t == 1) return 1;

        double c = Math.PI / 3;
        return (float) (-Math.pow(2, 10 * t - 10) * Math.sin((t * 10 - 10.75f) * c));
    }),
    ELASTIC_OUT(t -> {
        if (t == 0) return 0;
        if (t == 1) return 1;

        double c = Math.PI / 3;
        return (float) (Math.pow(2, -10 * t) * Math.sin((t * 10 - 0.75f) * c) + 1);
    }),
    ELASTIC_IN_OUT(t -> {
        if (t == 0) return 0;
        if (t == 1) return 1;

        double c = Math.PI / 4.5f;
        double sin = Math.sin((20 * t - 11.125f) * c) / 2;
        if (t < 0.5f) {
            return (float) (-Math.pow(2, 20 * t - 10) * sin);
        } else {
            return (float) (Math.pow(2, -20 * t + 10) * sin + 1);
        }
    });

    private final MathEasing mathEasing;

    Easing(MathEasing mathEasing) {
        this.mathEasing = mathEasing;
    }

    private float applyEasing(float progress) {
        float clampedProgress = FastMath.clamp(progress, 0.0f, 1.0f);
        return mathEasing.apply(clampedProgress);
    }

    /**
     * A lerping function that returns float value depending on the progress in range [0,1]. Values out of this will be clamped
     * @param start start value (from)
     * @param end end value (to)
     * @param progress is a value in range [0,1] on change from <code>start</code> to <code>end</code>
     * @return float value between <code>start</code> and <code>end</code> depending on <code>progress</code>
     */
    public float lerp(float start, float end, float progress) {
        return innerLerp(start, end, progress);
    }

    private float innerLerp(float start, float end, float progress) {
        float delta = end - start;
        float easedProgress = applyEasing(progress);
        return start + delta * easedProgress;
    }

    /**
     * A lerping function that returns int value depending on the progress in range [0,1]. Values out of this will be clamped
     * @param start start value (from)
     * @param end end value (to)
     * @param progress is a value in range [0,1] on change from <code>start</code> to <code>end</code>
     * @return int value between <code>start</code> and <code>end</code> depending on <code>progress</code>
     */
    public int lerp(int start, int end, float progress) {
        return innerLerp(start, end, progress);
    }

    private int innerLerp(int start, int end, float progress) {
        int delta = end - start;
        float easedProgress = applyEasing(progress);
        return start + FastMath.round(delta * easedProgress);
    }

    /**
     * A lerping function that returns color depending on the progress in range [0,1]. Values out of this will be clamped
     * @param start start value (from)
     * @param end end value (to)
     * @param progress is a value in range [0,1] on change from <code>start</code> to <code>end</code>
     * @return color between <code>start</code> and <code>end</code> depending on <code>progress</code>
     */
    public Color lerp(Color start, Color end, float progress) {
        return new Color(
                innerLerp(start.getA(), end.getA(), progress),
                innerLerp(start.getR(), end.getR(), progress),
                innerLerp(start.getG(), end.getG(), progress),
                innerLerp(start.getB(), end.getB(), progress));
    }

    @FunctionalInterface
    private interface MathEasing {
        float apply(float progress);
    }
}
