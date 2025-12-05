package com.comp2042.tetris.model.level;

import com.comp2042.tetris.view.UIConstants;
import javafx.util.Duration;

/**
 * The classic Tetris level with constant drop speed.
 * This level maintains a fixed tick interval regardless of score,
 * providing the traditional Tetris gameplay experience.
 *
 * @see GameLevel
 */
public class ClassicLevel implements GameLevel {

    /**
     * {@inheritDoc}
     * Returns a constant drop interval for classic gameplay.
     */
    @Override
    public Duration getTickInterval(int currentScore) {
        return UIConstants.DEFAULT_DROP_INTERVAL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Classic";
    }
}
