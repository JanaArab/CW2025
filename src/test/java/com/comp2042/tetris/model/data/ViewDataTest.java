package com.comp2042.tetris.model.data;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ViewDataTest {

    private static final int[][] SAMPLE_BRICK = {{1, 1}, {1, 0}};
    private static final int[][] SAMPLE_NEXT_BRICK = {{2, 2}, {0, 2}};

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Basic constructor sets all fields correctly")
        void basicConstructor_setsAllFields() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 5, 10, SAMPLE_NEXT_BRICK);

            assertEquals(5, viewData.getXPosition());
            assertEquals(10, viewData.getYPosition());
            assertEquals(10, viewData.getGhostYPosition(), "Ghost Y should default to Y position");
        }

        @Test
        @DisplayName("Constructor with ghost position sets ghost correctly")
        void constructorWithGhost_setsGhostPosition() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 5, 10, SAMPLE_NEXT_BRICK, 15);

            assertEquals(10, viewData.getYPosition());
            assertEquals(15, viewData.getGhostYPosition());
        }

        @Test
        @DisplayName("Constructor with rotation metadata preserves values")
        void constructorWithRotationMetadata_preservesValues() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 5, 10, SAMPLE_NEXT_BRICK, 15, 2, 4, 3);

            assertEquals(2, viewData.getCurrentRotationIndex());
            assertEquals(4, viewData.getRotationStateCount());
            assertEquals(3, viewData.getRotationsUsed());
        }

        @Test
        @DisplayName("Constructor with List of next bricks works correctly")
        void constructorWithListOfNextBricks_worksCorrectly() {
            List<int[][]> nextBricks = Arrays.asList(SAMPLE_NEXT_BRICK, SAMPLE_BRICK);
            ViewData viewData = new ViewData(SAMPLE_BRICK, 5, 10, nextBricks, 15);

            List<int[][]> result = viewData.getNextBricksData();
            assertEquals(2, result.size());
        }
    }

    @Nested
    @DisplayName("Defensive Copy Tests")
    class DefensiveCopyTests {

        @Test
        @DisplayName("getBrickData returns defensive copy")
        void getBrickData_returnsDefensiveCopy() {
            int[][] original = {{1, 1}, {1, 0}};
            ViewData viewData = new ViewData(original, 0, 0, SAMPLE_NEXT_BRICK);

            int[][] retrieved = viewData.getBrickData();
            retrieved[0][0] = 999;

            // Original should not be affected
            int[][] retrievedAgain = viewData.getBrickData();
            assertEquals(1, retrievedAgain[0][0], "Modifying retrieved data should not affect ViewData");
        }

        @Test
        @DisplayName("getNextBricksData returns defensive copies")
        void getNextBricksData_returnsDefensiveCopies() {
            List<int[][]> nextBricks = Arrays.asList(
                new int[][]{{1, 1}, {0, 0}},
                new int[][]{{2, 2}, {0, 0}}
            );
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, nextBricks, 0);

            List<int[][]> retrieved = viewData.getNextBricksData();
            retrieved.get(0)[0][0] = 999;

            // Original should not be affected
            List<int[][]> retrievedAgain = viewData.getNextBricksData();
            assertEquals(1, retrievedAgain.get(0)[0][0]);
        }

        @Test
        @DisplayName("getNextBricksData returns independent list")
        void getNextBricksData_returnsIndependentList() {
            List<int[][]> nextBricks = new java.util.ArrayList<>();
            nextBricks.add(SAMPLE_NEXT_BRICK);
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, nextBricks, 0);

            List<int[][]> retrieved = viewData.getNextBricksData();
            assertNotSame(nextBricks, retrieved);
        }
    }

    @Nested
    @DisplayName("Position Tests")
    class PositionTests {

        @Test
        @DisplayName("Negative positions are allowed")
        void negativePositions_areAllowed() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, -5, -10, SAMPLE_NEXT_BRICK, -15);

            assertEquals(-5, viewData.getXPosition());
            assertEquals(-10, viewData.getYPosition());
            assertEquals(-15, viewData.getGhostYPosition());
        }

        @Test
        @DisplayName("Zero positions are allowed")
        void zeroPositions_areAllowed() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, SAMPLE_NEXT_BRICK, 0);

            assertEquals(0, viewData.getXPosition());
            assertEquals(0, viewData.getYPosition());
            assertEquals(0, viewData.getGhostYPosition());
        }

        @Test
        @DisplayName("Large positions are allowed")
        void largePositions_areAllowed() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 1000, 2000, SAMPLE_NEXT_BRICK, 3000);

            assertEquals(1000, viewData.getXPosition());
            assertEquals(2000, viewData.getYPosition());
            assertEquals(3000, viewData.getGhostYPosition());
        }
    }

    @Nested
    @DisplayName("Rotation Metadata Tests")
    class RotationMetadataTests {

        @Test
        @DisplayName("Default rotation index is 0")
        void defaultRotationIndex_isZero() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, SAMPLE_NEXT_BRICK);
            assertEquals(0, viewData.getCurrentRotationIndex());
        }

        @Test
        @DisplayName("Default rotation state count is 1")
        void defaultRotationStateCount_isOne() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, SAMPLE_NEXT_BRICK);
            assertEquals(1, viewData.getRotationStateCount());
        }

        @Test
        @DisplayName("Default rotations used is 0")
        void defaultRotationsUsed_isZero() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, SAMPLE_NEXT_BRICK);
            assertEquals(0, viewData.getRotationsUsed());
        }

        @Test
        @DisplayName("Custom rotation metadata is preserved")
        void customRotationMetadata_isPreserved() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, SAMPLE_NEXT_BRICK, 0, 3, 4, 7);

            assertEquals(3, viewData.getCurrentRotationIndex());
            assertEquals(4, viewData.getRotationStateCount());
            assertEquals(7, viewData.getRotationsUsed());
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Empty next bricks list is handled")
        void emptyNextBricksList_isHandled() {
            List<int[][]> emptyList = Collections.emptyList();
            ViewData viewData = new ViewData(SAMPLE_BRICK, 0, 0, emptyList, 0);

            List<int[][]> result = viewData.getNextBricksData();
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Single cell brick works correctly")
        void singleCellBrick_worksCorrectly() {
            int[][] singleCell = {{1}};
            ViewData viewData = new ViewData(singleCell, 0, 0, singleCell);

            int[][] retrieved = viewData.getBrickData();
            assertEquals(1, retrieved.length);
            assertEquals(1, retrieved[0].length);
        }

        @Test
        @DisplayName("Ghost position below brick position is valid")
        void ghostBelowBrick_isValid() {
            ViewData viewData = new ViewData(SAMPLE_BRICK, 5, 5, SAMPLE_NEXT_BRICK, 18);

            assertEquals(5, viewData.getYPosition());
            assertEquals(18, viewData.getGhostYPosition());
            assertTrue(viewData.getGhostYPosition() > viewData.getYPosition());
        }
    }
}

