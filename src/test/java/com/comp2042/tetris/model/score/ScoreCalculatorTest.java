package com.comp2042.tetris.model.score;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreCalculatorTest {

    @Test
    void returnsZeroWhenNoRowsCleared() {
        assertEquals(0, ScoreCalculator.calculateRowClearBonus(0));
        assertEquals(0, ScoreCalculator.calculateRowClearBonus(-3));
    }

    @Test
    void returnsFiftyForSingleLine() {
        assertEquals(50, ScoreCalculator.calculateRowClearBonus(1));
    }

    @Test
    void scalesQuadraticallyWithRowsCleared() {
        assertEquals(200, ScoreCalculator.calculateRowClearBonus(2));
        assertEquals(450, ScoreCalculator.calculateRowClearBonus(3));
        assertEquals(1250, ScoreCalculator.calculateRowClearBonus(5));
    }
}

