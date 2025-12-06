/**
 * Contains information about the next rotation state of a brick.
 * Used to preview rotations before committing them.
 *
 * @see com.comp2042.tetris.model.bricks.BrickRotator
 */

package com.comp2042.tetris.model.event;

import com.comp2042.tetris.utils.MatrixOperations;

public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    /**
     * Constructs a NextShapeInfo with the given shape and position.
     *
     * @param shape the rotation state's shape matrix
     * @param position the rotation state index
     */
    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Gets a copy of the shape matrix.
     *
     * @return a defensive copy of the shape matrix
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Gets the rotation position index.
     *
     * @return the rotation state index
     */
    public int getPosition() {
        return position;
    }
}
