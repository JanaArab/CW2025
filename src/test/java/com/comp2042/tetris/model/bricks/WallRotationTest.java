package com.comp2042.tetris.model.bricks;

import com.comp2042.tetris.utils.MatrixOperations;
import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to verify rotation behavior near walls
 */
class WallRotationTest {

    @Test
    void testIBrickRotationNearRightWall() {
        // Create a 10-column board (typical Tetris width)
        int[][] board = new int[20][10];

        // I-brick horizontal shape (4 cells wide)
        int[][] horizontalShape = {
            {0, 0, 0, 0},
            {1, 1, 1, 1},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
        };

        // I-brick vertical shape (1 cell wide)
        int[][] verticalShape = {
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0},
            {0, 1, 0, 0}
        };

        // Test rotating horizontal I-brick at x=6 (near right wall at column 9)
        // Horizontal brick at x=6 occupies columns 6,7,8,9 (fits exactly)
        assertFalse(MatrixOperations.intersect(board, horizontalShape, 6, 5),
            "Horizontal I-brick should fit at x=6");

        // After rotation to vertical, it should fit at x=6 (column 7 has the blocks)
        // Or be kicked to any valid position
        StandardRotationStrategy strategy = new StandardRotationStrategy();
        Point result = strategy.findOffsetForRotation(board, verticalShape, new Point(6, 5));

        assertNotNull(result, "Rotation should be possible");
        assertFalse(MatrixOperations.intersect(board, verticalShape,
            (int)result.getX(), (int)result.getY()),
            "Result position should not cause collision");
    }

    @Test
    void testLBrickRotationAtLeftWall() {
        int[][] board = new int[20][10];

        // L-brick in one orientation
        int[][] lShape1 = {
            {0, 0, 0, 0},
            {0, 3, 3, 3},
            {0, 3, 0, 0},
            {0, 0, 0, 0}
        };

        // L-brick rotated
        int[][] lShape2 = {
            {0, 0, 0, 0},
            {0, 3, 3, 0},
            {0, 0, 3, 0},
            {0, 0, 3, 0}
        };

        // At x=0 (left wall), should still be able to rotate
        StandardRotationStrategy strategy = new StandardRotationStrategy();
        Point result = strategy.findOffsetForRotation(board, lShape2, new Point(0, 5));

        assertNotNull(result, "Should find valid rotation position");
        // The rotation may result in negative X if the brick has a left offset in its matrix
        // This is actually correct - x=-1 with leftOffset=1 means the brick starts at column 0
        // Just verify no collision
        assertFalse(MatrixOperations.intersect(board, lShape2, (int)result.getX(), (int)result.getY()),
            "Result should not cause collision");
    }

    @Test
    void testRotationShouldPreferCloserToWall() {
        int[][] board = new int[20][10];

        // Simple 2x2 shape
        int[][] shape = {
            {1, 1},
            {1, 1}
        };

        // At right edge (x=8), shape occupies columns 8-9
        assertFalse(MatrixOperations.intersect(board, shape, 8, 5));

        StandardRotationStrategy strategy = new StandardRotationStrategy();
        Point result = strategy.findOffsetForRotation(board, shape, new Point(8, 5));

        // Should stay at x=8 since square doesn't change shape
        assertEquals(8, result.getX(), "Square brick shouldn't move when rotating");
    }
}

