package com.comp2042.tetris.model.bricks;

import com.comp2042.tetris.utils.MatrixOperations;
import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Detailed tests to understand rotation behavior with actual brick shapes
 */
class DetailedRotationTest {

    @Test
    void testIBrickHorizontalToVerticalAtRightEdge() {
        // 10-column board
        int[][] board = new int[20][10];

        // Horizontal I-brick
        int[][] horizontal = {
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };

        // Vertical I-brick
        int[][] vertical = {
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0}
        };

        // Place horizontal I-brick at x=6
        // This means: columns 6,7,8,9 have blocks (row 1 of the matrix)
        System.out.println("Testing horizontal I-brick at x=6");
        assertFalse(MatrixOperations.intersect(board, horizontal, 6, 5));

        // Now rotate to vertical
        // In vertical orientation, the blocks are in column 1 of the 4x4 matrix
        // So if we keep x=6, the blocks would be at board column 7
        // This should fit fine!
        StandardRotationStrategy strategy = new StandardRotationStrategy();
        Point result = strategy.findOffsetForRotation(board, vertical, new Point(6, 5));

        System.out.println("Rotation result: " + result);
        assertNotNull(result);

        // The result should keep the brick as close to the right as possible
        // Check if the result position causes collision
        assertFalse(MatrixOperations.intersect(board, vertical, (int)result.getX(), (int)result.getY()));

        // Check what actual columns are occupied
        printBrickPosition("Vertical I-brick", vertical, (int)result.getX(), 10);
    }

    @Test
    void testIBrickVerticalToHorizontalAtRightEdge() {
        int[][] board = new int[20][10];

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

        // Vertical I-brick at x=7 means blocks are in column 8 (7 + 1)
        // This is close to the right wall
        System.out.println("\nTesting vertical I-brick at x=7");
        assertFalse(MatrixOperations.intersect(board, vertical, 7, 5));
        printBrickPosition("Vertical I-brick before rotation", vertical, 7, 10);

        // Rotate to horizontal
        // Horizontal has blocks at columns 0,1,2,3 of matrix
        // If we keep x=7, blocks would be at board columns 7,8,9,10
        // But column 10 is out of bounds! (board is 0-9)
        // So it should kick left
        StandardRotationStrategy strategy = new StandardRotationStrategy();
        Point result = strategy.findOffsetForRotation(board, horizontal, new Point(7, 5));

        System.out.println("Rotation result: " + result);
        assertNotNull(result, "Should find valid position");

        // Result should be x=6 (the rightmost valid position)
        assertFalse(MatrixOperations.intersect(board, horizontal, (int)result.getX(), (int)result.getY()));
        printBrickPosition("Horizontal I-brick after rotation", horizontal, (int)result.getX(), 10);

        // The brick should be against the right wall
        // Horizontal I at x=6 means columns 6,7,8,9 - this is flush with the wall!
        assertTrue(result.getX() <= 6, "Should be at x=6 or further left to fit");
    }

    private void printBrickPosition(String label, int[][] brick, int xOffset, int boardWidth) {
        System.out.println(label + " at x=" + xOffset + ":");
        System.out.print("  Board columns: ");
        for (int i = 0; i < boardWidth; i++) {
            System.out.print(i);
        }
        System.out.println();

        for (int row = 0; row < brick.length; row++) {
            System.out.print("  " + row + ": ");
            for (int col = 0; col < boardWidth; col++) {
                if (col >= xOffset && col < xOffset + brick[row].length) {
                    int brickCol = col - xOffset;
                    System.out.print(brick[row][brickCol] != 0 ? "X" : ".");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
}

