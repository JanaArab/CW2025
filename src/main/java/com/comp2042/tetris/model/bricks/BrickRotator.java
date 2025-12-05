package com.comp2042.tetris.model.bricks;

import com.comp2042.tetris.model.event.NextShapeInfo;

import java.awt.Point;
import java.util.List;

/**
 * Handles brick rotation and tracks the current rotation state.
 * Uses a {@link RotationStrategy} to determine valid rotation positions.
 *
 * <p>This class manages:</p>
 * <ul>
 *   <li>The current brick and its rotation state</li>
 *   <li>Rotation attempts with wall-kick support</li>
 *   <li>Previewing the next rotation state</li>
 * </ul>
 *
 * @see RotationStrategy
 * @see StandardRotationStrategy
 */
public class BrickRotator {

    private final RotationStrategy rotationStrategy;
    private Brick brick;
    private int currentShape = 0;

    /**
     * Constructs a BrickRotator with the default rotation strategy.
     */
    public BrickRotator() {
        this(new StandardRotationStrategy());
    }

    /**
     * Constructs a BrickRotator with a custom rotation strategy.
     *
     * @param rotationStrategy the strategy for finding valid rotation positions
     */
    public BrickRotator(RotationStrategy rotationStrategy) {
        this.rotationStrategy = rotationStrategy;
    }

    /**
     * Previews the next rotation state without applying it.
     *
     * @return information about the next rotation shape and index
     * @throws IllegalStateException if no brick has been assigned
     */
    public NextShapeInfo peekNextRotation() {
        if (brick == null) {
            throw new IllegalStateException("BrickRotator: no brick assigned yet.");
        }
        int nextShape = (currentShape + 1) % getBrickShapes().size();
        return new NextShapeInfo(getBrickShapes().get(nextShape), nextShape);
    }

    /**
     * Gets the current rotation state's shape matrix.
     *
     * @return the 2D array representing the current shape
     * @throws IllegalStateException if no brick has been assigned
     */
    public int[][] getCurrentShape() {

        /*added an if statement for the same purpose  */
        if (brick == null) {
            throw new IllegalStateException("BrickRotator: no brick assigned yet.");
        }
        return getBrickShapes().get(currentShape);
    }

    /**
     * Attempts to rotate the brick left (counterclockwise).
     * Uses the rotation strategy to find a valid position with wall-kicks.
     *
     * @param boardMatrix the current board state for collision detection
     * @param offset the current position of the brick
     * @return the new offset if rotation succeeded, null if blocked
     */
    public Point tryRotateLeft(int[][] boardMatrix, Point offset) {
        NextShapeInfo nextShapeInfo = peekNextRotation();
        Point kickedOffset = rotationStrategy.findOffsetForRotation(boardMatrix, nextShapeInfo.getShape(), offset);
        if (kickedOffset != null) {
            currentShape = nextShapeInfo.getPosition();
            return kickedOffset;
        }
        return null;
    }

    /**
     * Sets the current rotation state index directly.
     *
     * @param currentShape the rotation state index to set
     */
    public void setCurrentShape(int currentShape) {
        this.currentShape = currentShape;
    }

    /**
     * Sets a new brick and resets the rotation state to 0.
     *
     * @param brick the brick to assign (will be cloned)
     */
    public void setBrick(Brick brick) {
        this.brick = brick.cloneBrick();
        currentShape = 0;
    }
    private List<int[][]> getBrickShapes() {
        return brick.cloneShape();
    }

    /**
     * Returns the total number of rotation states for the current brick.
     *
     * @return the count of rotation states, or 0 if no brick assigned
     */
    public int getRotationStateCount() {
        if (brick == null) return 0;
        return getBrickShapes().size();
    }

    /**
     * Returns the current rotation state index.
     *
     * @return the current shape index (0 to rotationStateCount-1)
     */
    public int getCurrentShapeIndex() {
        return currentShape;
    }

}
