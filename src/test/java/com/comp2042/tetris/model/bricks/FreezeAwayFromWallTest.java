package com.comp2042.tetris.model.bricks;

import com.comp2042.tetris.utils.MatrixOperations;
import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to find the "freeze away from wall" bug
 */
class FreezeAwayFromWallTest {

    @Test
    void testIBrickRotationWithObstacleNearWall() {
        int[][] board = new int[20][10];

        // Place an obstacle at column 8
        board[6][8] = 9;

        int[][] vertical = {
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0}
        };

        int[][] horizontal = {
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };

        // Vertical I at x=7 (blocks at column 8)
        // There's an obstacle at board[6][8], so the brick must be above it
        System.out.println("Testing with obstacle at column 8, row 6");

        // Try rotating at y=2 (above the obstacle)
        assertFalse(MatrixOperations.intersect(board, vertical, 7, 2));

        StandardRotationStrategy strategy = new StandardRotationStrategy();
        Point result = strategy.findOffsetForRotation(board, horizontal, new Point(7, 2));

        System.out.println("Rotation result: " + result);
        if (result != null) {
            System.out.println("Result x=" + result.getX() + ", y=" + result.getY());
            printBoard(board, horizontal, (int)result.getX(), (int)result.getY());
        }
    }

    @Test
    void testLBrickRotationAtRightWall() {
        int[][] board = new int[20][10];

        // L-brick shapes from the actual code
        int[][] lShape0 = {
            {0, 0, 0, 0},
            {0, 3, 3, 3},
            {0, 3, 0, 0},
            {0, 0, 0, 0}
        };

        int[][] lShape1 = {
            {0, 0, 0, 0},
            {0, 3, 3, 0},
            {0, 0, 3, 0},
            {0, 0, 3, 0}
        };

        // Place L-brick at x=7 in first orientation
        // Blocks are at columns 8,9,10 - but 10 is out of bounds!
        // So x=7 should NOT work
        assertTrue(MatrixOperations.intersect(board, lShape0, 7, 5),
            "L-brick at x=7 should intersect boundary");

        // Place it at x=6 - blocks at columns 7,8,9 - should work
        assertFalse(MatrixOperations.intersect(board, lShape0, 6, 5),
            "L-brick should fit at x=6");

        System.out.println("\nL-brick at x=6:");
        printBoard(board, lShape0, 6, 5);

        // Now try to rotate it
        StandardRotationStrategy strategy = new StandardRotationStrategy();
        Point result = strategy.findOffsetForRotation(board, lShape1, new Point(6, 5));

        System.out.println("After rotation result: " + result);
        if (result != null) {
            printBoard(board, lShape1, (int)result.getX(), (int)result.getY());

            // Check if brick is flush against right wall
            // lShape1 has blocks in columns 1 and 2 of the matrix (0-indexed)
            // At x=7, blocks would be at board columns 8,9 - flush with wall
            // At x=8, blocks would be at board columns 9,10 - out of bounds
            System.out.println("Brick is at x=" + result.getX());
            if (result.getX() < 7) {
                System.out.println("WARNING: Brick is NOT flush with right wall!");
            }
        }
    }

    @Test
    void testTBrickRotationAtRightWall() {
        int[][] board = new int[20][10];

        // T-brick in different orientations
        int[][] tShape0 = {
            {0, 0, 0, 0},
            {0, 7, 7, 7},
            {0, 0, 7, 0},
            {0, 0, 0, 0}
        };

        int[][] tShape1 = {
            {0, 0, 0, 0},
            {0, 0, 7, 0},
            {0, 7, 7, 0},
            {0, 0, 7, 0}
        };

        // T-brick at x=7 in first orientation
        // Blocks at columns 8,9,10 - out of bounds
        assertTrue(MatrixOperations.intersect(board, tShape0, 7, 5));

        // At x=6, blocks at columns 7,8,9 - should work
        assertFalse(MatrixOperations.intersect(board, tShape0, 6, 5));

        System.out.println("\nT-brick at x=6:");
        printBoard(board, tShape0, 6, 5);

        // Rotate to tShape1
        StandardRotationStrategy strategy = new StandardRotationStrategy();
        Point result = strategy.findOffsetForRotation(board, tShape1, new Point(6, 5));

        System.out.println("After rotation result: " + result);
        if (result != null) {
            printBoard(board, tShape1, (int)result.getX(), (int)result.getY());
            System.out.println("Brick is at x=" + result.getX());

            // tShape1 has width of 2 (columns 1,2 of matrix have blocks)
            // To be flush with right wall at column 9:
            // x + 2 should equal 9, so x should be 7
            if (result.getX() < 7) {
                System.out.println("WARNING: Brick is NOT flush with right wall!");
                System.out.println("Expected x=7 or x=8, got x=" + result.getX());
            }
        }
    }

    private void printBoard(int[][] board, int[][] brick, int xOffset, int yOffset) {
        System.out.println("  Columns: 0123456789");
        for (int row = 0; row < Math.min(10, board.length); row++) {
            System.out.print("  Row " + row + ": ");
            for (int col = 0; col < board[0].length; col++) {
                boolean hasBrick = false;
                if (row >= yOffset && row < yOffset + brick.length &&
                    col >= xOffset && col < xOffset + brick[0].length) {
                    int brickRow = row - yOffset;
                    int brickCol = col - xOffset;
                    if (brick[brickRow][brickCol] != 0) {
                        hasBrick = true;
                    }
                }

                if (hasBrick) {
                    System.out.print("X");
                } else if (board[row][col] != 0) {
                    System.out.print("#");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
}

