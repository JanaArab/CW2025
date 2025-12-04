package com.comp2042.tetris.model.board;

import com.comp2042.tetris.model.data.ViewData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Visual demonstration that bricks can now reach walls properly
 */
class WallReachDemonstrationTest {

    @Test
    void demonstrateWallReachFix() {
        System.out.println("\n========================================");
        System.out.println("WALL COLLISION FIX DEMONSTRATION");
        System.out.println("========================================\n");

        SimpleBoard board = new SimpleBoard(20, 10);

        System.out.println("Testing 10 random bricks to verify they can reach walls...\n");

        int leftWallTouches = 0;
        int rightWallTouches = 0;

        for (int i = 0; i < 10; i++) {
            // Test left wall
            board.createNewBrick();
            ViewData initialData = board.getViewData();

            while (board.moveBrickLeft()) {
                // Move until can't
            }

            ViewData leftData = board.getViewData();
            boolean touchesLeft = checkTouchesLeftWall(leftData);
            if (touchesLeft) {
                leftWallTouches++;
            }

            // Test right wall
            board.createNewBrick();

            while (board.moveBrickRight()) {
                // Move until can't
            }

            ViewData rightData = board.getViewData();
            boolean touchesRight = checkTouchesRightWall(rightData, 10);
            if (touchesRight) {
                rightWallTouches++;
            }

            System.out.println("Brick " + (i+1) + ": Left wall=" + (touchesLeft ? "✓" : "✗") +
                             ", Right wall=" + (touchesRight ? "✓" : "✗"));
        }

        System.out.println("\n========================================");
        System.out.println("RESULTS:");
        System.out.println("Left wall touches:  " + leftWallTouches + "/10");
        System.out.println("Right wall touches: " + rightWallTouches + "/10");
        System.out.println("========================================\n");

        // All bricks should be able to reach walls
        assertEquals(10, leftWallTouches, "All bricks should reach left wall");
        assertEquals(10, rightWallTouches, "All bricks should reach right wall");

        System.out.println("✓ SUCCESS! All bricks can now reach both walls!\n");
    }

    private boolean checkTouchesLeftWall(ViewData data) {
        int[][] shape = data.getBrickData();
        int xOffset = data.getXPosition();

        // Find leftmost filled cell
        int leftmostCol = Integer.MAX_VALUE;
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0 && col < leftmostCol) {
                    leftmostCol = col;
                }
            }
        }

        if (leftmostCol == Integer.MAX_VALUE) {
            return false;
        }

        int boardColumn = xOffset + leftmostCol;
        return boardColumn == 0;
    }

    private boolean checkTouchesRightWall(ViewData data, int boardWidth) {
        int[][] shape = data.getBrickData();
        int xOffset = data.getXPosition();

        // Find rightmost filled cell
        int rightmostCol = -1;
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0 && col > rightmostCol) {
                    rightmostCol = col;
                }
            }
        }

        if (rightmostCol == -1) {
            return false;
        }

        int boardColumn = xOffset + rightmostCol;
        return boardColumn == boardWidth - 1;
    }
}

