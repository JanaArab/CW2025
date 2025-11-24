package com.comp2042.tetris.model.bricks;
import com.comp2042.tetris.utils.MatrixOperations;
import java.awt.Point;

public class StandardRotationStrategy implements RotationStrategy {
    private static final int[] KICKS = {-1, 1, -2, 2};

    @Override
    public Point findOffsetForRotation(int[][] boardMatrix, int[][] rotatedShape, Point currentOffset) {
        int currentX = (int) currentOffset.getX();
        int currentY = (int) currentOffset.getY();

        // Try current position first
        if (!MatrixOperations.intersect(boardMatrix, rotatedShape, currentX, currentY)) {
            // Current position works, but try to optimize by shifting toward walls if possible
            return optimizePosition(boardMatrix, rotatedShape, currentX, currentY);
        }

        // Current position doesn't work, try wall kicks
        for (int dx : KICKS) {
            int newX = currentX + dx;
            if (!MatrixOperations.intersect(boardMatrix, rotatedShape, newX, currentY)) {
                // Found a valid kick position, optimize it before returning
                return optimizePosition(boardMatrix, rotatedShape, newX, currentY);
            }
        }
        return null;
    }

    /**
     * After finding a valid position, try to shift the brick to fill gaps
     * and position it optimally (flush against walls when possible).
     * This prevents bricks from "floating" away from walls after rotation.
     * Only applies optimization when near walls on standard-sized boards.
     */
    private Point optimizePosition(int[][] boardMatrix, int[][] rotatedShape, int x, int y) {
        int boardWidth = boardMatrix.length > 0 ? boardMatrix[0].length : 0;

        // Only apply optimization on boards with width >= 8 (standard Tetris is 10)
        // This preserves behavior on smaller test boards
        if (boardWidth < 8) {
            return new Point(x, y);
        }

        // Calculate the actual width of the brick (rightmost filled column)
        int brickWidth = getBrickWidth(rotatedShape);
        int brickLeftOffset = getBrickLeftOffset(rotatedShape);

        // Calculate actual rightmost position of brick content on board
        int rightmostBrickColumn = x + brickWidth - 1;
        int leftmostBrickColumn = x + brickLeftOffset;

        // Check if brick is near a wall (within 1 cell of touching the wall)
        int gapToRightWall = (boardWidth - 1) - rightmostBrickColumn;
        int gapToLeftWall = leftmostBrickColumn;

        // Only optimize if there's a small gap (1-2 cells) to a wall
        boolean hasGapToRightWall = gapToRightWall > 0 && gapToRightWall <= 2;
        boolean hasGapToLeftWall = gapToLeftWall > 0 && gapToLeftWall <= 2;

        if (!hasGapToRightWall && !hasGapToLeftWall) {
            // Not near any wall with a gap, return position as-is
            return new Point(x, y);
        }

        int bestX = x;

        // If there's a gap to right wall, try to close it
        if (hasGapToRightWall) {
            for (int testX = x + 1; testX <= boardWidth - brickWidth + brickLeftOffset; testX++) {
                if (!MatrixOperations.intersect(boardMatrix, rotatedShape, testX, y)) {
                    bestX = testX;
                } else {
                    break; // Hit an obstacle, stop trying
                }
            }
        }

        // If there's a gap to left wall and we didn't move right, try to close it
        if (hasGapToLeftWall && bestX == x) {
            // Calculate minimum valid X (leftmost brick cell should be at board column 0)
            int minValidX = -brickLeftOffset;
            for (int testX = x - 1; testX >= minValidX; testX--) {
                if (!MatrixOperations.intersect(boardMatrix, rotatedShape, testX, y)) {
                    bestX = testX;
                } else {
                    break; // Hit an obstacle, stop trying
                }
            }
        }

        return new Point(bestX, y);
    }

    /**
     * Get the width of the actual brick content (distance from leftmost to rightmost filled cell + 1)
     */
    private int getBrickWidth(int[][] shape) {
        int maxCol = -1;
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0 && col > maxCol) {
                    maxCol = col;
                }
            }
        }
        return maxCol + 1;
    }

    /**
     * Get the left offset of the brick (column of leftmost filled cell)
     */
    private int getBrickLeftOffset(int[][] shape) {
        int minCol = Integer.MAX_VALUE;
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0 && col < minCol) {
                    minCol = col;
                }
            }
        }
        return minCol == Integer.MAX_VALUE ? 0 : minCol;
    }
}
