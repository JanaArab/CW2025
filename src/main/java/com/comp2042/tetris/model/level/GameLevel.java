package com.comp2042.tetris.model.level;

import javafx.util.Duration;

/**
 * Defines the characteristics of a game level in Tetris.
 * Each level can have different speed settings, special mechanics,
 * and gameplay modifiers.
 *
 * <p>Implementations can customize:</p>
 * <ul>
 *   <li>Game speed (tick interval)</li>
 *   <li>Rotation limits</li>
 *   <li>Garbage row mechanics</li>
 *   <li>Visual effects (flickering)</li>
 *   <li>UI modifications (hidden next brick)</li>
 *   <li>Control modifications (inverted controls)</li>
 * </ul>
 *
 * @see ClassicLevel
 * @see Level1
 * @see Level2
 * @see Level3
 */
public interface GameLevel {

    /**
     * Gets the tick interval (time between automatic brick drops).
     * Can vary based on the current score for progressive difficulty.
     *
     * @param currentScore the player's current score
     * @return the duration between game ticks
     */
    Duration getTickInterval(int currentScore);

    /**
     * Gets the display name of this level.
     *
     * @return the level name
     */
    String getName();

    /**
     * Gets the maximum number of rotations allowed per brick.
     *
     * @return the rotation limit, or -1 for unlimited
     */
    default int getRotationLimit() {
        return -1;
    }

    /**
     * Checks if garbage rows should be added during gameplay.
     *
     * @return true if garbage mechanics are enabled
     */
    default boolean isGarbageEnabled() {
        return false;
    }

    /**
     * Checks if screen flickering effects are enabled.
     *
     * @return true if flicker effects should be shown
     */
    default boolean isFlickerEnabled() {
        return false;
    }

    /**
     * Checks if the next brick preview should be hidden.
     *
     * @return true if next brick preview is hidden
     */
    default boolean isNextBrickHidden() {
        return false;
    }

    /**
     * Checks if horizontal controls are inverted.
     *
     * @return true if left/right controls are swapped
     */
    default boolean areControlsInverted() {
        return false;
    }
}
