package com.comp2042.tetris.model.score;

import com.comp2042.tetris.utils.Pure;

public class ScoreCalculator {
    private ScoreCalculator() {
    }

    @Pure
    public static int calculateRowClearBonus(int clearedRows) {
        if (clearedRows <= 0) {
            return 0;
        }
        return 50 * clearedRows * clearedRows;
    }
}
