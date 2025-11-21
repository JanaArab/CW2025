/**
 * used to keep up with the next rotation of brick
 * the index of rotation and the matrix
 */

package com.comp2042.tetris.model.event;

import com.comp2042.tetris.utils.MatrixOperations;

public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    public int getPosition() {
        return position;
    }
}
