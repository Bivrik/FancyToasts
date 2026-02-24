package net.bivrik.fancytoasts.utility;

/**
 * "Fast" math nobody asked for. Questionable decision, but might be x1.2-2.0 times faster
 */
public final class FastMath {
    private FastMath() {}

    /**
     * Clamps a float value
     * @param value value to clamp
     * @param min min value
     * @param max max value
     * @return <code>max</code> if value is greater than <code>max</code>, <code>min</code> if value is less than <code>min</code>, value otherwise
     */
    public static float clamp(float value, float min, float max) {
        return value > max ? max : value < min ? min : value;
    }

    /**
     * Clamps an integer value
     * @param value value to clamp
     * @param min min value
     * @param max max value
     * @return <code>max</code> if value is greater than <code>max</code>, <code>min</code> if value is less than <code>min</code>, value otherwise
     */
    public static int clamp(int value, int min, int max) {
        return value > max ? max : value < min ? min : value;
    }

    /**
     * Rounds a float value to the closest integer
     * @param value value to round
     * @return rounded value to the closest integer
     */
    public static int round(float value) {
        return (int) (value + (value >= 0 ? 0.5f : -0.5f));
    }

    /**
     * Rounds a float value to the closest integer
     * @param value value to round
     * @return rounded value to the closest integer
     */
    public static int round(double value) {
        return (int) (value + (value >= 0 ? 0.5f : -0.5f));
    }
}
