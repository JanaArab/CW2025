package com.comp2042.tetris.model.score;

import com.comp2042.tetris.utils.Pure;

/**
 * Utility class for score calculations.
 * Provides pure functions for computing score bonuses.
 *
 * @see ScoreManager
 */
public class ScoreCalculator {
    private ScoreCalculator() {
        // Utility class - prevent instantiation
    }

    /**
     * Calculates the bonus score for clearing rows.
     * Uses exponential scaling: 50 * rows^2
     *
     * <p>Examples:</p>
     * <ul>
     *   <li>1 row = 50 points</li>
     *   <li>2 rows = 200 points</li>
     *   <li>3 rows = 450 points</li>
     *   <li>4 rows (Tetris) = 800 points</li>
     * </ul>
     *
     * @param clearedRows the number of rows cleared
     * @return the bonus score, or 0 if no rows cleared
     */
    @Pure
    public static int calculateRowClearBonus(int clearedRows) {
        if (clearedRows <= 0) {
            return 0;
        }
        return 50 * clearedRows * clearedRows;
    }
}
