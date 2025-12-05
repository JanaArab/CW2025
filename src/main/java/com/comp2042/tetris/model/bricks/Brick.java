package com.comp2042.tetris.model.bricks;

import java.util.List;

/**
 * Represents a Tetris brick (tetromino) piece.
 * Each brick has multiple rotation states represented as 2D matrices.
 *
 * <p>Implementations include the standard seven Tetris pieces:</p>
 * <ul>
 *   <li>I - Long straight piece</li>
 *   <li>J - J-shaped piece</li>
 *   <li>L - L-shaped piece</li>
 *   <li>O - Square piece</li>
 *   <li>S - S-shaped piece</li>
 *   <li>T - T-shaped piece</li>
 *   <li>Z - Z-shaped piece</li>
 * </ul>
 *
 * @see BrickType
 * @see AbstractBrick
 */
public interface Brick extends Cloneable {

    /**
     * Returns the shape matrices for all rotation states of this brick.
     *
     * @return a list of 2D arrays representing each rotation state
     */
    List<int[][]> getShapeMatrix();

    /**
     * Returns a deep copy of all shape matrices.
     *
     * @return a cloned list of shape matrices
     */
    List<int[][]> cloneShape();

    /**
     * Creates a clone of this brick.
     *
     * @return a new Brick instance with the same properties
     */
    Brick cloneBrick();
}
