/**
 * responsible for displaying the state of the game with the help of gui
 */

package com.comp2042.tetris.model.data;

import com.comp2042.tetris.utils.MatrixOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int[][] nextBrickData;
    private final List<int[][]> nextBricksData;
    private final int ghostYPosition;

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData) {
        this(brickData, xPosition, yPosition, nextBrickData, yPosition);
    }

    public ViewData(int[][] brickData, int xPosition, int yPosition, int[][] nextBrickData, int ghostYPosition) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBrickData;
        this.nextBricksData = Collections.singletonList(nextBrickData);
        this.ghostYPosition = ghostYPosition;
    }

    public ViewData(int[][] brickData, int xPosition, int yPosition, List<int[][]> nextBricksData, int ghostYPosition) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.nextBrickData = nextBricksData.isEmpty() ? new int[0][0] : nextBricksData.get(0);
        this.nextBricksData = new ArrayList<>(nextBricksData);
        this.ghostYPosition = ghostYPosition;
    }

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public int getGhostYPosition() {
        return ghostYPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
        //this method will be used later on to show next brick
    }

    public List<int[][]> getNextBricksData() {
        List<int[][]> copies = new ArrayList<>();
        for (int[][] brickData : nextBricksData) {
            copies.add(MatrixOperations.copy(brickData));
        }
        return copies;
    }
}
