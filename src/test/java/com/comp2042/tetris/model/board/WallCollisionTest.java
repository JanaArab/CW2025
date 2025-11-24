package com.comp2042.tetris.model.board;

import com.comp2042.tetris.model.bricks.*;
import com.comp2042.tetris.model.score.Score;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests to verify wall collision behavior
 */
class WallCollisionTest {

    @Test
    void testBrickCanMoveToLeftWall() {
        // Use the random generator which will give us various bricks
        SimpleBoard board = new SimpleBoard(20, 10);

        board.createNewBrick();

        // Move brick all the way to the left
        int moveCount = 0;
        while (board.moveBrickLeft() && moveCount < 20) {
            moveCount++;
        }

        System.out.println("Brick moved left " + moveCount + " times");

        // Should be able to move at least a few times (spawns in center)
        assertTrue(moveCount > 0, "Should be able to move left from center spawn");

        assertNotNull(board.getViewData());
    }

    @Test
    void testBrickCanMoveToRightWall() {
        SimpleBoard board = new SimpleBoard(20, 10);

        board.createNewBrick();

        // Move brick all the way to the right
        int moveCount = 0;
        while (board.moveBrickRight() && moveCount < 20) {
            moveCount++;
        }

        System.out.println("Brick moved right " + moveCount + " times");

        // Should be able to move at least a few times
        assertTrue(moveCount > 0, "Should be able to move right from center spawn");

        assertNotNull(board.getViewData());
    }

    @Test
    void testMultipleBricksMovement() {
        SimpleBoard board = new SimpleBoard(20, 10);

        // Test 5 random bricks
        for (int i = 0; i < 5; i++) {
            board.createNewBrick();

            int leftMoves = 0;
            while (board.moveBrickLeft() && leftMoves < 20) {
                leftMoves++;
            }

            System.out.println("Brick " + i + " moved left " + leftMoves + " times");
            assertTrue(leftMoves > 0, "Brick " + i + " should move left");

            // Create new brick for right test
            board.createNewBrick();

            int rightMoves = 0;
            while (board.moveBrickRight() && rightMoves < 20) {
                rightMoves++;
            }

            System.out.println("Brick " + i + " moved right " + rightMoves + " times");
            assertTrue(rightMoves > 0, "Brick " + i + " should move right");
        }
    }
}

