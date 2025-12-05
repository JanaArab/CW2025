package com.comp2042.tetris.model.bricks;

import java.util.List;

/**
 * Interface for generating Tetris bricks.
 * Implementations control how new bricks are selected and
 * provide previews of upcoming bricks.
 *
 * <p>Common implementations include:</p>
 * <ul>
 *   <li>{@link RandomBrickGenerator} - Uses a bag randomization system</li>
 * </ul>
 *
 * @see RandomBrickGenerator
 * @see Brick
 */
public interface BrickGenerator {

    /**
     * Gets the next brick to be played and advances the queue.
     *
     * @return the next brick in the sequence
     */
    Brick getBrick();

    /**
     * Peeks at the next brick without removing it from the queue.
     *
     * @return the next brick that will be returned by {@link #getBrick()}
     */
    Brick getNextBrick();

    /**
     * Returns a preview of upcoming bricks.
     *
     * @param count the number of upcoming bricks to preview
     * @return a list of the next bricks in order
     */
    List<Brick> getNextBricks(int count);
}
