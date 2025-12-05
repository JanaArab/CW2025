package com.comp2042.tetris.model.event;

import com.comp2042.tetris.model.data.ViewData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GameStateSnapshotTest {

    private static final int[][] SAMPLE_BOARD = {{0, 1}, {2, 3}};
    private static final int[][] SAMPLE_BRICK = {{1, 1}, {1, 0}};
    private static final int[][] SAMPLE_NEXT = {{2, 2}};

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor sets boardMatrix")
        void constructor_setsBoardMatrix() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, SAMPLE_NEXT);
            GameStateSnapshot snapshot = new GameStateSnapshot(SAMPLE_BOARD, viewData);

            assertArrayEquals(SAMPLE_BOARD, snapshot.boardMatrix());
        }

        @Test
        @DisplayName("Constructor sets viewData")
        void constructor_setsViewData() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 5, 10, SAMPLE_NEXT);
            GameStateSnapshot snapshot = new GameStateSnapshot(SAMPLE_BOARD, viewData);

            assertSame(viewData, snapshot.viewData());
        }
    }

    @Nested
    @DisplayName("Accessor Tests")
    class AccessorTests {

        @Test
        @DisplayName("boardMatrix returns correct reference")
        void boardMatrix_returnsCorrectReference() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, SAMPLE_NEXT);
            GameStateSnapshot snapshot = new GameStateSnapshot(SAMPLE_BOARD, viewData);

            assertSame(SAMPLE_BOARD, snapshot.boardMatrix());
        }

        @Test
        @DisplayName("viewData returns correct reference")
        void viewData_returnsCorrectReference() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 3, 7, SAMPLE_NEXT);
            GameStateSnapshot snapshot = new GameStateSnapshot(SAMPLE_BOARD, viewData);

            ViewData retrieved = snapshot.viewData();
            assertEquals(3, retrieved.getXPosition());
            assertEquals(7, retrieved.getYPosition());
        }
    }

    @Nested
    @DisplayName("ViewData Content Tests")
    class ViewDataContentTests {

        @Test
        @DisplayName("ViewData position is accessible through snapshot")
        void viewData_positionAccessible() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 4, 8, SAMPLE_NEXT);
            GameStateSnapshot snapshot = new GameStateSnapshot(SAMPLE_BOARD, viewData);

            assertEquals(4, snapshot.viewData().getXPosition());
            assertEquals(8, snapshot.viewData().getYPosition());
        }

        @Test
        @DisplayName("ViewData brick data is accessible through snapshot")
        void viewData_brickDataAccessible() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, SAMPLE_NEXT);
            GameStateSnapshot snapshot = new GameStateSnapshot(SAMPLE_BOARD, viewData);

            int[][] brickData = snapshot.viewData().getBrickData();
            assertEquals(2, brickData.length);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Empty board matrix is allowed")
        void emptyBoardMatrix_isAllowed() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, SAMPLE_NEXT);
            GameStateSnapshot snapshot = new GameStateSnapshot(new int[0][0], viewData);

            assertEquals(0, snapshot.boardMatrix().length);
        }

        @Test
        @DisplayName("Large board matrix works")
        void largeBoardMatrix_works() {
            int[][] largeBoard = new int[20][10];
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, SAMPLE_NEXT);
            GameStateSnapshot snapshot = new GameStateSnapshot(largeBoard, viewData);

            assertEquals(20, snapshot.boardMatrix().length);
            assertEquals(10, snapshot.boardMatrix()[0].length);
        }
    }

    @Nested
    @DisplayName("Record Behavior Tests")
    class RecordBehaviorTests {

        @Test
        @DisplayName("Same instance equals itself")
        void sameInstance_equalsItself() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, SAMPLE_NEXT);
            GameStateSnapshot snapshot = new GameStateSnapshot(SAMPLE_BOARD, viewData);

            assertEquals(snapshot, snapshot);
        }

        @Test
        @DisplayName("toString contains useful information")
        void toString_containsUsefulInfo() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, SAMPLE_NEXT);
            GameStateSnapshot snapshot = new GameStateSnapshot(SAMPLE_BOARD, viewData);

            String str = snapshot.toString();
            assertNotNull(str);
            assertTrue(str.contains("GameStateSnapshot"));
        }
    }
}

