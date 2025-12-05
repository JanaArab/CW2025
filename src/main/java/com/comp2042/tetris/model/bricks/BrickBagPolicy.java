package com.comp2042.tetris.model.bricks;

import java.util.Deque;
import java.util.List;

/**
 * Defines the policy for creating bags of bricks.
 * Different implementations can provide different randomization strategies.
 *
 * @see ShuffleBagPolicy
 */
public interface BrickBagPolicy {

    /**
     * Creates a new bag of bricks from the given prototypes.
     *
     * @param prototypes the list of brick prototypes to include in the bag
     * @return a deque containing the bricks in the desired order
     */
    Deque<Brick> createBag(List<Brick> prototypes);
}
