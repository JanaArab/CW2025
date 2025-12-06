package com.comp2042.tetris.model.bricks;

import java.awt.Point;

/**
 * Strategy interface for finding valid rotation positions.
 * Implementations handle wall-kick logic to find valid positions
 * when rotation would cause collision.
 *
 * @see StandardRotationStrategy
 * @see BrickRotator
 */
public interface RotationStrategy {

    /**
     * Finds a valid position for a rotated brick.
     *
     * @param boardMatrix the current board state for collision detection
     * @param rotatedShape the shape matrix after rotation
     * @param currentOffset the current position of the brick
     * @return the adjusted position if valid, null if rotation is blocked
     */
    Point findOffsetForRotation(int[][] boardMatrix, int[][] rotatedShape, Point currentOffset);
}
