package com.comp2042.tetris.model.level;

import com.comp2042.tetris.view.UIConstants;
import javafx.util.Duration;

/**
 * Level 2 with limited rotation mechanic.
 * This level adds a rotation constraint where each brick can only
 * be rotated a limited number of times.
 *
 * <p>Level features:</p>
 * <ul>
 *   <li>Standard drop speed (same as Classic)</li>
 *   <li>Rotation limit: 4 rotations per brick</li>
 * </ul>
 *
 * @see ClassicLevel
 * @see GameLevel
 */
public class Level2 extends ClassicLevel {

    /**
     * {@inheritDoc}
     */
    @Override
    public Duration getTickInterval(int currentScore) {
        return UIConstants.DEFAULT_DROP_INTERVAL;
    }

    /**
     * {@inheritDoc}
     * Returns 4, limiting rotations to 4 per brick.
     */
    @Override
    public int getRotationLimit() {
        return 4;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Level 2";
    }
}
