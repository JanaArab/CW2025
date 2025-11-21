package com.comp2042.tetris.model.bricks;

import org.junit.jupiter.api.Test;

import java.awt.Point;

import static org.junit.jupiter.api.Assertions.*;

class StandardRotationStrategyTest {

    private final StandardRotationStrategy strategy = new StandardRotationStrategy();

    @Test
    void returnsCurrentOffsetWhenNoCollisionDetected() {
        int[][] board = emptyBoard(4, 4);
        int[][] rotatedShape = {
                {1, 1},
                {1, 0}
        };
        Point current = new Point(1, 1);

        Point result = strategy.findOffsetForRotation(board, rotatedShape, current);

        assertEquals(current, result);
    }

    @Test
    void usesNegativeKickWhenLeftCellIsFree() {
        int[][] board = emptyBoard(4, 4);
        board[1][1] = 9; // block current offset
        int[][] rotatedShape = {
                {1}
        };

        Point result = strategy.findOffsetForRotation(board, rotatedShape, new Point(1, 1));

        assertEquals(new Point(0, 1), result);
    }

    @Test
    void usesPositiveKickWhenRightCellIsFree() {
        int[][] board = emptyBoard(4, 4);
        board[1][1] = 9; // block current
        board[1][0] = 8; // block left kick
        int[][] rotatedShape = {
                {1}
        };

        Point result = strategy.findOffsetForRotation(board, rotatedShape, new Point(1, 1));

        assertEquals(new Point(2, 1), result);
    }

    @Test
    void usesLargerNegativeKickWhenCloserOffsetsFail() {
        int[][] board = emptyBoard(3, 5);
        board[0][2] = 7; // current collision
        board[0][1] = 8; // -1 collision
        board[0][3] = 9; // +1 collision
        int[][] rotatedShape = {
                {1}
        };

        Point result = strategy.findOffsetForRotation(board, rotatedShape, new Point(2, 0));

        assertEquals(new Point(0, 0), result);
    }

    @Test
    void usesLargerPositiveKickWhenAllOtherOffsetsFail() {
        int[][] board = emptyBoard(3, 6);
        board[0][2] = 7; // current
        board[0][1] = 8; // -1 collision
        board[0][3] = 9; // +1 collision
        board[0][0] = 6; // -2 collision
        int[][] rotatedShape = {
                {1}
        };

        Point result = strategy.findOffsetForRotation(board, rotatedShape, new Point(2, 0));

        assertEquals(new Point(4, 0), result);
    }

    @Test
    void returnsNullWhenNoKickAvoidsCollision() {
        int[][] board = emptyBoard(3, 4);
        board[0][3] = 1; // current (x=3)
        board[0][2] = 1; // -1 collision
        board[0][1] = 1; // -2 collision
        // +1 and +2 result in out-of-bounds for 4-column board
        int[][] rotatedShape = {
                {1}
        };

        Point result = strategy.findOffsetForRotation(board, rotatedShape, new Point(3, 0));

        assertNull(result);
    }

    private static int[][] emptyBoard(int rows, int cols) {
        int[][] board = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                board[r][c] = 0;
            }
        }
        return board;
    }
}

