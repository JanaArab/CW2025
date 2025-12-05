/**
 * responsible for displaying the state of the game with the help of gui
 */

package com.comp2042.tetris.model.data;

import com.comp2042.tetris.utils.MatrixOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data transfer object containing all view-related information for rendering.
 * This class encapsulates the current brick shape, position, next brick preview,
 * ghost brick position, and rotation metadata.
 *
 * <p>ViewData is immutable and provides defensive copies of mutable data.</p>
 *
 * @see com.comp2042.tetris.view.BoardRenderer
 */
public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final List<int[][]> nextBricksData;
    private final int ghostYPosition;
    private final int currentRotationIndex;
    private final int rotationStateCount;
    private final int rotationsUsed;

    /**
     * Constructs ViewData with basic brick information.
     *
     * @param brickData   the current brick shape matrix
     * @param xPosition    the horizontal position
     * @param yPosition    the vertical position
     * @param nextBrickData the next brick shape matrix
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        this(brickData, xPosition, yPosition, nextBrickData, yPosition);
    }

    /**
     * Constructs ViewData with ghost position.
     *
     * @param brickData    the current brick shape matrix
     * @param xPosition     the horizontal position
     * @param yPosition     the vertical position
     * @param nextBrickData the next brick shape matrix
     * @param ghostYPosition the Y position where the brick would land
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostYPosition) {
        this(brickData, xPosition, yPosition, nextBrickData, ghostYPosition, 0, 1, 0);
    }

    /**
     * Constructs ViewData with multiple next brick previews.
     *
     * @param brickData    the current brick shape matrix
     * @param xPosition     the horizontal position
     * @param yPosition     the vertical position
     * @param nextBricksData list of next brick shape matrices
     * @param ghostYPosition the Y position where the brick would land
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]> nextBricksData, int ghostYPosition) {
        this(brickData, xPosition, yPosition, nextBricksData, ghostYPosition, 0, 1, 0);
    }

    /**
     * Constructs ViewData with full rotation metadata.
     *
     * @param brickData    the current brick shape matrix
     * @param xPosition     the horizontal position
     * @param yPosition     the vertical position
     * @param nextBrickData the next brick shape matrix
     * @param ghostYPosition the Y position where the brick would land
     * @param currentRotationIndex the current rotation state index
     * @param rotationStateCount total number of rotation states
     * @param rotationsUsed number of rotations used for current brick
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostYPosition,
                    int currentRotationIndex, int rotationStateCount, int rotationsUsed) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.nextBricksData = Collections.singletonList(nextBrickData);
        this.ghostYPosition = ghostYPosition;
        this.currentRotationIndex = currentRotationIndex;
        this.rotationStateCount = rotationStateCount;
        this.rotationsUsed = rotationsUsed;
    }

    /**
     * Constructs ViewData with multiple next bricks and rotation metadata.
     *
     * @param brickData    the current brick shape matrix
     * @param xPosition     the horizontal position
     * @param yPosition     the vertical position
     * @param nextBricksData list of next brick shape matrices
     * @param ghostYPosition the Y position where the brick would land
     * @param currentRotationIndex the current rotation state index
     * @param rotationStateCount total number of rotation states
     * @param rotationsUsed number of rotations used for current brick
     */
    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]> nextBricksData, int ghostYPosition,
                    int currentRotationIndex, int rotationStateCount, int rotationsUsed) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBricksData.isEmpty() ? new int[0][0] : nextBricksData.get(0);
        this.nextBricksData = new ArrayList<>(nextBricksData);
        this.ghostYPosition = ghostYPosition;
        this.currentRotationIndex = currentRotationIndex;
        this.rotationStateCount = rotationStateCount;
        this.rotationsUsed = rotationsUsed;
    }

    /**
     * Returns a copy of the current brick shape matrix.
     *
     * @return defensive copy of the brick data
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Returns the horizontal position of the brick.
     *
     * @return the X position (column index)
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * Returns the vertical position of the brick.
     *
     * @return the Y position (row index)
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * Returns the Y position where the ghost brick is displayed.
     * This shows where the brick would land if dropped.
     *
     * @return the ghost brick Y position
     */
    public int getGhostYPosition() {
        return ghostYPosition;
    }

    /**
     * Returns copies of all next brick preview data.
     *
     * @return list of next brick shape matrices
     */
    public List<int[][]> getNextBricksData() {
        List<int[][]> copies = new ArrayList<>();
        for (int[][] brickData : nextBricksData) {
            copies.add(MatrixOperations.copy(brickData));
        }
        return copies;
    }

    /**
     * Returns the current rotation state index.
     *
     * @return rotation index (0 to rotationStateCount-1)
     */
    public int getCurrentRotationIndex() {
        return currentRotationIndex;
    }

    /**
     * Returns the total number of rotation states for the current brick.
     *
     * @return the rotation state count
     */
    public int getRotationStateCount() {
        return rotationStateCount;
    }

    /**
     * Returns the number of rotations used for the current brick.
     *
     * @return rotations used count
     */
    public int getRotationsUsed() {
        return rotationsUsed;
    }
}
