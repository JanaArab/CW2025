package com.comp2042.tetris.model.board;

import com.comp2042.tetris.model.bricks.BrickRotator;
import com.comp2042.tetris.model.data.ViewData;
import com.comp2042.tetris.model.score.Score;
import com.comp2042.tetris.utils.MatrixOperations;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Detailed tests to verify bricks can reach walls properly
 */
class DetailedWallCollisionTest {

    @Test
    void testBrickPositionsWhenMovingToWalls() {
        SimpleBoard board = new SimpleBoard(20, 10);

        for (int testRun = 0; testRun < 3; testRun++) {
            board.createNewBrick();
            ViewData initialData = board.getViewData();
            int initialX = initialData.getXPosition();

            System.out.println("\n=== Test Run " + testRun + " ===");
            System.out.println("Initial X position: " + initialX);
            printBrickOnBoard(board, "Initial position");

            // Move left until can't
            int leftMoves = 0;
            while (board.moveBrickLeft() && leftMoves < 20) {
                leftMoves++;
            }

            ViewData leftData = board.getViewData();
            System.out.println("\nAfter moving left " + leftMoves + " times, X position: " + leftData.getXPosition());
            printBrickOnBoard(board, "At left wall");

            // Verify the brick is actually at the left wall (touching column 0)
            int[][] shape = leftData.getBrickData();
            int leftX = leftData.getXPosition();
            boolean touchesLeftWall = checkIfTouchesLeftWall(shape, leftX);
            System.out.println("Touches left wall: " + touchesLeftWall);

            // Reset and test right
            board.createNewBrick();
            ViewData initialData2 = board.getViewData();
            System.out.println("\n--- Right Movement Test ---");
            System.out.println("Initial X position: " + initialData2.getXPosition());
            printBrickOnBoard(board, "Initial position");

            int rightMoves = 0;
            while (board.moveBrickRight() && rightMoves < 20) {
                rightMoves++;
            }

            ViewData rightData = board.getViewData();
            System.out.println("\nAfter moving right " + rightMoves + " times, X position: " + rightData.getXPosition());
            printBrickOnBoard(board, "At right wall");

            // Verify the brick is actually at the right wall (touching column 9)
            int rightX = rightData.getXPosition();
            boolean touchesRightWall = checkIfTouchesRightWall(shape, rightX, 10);
            System.out.println("Touches right wall: " + touchesRightWall);
        }
    }

    private void printBrickOnBoard(SimpleBoard board, String label) {
        ViewData data = board.getViewData();
        int[][] shape = data.getBrickData();
        int xOffset = data.getXPosition();
        int yOffset = data.getYPosition();

        System.out.println(label + ":");
        System.out.print("  Columns: ");
        for (int i = 0; i < 10; i++) {
            System.out.print(i);
        }
        System.out.println();

        // Print a few rows around the brick
        for (int row = Math.max(0, yOffset - 1); row < Math.min(10, yOffset + shape.length + 1); row++) {
            System.out.print("  Row " + row + ": ");
            for (int col = 0; col < 10; col++) {
                boolean hasBrick = false;
                if (row >= yOffset && row < yOffset + shape.length &&
                    col >= xOffset && col < xOffset + shape[0].length) {
                    int brickRow = row - yOffset;
                    int brickCol = col - xOffset;
                    if (shape[brickRow][brickCol] != 0) {
                        hasBrick = true;
                    }
                }

                System.out.print(hasBrick ? "X" : ".");
            }
            System.out.println();
        }
    }

    private boolean checkIfTouchesLeftWall(int[][] shape, int xOffset) {
        // Find the leftmost filled cell in the brick
        int leftmostCol = Integer.MAX_VALUE;
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0 && col < leftmostCol) {
                    leftmostCol = col;
                }
            }
        }

        if (leftmostCol == Integer.MAX_VALUE) {
            return false; // No filled cells?
        }

        // The leftmost brick cell should be at board column 0
        int boardColumn = xOffset + leftmostCol;
        System.out.println("  Leftmost brick cell at board column: " + boardColumn);
        return boardColumn == 0;
    }

    private boolean checkIfTouchesRightWall(int[][] shape, int xOffset, int boardWidth) {
        // Find the rightmost filled cell in the brick
        int rightmostCol = -1;
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] != 0 && col > rightmostCol) {
                    rightmostCol = col;
                }
            }
        }

        if (rightmostCol == -1) {
            return false; // No filled cells?
        }

        // The rightmost brick cell should be at board column 9 (boardWidth - 1)
        int boardColumn = xOffset + rightmostCol;
        System.out.println("  Rightmost brick cell at board column: " + boardColumn);
        return boardColumn == boardWidth - 1;
    }
}

