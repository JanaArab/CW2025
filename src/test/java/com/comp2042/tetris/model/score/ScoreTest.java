package com.comp2042.tetris.model.score;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoreTest {

    @Test
    void valueStartsAtZero() {
        Score score = new Score();

        assertEquals(0, score.getValue());
    }

    @Test
    void addAccumulatesPositiveAndNegativeDeltas() {
        Score score = new Score();

        score.add(150);
        score.add(-20);

        assertEquals(130, score.getValue());
    }

    @Test
    void resetClearsAccumulatedPoints() {
        Score score = new Score();
        score.add(999);

        score.reset();

        assertEquals(0, score.getValue());
    }
}

