package com.comp2042.tetris.model.level;

import com.comp2042.tetris.view.UIConstants;
import javafx.util.Duration;

/**
 * Level 3 - The most challenging level with multiple difficulty modifiers.
 * This level combines several mechanics to create maximum difficulty.
 *
 * <p>Level features:</p>
 * <ul>
 *   <li>Standard drop speed</li>
 *   <li>Garbage rows: Random garbage rows are added periodically</li>
 *   <li>Screen flicker: Visual distortion effect</li>
 *   <li>Hidden next brick: Cannot see upcoming pieces</li>
 *   <li>Inverted controls: Left/Right controls are swapped</li>
 * </ul>
 *
 * @see ClassicLevel
 * @see GameLevel
 */
public class Level3 extends ClassicLevel {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "Level 3";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Duration getTickInterval(int currentScore) {
        // Keeps standard speed, difficulty comes from garbage/flicker
        return UIConstants.DEFAULT_DROP_INTERVAL;
    }

    /**
     * {@inheritDoc}
     * @return true - garbage rows are enabled for this level
     */
    @Override
    public boolean isGarbageEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     * @return true - screen flickering is enabled for this level
     */
    @Override
    public boolean isFlickerEnabled() {
        return true;
    }

    /**
     * {@inheritDoc}
     * @return true - next brick preview is hidden for this level
     */
    @Override
    public boolean isNextBrickHidden() {
        return true;
    }

    /**
     * {@inheritDoc}
     * @return true - horizontal controls are inverted for this level
     */
    @Override
    public boolean areControlsInverted() {
        return true;
    }
}
