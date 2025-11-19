package com.comp2042.tetris.utils;

import com.comp2042.tetris.model.board.ClearRow;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatrixOperationsTest {

    @Test
    void intersectReturnsTrueWhenBrickHitsOccupiedCell() {
        int[][] board = {
                {0, 0, 0},
                {0, 2, 0},
                {0, 0, 0}
        };
        int[][] brick = {
                {1}
        };

        assertTrue(MatrixOperations.intersect(board, brick, 1, 1));
    }

    @Test
    void intersectReturnsTrueWhenBrickLeavesBoardBounds() {
        int[][] board = new int[2][2];
        int[][] brick = {
                {1, 1}
        };

        assertTrue(MatrixOperations.intersect(board, brick, 1, 0));
    }

    @Test
    void intersectReturnsFalseWhenPlacementIsValid() {
        int[][] board = new int[4][4];
        int[][] brick = {
                {0, 1},
                {1, 1}
        };

        assertFalse(MatrixOperations.intersect(board, brick, 1, 1));
    }

    @Test
    void mergeCopiesBrickIntoBoardPositions() {
        int[][] board = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, 0}
        };
        int[][] brick = {
                {4, 0},
                {4, 4}
        };

        int[][] merged = MatrixOperations.merge(board, brick, 1, 0);

        assertArrayEquals(new int[]{0, 4, 0}, merged[0]);
        assertArrayEquals(new int[]{0, 4, 4}, merged[1]);
        assertArrayEquals(new int[]{0, 0, 0}, merged[2]);
        assertArrayEquals(new int[]{0, 0, 0}, board[0], "Original board must remain unchanged");
    }

    @Test
    void checkRemovingReturnsClearedRowDataAndScore() {
        int[][] board = {
                {1, 1, 1},
                {0, 0, 0},
                {2, 2, 2},
                {0, 3, 0}
        };

        ClearRow clearRow = MatrixOperations.checkRemoving(board);

        assertEquals(2, clearRow.linesRemoved());
        assertEquals(50 * 2 * 2, clearRow.scoreBonus());
        assertArrayEquals(new int[]{0, 0, 0}, clearRow.newMatrix()[0]);
        assertArrayEquals(new int[]{0, 0, 0}, clearRow.newMatrix()[1]);
        assertArrayEquals(new int[]{0, 0, 0}, clearRow.newMatrix()[2]);
        assertArrayEquals(new int[]{0, 3, 0}, clearRow.newMatrix()[3]);
    }

    @Test
    void checkRemovingReturnsZeroWhenNoRowsCleared() {
        int[][] board = {
                {0, 1},
                {1, 0}
        };

        ClearRow clearRow = MatrixOperations.checkRemoving(board);

        assertEquals(0, clearRow.linesRemoved());
        assertEquals(0, clearRow.scoreBonus());
        int[][] resultingMatrix = clearRow.newMatrix();
        assertArrayEquals(new int[]{0, 1}, resultingMatrix[0]);
        assertArrayEquals(new int[]{1, 0}, resultingMatrix[1]);
    }

    @Test
    void deepCopyListReturnsIndependentCopies() {
        int[][] originalMatrix = {
                {1, 0},
                {0, 1}
        };
        List<int[][]> list = new java.util.ArrayList<>();
        list.add(MatrixOperations.copy(originalMatrix));

        List<int[][]> copy = MatrixOperations.deepCopyList(list);

        assertNotSame(list.get(0), copy.get(0));
        copy.get(0)[0][0] = 9;
        assertEquals(1, list.get(0)[0][0]);
    }

    @Test
    void intersectReturnsTrueWhenBrickHitsLeftBoundary() {
        int[][] board = new int[4][4];
        int[][] brick = {
                {1}
        };

        assertTrue(MatrixOperations.intersect(board, brick, -1, 0));
    }

    @Test
    void intersectReturnsTrueWhenBrickHitsRightBoundary() {
        int[][] board = new int[4][4];
        int[][] brick = {
                {1, 1}
        };

        assertTrue(MatrixOperations.intersect(board, brick, 3, 0));
    }

    @Test
    void intersectReturnsTrueWhenBrickHitsTopBoundary() {
        int[][] board = new int[4][4];
        int[][] brick = {
                {1}
        };

        assertTrue(MatrixOperations.intersect(board, brick, 0, -1));
    }

    @Test
    void mergePreservesExistingBackgroundBlocks() {
        int[][] board = {
                {5, 5, 5},
                {5, 5, 5},
                {5, 5, 5}
        };
        int[][] brick = {
                {0, 1, 0},
                {1, 1, 1},
                {0, 0, 0}
        };

        int[][] merged = MatrixOperations.merge(board, brick, 0, 0);

        assertArrayEquals(new int[]{5, 1, 5}, merged[0]);
        assertArrayEquals(new int[]{1, 1, 1}, merged[1]);
        assertArrayEquals(new int[]{5, 5, 5}, merged[2]);
    }

    @Test
    void checkRemovingOneLineCleared() {
        int[][] board = {
                {3, 3, 3, 0},
                {1, 1, 1, 1}
        };

        ClearRow clearRow = MatrixOperations.checkRemoving(board);

        assertEquals(1, clearRow.linesRemoved());
        assertEquals(50, clearRow.scoreBonus());
        assertArrayEquals(new int[]{0, 0, 0, 0}, clearRow.newMatrix()[0]);
        assertArrayEquals(new int[]{3, 3, 3, 0}, clearRow.newMatrix()[1]);
    }

    @Test
    void checkRemovingFourLinesCleared() {
        int[][] board = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {2, 2, 2, 2},
                {3, 3, 3, 3},
                {4, 4, 4, 4}
        };

        ClearRow clearRow = MatrixOperations.checkRemoving(board);

        assertEquals(4, clearRow.linesRemoved());
        assertEquals(800, clearRow.scoreBonus());
        assertEquals(5, clearRow.newMatrix().length);
        assertArrayEquals(new int[]{0, 0, 0, 0}, clearRow.newMatrix()[0]);
        assertArrayEquals(new int[]{0, 0, 0, 0}, clearRow.newMatrix()[1]);
        assertArrayEquals(new int[]{0, 0, 0, 0}, clearRow.newMatrix()[2]);
        assertArrayEquals(new int[]{0, 0, 0, 0}, clearRow.newMatrix()[3]);
        assertArrayEquals(new int[]{0, 0, 0, 0}, clearRow.newMatrix()[4]);
    }
}
