package com.comp2042.tetris.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ScoreThresholdDetector {

    private ScoreThresholdDetector() {
        // utility
    }

    /**
     * Determine which multiples of `step` were crossed when going from oldScore (exclusive) to newScore (inclusive).
     * For example: old=450, new=1550, step=500 => returns [500,1000,1500]
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
