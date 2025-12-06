package com.comp2042.tetris.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Utility class for detecting when score crosses certain thresholds.
 * Used to trigger special effects like fireworks when score milestones are reached.
 *
 * @see com.comp2042.tetris.view.GameViewPresenter
 */
public final class ScoreThresholdDetector {

    private ScoreThresholdDetector() {
        // utility - prevent instantiation
    }

    /**
     * Determine which multiples of a step were crossed when going from oldScore to newScore.
     *
     * <p>Example: old=450, new=1550, step=500 returns [500, 1000, 1500]</p>
     *
     * @param oldScore the previous score (exclusive)
     * @param newScore the new score (inclusive)
     * @param step the threshold step size
     * @return a list of crossed threshold values
     * @throws IllegalArgumentException if step is not positive
     */
    public static List<Integer> determineCrossedThresholds(int oldScore, int newScore, int step) {
        if (step <= 0) {
            throw new IllegalArgumentException("step must be > 0");
        }
        List<Integer> crossed = new ArrayList<>();
        if (newScore <= oldScore) {
            return crossed;
        }

        int next = ((oldScore / step) + 1) * step;
        while (next <= newScore) {
            crossed.add(next);
            next += step;
        }
        return crossed;
    }
}
