package com.comp2042.tetris.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ScoreThresholdDetectorTest {

    @Nested
    @DisplayName("Valid Threshold Crossing Tests")
    class ValidThresholdCrossingTests {

        @Test
        @DisplayName("Single threshold crossed returns one element")
        void determineCrossedThresholds_singleThreshold_returnsOne() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(400, 600, 500);
            assertEquals(List.of(500), result);
        }

        @Test
        @DisplayName("Multiple thresholds crossed returns all")
        void determineCrossedThresholds_multipleThresholds_returnsAll() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(450, 1550, 500);
            assertEquals(List.of(500, 1000, 1500), result);
        }

        @Test
        @DisplayName("Large score jump crosses many thresholds")
        void determineCrossedThresholds_largeJump_returnsManyThresholds() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(0, 2500, 500);
            assertEquals(List.of(500, 1000, 1500, 2000, 2500), result);
        }
    }

    @Nested
    @DisplayName("Empty Result Tests")
    class EmptyResultTests {

        @Test
        @DisplayName("No score change returns empty list")
        void determineCrossedThresholds_noChange_returnsEmpty() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(500, 500, 500);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Score decrease returns empty list")
        void determineCrossedThresholds_newScoreLessThanOld_returnsEmpty() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(1000, 500, 500);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Score increase within same threshold returns empty")
        void determineCrossedThresholds_withinSameThreshold_returnsEmpty() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(100, 200, 500);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Score just below next threshold returns empty")
        void determineCrossedThresholds_justBelowThreshold_returnsEmpty() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(400, 499, 500);
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("Exact Boundary Tests")
    class ExactBoundaryTests {

        @Test
        @DisplayName("New score exactly on threshold includes it")
        void determineCrossedThresholds_exactBoundary_includesThreshold() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(400, 500, 500);
            assertEquals(List.of(500), result);
        }

        @Test
        @DisplayName("Old score exactly on threshold excludes it")
        void determineCrossedThresholds_oldScoreOnBoundary_excludesIt() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(500, 999, 500);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Both scores on different thresholds")
        void determineCrossedThresholds_bothOnThresholds_correctRange() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(500, 1000, 500);
            assertEquals(List.of(1000), result);
        }

        @Test
        @DisplayName("Zero old score crosses first threshold")
        void determineCrossedThresholds_fromZero_crossesFirst() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(0, 500, 500);
            assertEquals(List.of(500), result);
        }
    }

    @Nested
    @DisplayName("Invalid Input Tests")
    class InvalidInputTests {

        @Test
        @DisplayName("Negative step throws IllegalArgumentException")
        void determineCrossedThresholds_negativeStep_throwsException() {
            assertThrows(IllegalArgumentException.class,
                () -> ScoreThresholdDetector.determineCrossedThresholds(0, 100, -500));
        }

        @Test
        @DisplayName("Zero step throws IllegalArgumentException")
        void determineCrossedThresholds_zeroStep_throwsException() {
            assertThrows(IllegalArgumentException.class,
                () -> ScoreThresholdDetector.determineCrossedThresholds(0, 100, 0));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Very small step with large range")
        void determineCrossedThresholds_smallStep_manyResults() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(0, 100, 10);
            assertEquals(10, result.size());
            assertEquals(List.of(10, 20, 30, 40, 50, 60, 70, 80, 90, 100), result);
        }

        @Test
        @DisplayName("Step larger than score difference")
        void determineCrossedThresholds_stepLargerThanDiff_mayBeEmpty() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(0, 50, 1000);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Step of 1 returns every integer crossed")
        void determineCrossedThresholds_stepOfOne_returnsAllIntegers() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(5, 8, 1);
            assertEquals(List.of(6, 7, 8), result);
        }
    }

    @Nested
    @DisplayName("Realistic Game Scenario Tests")
    class RealisticGameScenarioTests {

        @Test
        @DisplayName("Level up every 1000 points")
        void determineCrossedThresholds_levelUpScenario() {
            // Player scores from 2500 to 4200 (crosses 3000 and 4000)
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(2500, 4200, 1000);
            assertEquals(List.of(3000, 4000), result);
        }

        @Test
        @DisplayName("Speed increase every 100 points up to 500")
        void determineCrossedThresholds_speedIncreaseScenario() {
            // Player scores from 50 to 350
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(50, 350, 100);
            assertEquals(List.of(100, 200, 300), result);
        }
    }

    @Nested
    @DisplayName("Return Value Integrity Tests")
    class ReturnValueIntegrityTests {

        @Test
        @DisplayName("Returned list is mutable")
        void determineCrossedThresholds_returnedListIsMutable() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(0, 500, 500);
            assertDoesNotThrow(() -> result.add(999));
        }

        @Test
        @DisplayName("Returned values are in ascending order")
        void determineCrossedThresholds_valuesInAscendingOrder() {
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(0, 2000, 500);
            for (int i = 0; i < result.size() - 1; i++) {
                assertTrue(result.get(i) < result.get(i + 1));
            }
        }

        @Test
        @DisplayName("All returned values are multiples of step")
        void determineCrossedThresholds_allValuesAreMultiplesOfStep() {
            int step = 500;
            List<Integer> result = ScoreThresholdDetector.determineCrossedThresholds(0, 2500, step);
            for (int value : result) {
                assertEquals(0, value % step, "Value " + value + " is not a multiple of " + step);
            }
        }
    }
}
