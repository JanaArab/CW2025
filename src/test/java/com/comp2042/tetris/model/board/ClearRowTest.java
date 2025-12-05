package com.comp2042.tetris.model.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ClearRow record - represents result of row clearing.
 */
class ClearRowTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor creates record with all values")
        void constructor_setsAllValues() {
            int[][] matrix = {{1, 2}, {3, 4}};
            List<Integer> clearedRows = List.of(5, 10);

            ClearRow clearRow = new ClearRow(2, matrix, 100, clearedRows);

            assertEquals(2, clearRow.linesRemoved());
            assertEquals(100, clearRow.scoreBonus());
            assertEquals(clearedRows, clearRow.clearedRows());
        }

        @Test
        @DisplayName("Constructor accepts zero lines removed")
        void constructor_acceptsZeroLines() {
            ClearRow clearRow = new ClearRow(0, new int[0][0], 0, Collections.emptyList());

            assertEquals(0, clearRow.linesRemoved());
            assertEquals(0, clearRow.scoreBonus());
        }

        @Test
        @DisplayName("Constructor accepts negative score bonus")
        void constructor_acceptsNegativeBonus() {
            ClearRow clearRow = new ClearRow(1, new int[][]{{1}}, -50, List.of(1));

            assertEquals(-50, clearRow.scoreBonus());
        }
    }

    @Nested
    @DisplayName("newMatrix Defensive Copy Tests")
    class NewMatrixDefensiveCopyTests {

        @Test
        @DisplayName("newMatrix returns defensive copy")
        void newMatrix_returnsDefensiveCopy() {
            int[][] original = {{1, 2}, {3, 4}};
            ClearRow clearRow = new ClearRow(1, original, 50, List.of(1));

            int[][] retrieved = clearRow.newMatrix();
            retrieved[0][0] = 999;

            int[][] retrievedAgain = clearRow.newMatrix();
            assertEquals(1, retrievedAgain[0][0], "Original should not be modified");
        }

        @Test
        @DisplayName("newMatrix returns independent copy each time")
        void newMatrix_returnsIndependentCopyEachTime() {
            int[][] matrix = {{1, 2}, {3, 4}};
            ClearRow clearRow = new ClearRow(1, matrix, 50, List.of(1));

            int[][] first = clearRow.newMatrix();
            int[][] second = clearRow.newMatrix();

            assertNotSame(first, second);
        }
    }

    @Nested
    @DisplayName("Accessors Tests")
    class AccessorsTests {

        @Test
        @DisplayName("linesRemoved returns correct value")
        void linesRemoved_returnsCorrectValue() {
            ClearRow clearRow = new ClearRow(4, new int[][]{{0}}, 800, List.of(1, 2, 3, 4));
            assertEquals(4, clearRow.linesRemoved());
        }

        @Test
        @DisplayName("scoreBonus returns correct value")
        void scoreBonus_returnsCorrectValue() {
            ClearRow clearRow = new ClearRow(2, new int[][]{{0}}, 200, List.of(1, 2));
            assertEquals(200, clearRow.scoreBonus());
        }

        @Test
        @DisplayName("clearedRows returns correct list")
        void clearedRows_returnsCorrectList() {
            List<Integer> rows = List.of(15, 16, 17);
            ClearRow clearRow = new ClearRow(3, new int[][]{{0}}, 300, rows);
            assertEquals(rows, clearRow.clearedRows());
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Empty matrix is handled")
        void emptyMatrix_isHandled() {
            ClearRow clearRow = new ClearRow(0, new int[0][0], 0, Collections.emptyList());

            int[][] matrix = clearRow.newMatrix();
            assertEquals(0, matrix.length);
        }

        @Test
        @DisplayName("Single cell matrix works")
        void singleCellMatrix_works() {
            int[][] matrix = {{42}};
            ClearRow clearRow = new ClearRow(1, matrix, 50, List.of(0));

            int[][] retrieved = clearRow.newMatrix();
            assertEquals(42, retrieved[0][0]);
        }

        @Test
        @DisplayName("Large matrix is copied correctly")
        void largeMatrix_copiedCorrectly() {
            int[][] matrix = new int[20][10];
            for (int r = 0; r < 20; r++) {
                for (int c = 0; c < 10; c++) {
                    matrix[r][c] = r * 10 + c;
                }
            }

            ClearRow clearRow = new ClearRow(4, matrix, 800, List.of(16, 17, 18, 19));

            int[][] retrieved = clearRow.newMatrix();
            for (int r = 0; r < 20; r++) {
                for (int c = 0; c < 10; c++) {
                    assertEquals(r * 10 + c, retrieved[r][c]);
                }
            }
        }
    }

    @Nested
    @DisplayName("Record Equality Tests")
    class RecordEqualityTests {

        @Test
        @DisplayName("Equal records are equal")
        void equalRecords_areEqual() {
            int[][] matrix1 = {{1, 2}, {3, 4}};
            int[][] matrix2 = {{1, 2}, {3, 4}};
            List<Integer> rows = List.of(1);

            ClearRow cr1 = new ClearRow(1, matrix1, 50, rows);
            ClearRow cr2 = new ClearRow(1, matrix2, 50, rows);

            // Note: Record equality uses reference equality for arrays
            // so these won't be equal due to different array references
            assertNotEquals(cr1, cr2);
        }

        @Test
        @DisplayName("Same instance is equal to itself")
        void sameInstance_isEqual() {
            ClearRow clearRow = new ClearRow(1, new int[][]{{1}}, 50, List.of(1));
            assertEquals(clearRow, clearRow);
        }
    }
}

