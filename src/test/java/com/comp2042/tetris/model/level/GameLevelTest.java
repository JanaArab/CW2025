package com.comp2042.tetris.model.level;

import javafx.util.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GameLevelTest {

    @Nested
    @DisplayName("ClassicLevel Tests")
    class ClassicLevelTests {

        private final ClassicLevel classicLevel = new ClassicLevel();

        @Test
        @DisplayName("getName returns 'Classic'")
        void getName_returnsClassic() {
            assertEquals("Classic", classicLevel.getName());
        }

        @Test
        @DisplayName("getTickInterval remains constant regardless of score")
        void getTickInterval_remainsConstant() {
            Duration atZero = classicLevel.getTickInterval(0);
            Duration atThousand = classicLevel.getTickInterval(1000);
            Duration atMillion = classicLevel.getTickInterval(1000000);

            assertEquals(atZero, atThousand);
            assertEquals(atThousand, atMillion);
        }

        @Test
        @DisplayName("Default rotation limit is unlimited (-1)")
        void getRotationLimit_returnsUnlimited() {
            assertEquals(-1, classicLevel.getRotationLimit());
        }

        @Test
        @DisplayName("Garbage is disabled by default")
        void isGarbageEnabled_returnsFalse() {
            assertFalse(classicLevel.isGarbageEnabled());
        }

        @Test
        @DisplayName("Flicker is disabled by default")
        void isFlickerEnabled_returnsFalse() {
            assertFalse(classicLevel.isFlickerEnabled());
        }

        @Test
        @DisplayName("Next brick is visible by default")
        void isNextBrickHidden_returnsFalse() {
            assertFalse(classicLevel.isNextBrickHidden());
        }

        @Test
        @DisplayName("Controls are not inverted by default")
        void areControlsInverted_returnsFalse() {
            assertFalse(classicLevel.areControlsInverted());
        }
    }

    @Nested
    @DisplayName("Level1 Tests (DynamicLevel)")
    class Level1Tests {

        private final Level1 level1 = new Level1();

        @Test
        @DisplayName("getName returns 'Level 1'")
        void getName_returnsLevel1() {
            assertEquals("Level 1", level1.getName());
        }

        @Test
        @DisplayName("getTickInterval decreases as score increases")
        void getTickInterval_decreasesWithScore() {
            Duration atZero = level1.getTickInterval(0);
            Duration atHighScore = level1.getTickInterval(500);

            assertTrue(atHighScore.toMillis() < atZero.toMillis(),
                "Interval should decrease (speed up) as score increases");
        }

        @Test
        @DisplayName("getTickInterval caps at max score")
        void getTickInterval_capsAtMaxScore() {
            Duration atMax = level1.getTickInterval(500);
            Duration beyondMax = level1.getTickInterval(10000);

            assertEquals(atMax, beyondMax,
                "Interval should not change beyond max score threshold");
        }

        @Test
        @DisplayName("getTickInterval returns positive duration")
        void getTickInterval_alwaysPositive() {
            Duration interval = level1.getTickInterval(1000000);
            assertTrue(interval.toMillis() > 0, "Interval must always be positive");
        }

        @Test
        @DisplayName("Speed increases at each threshold")
        void getTickInterval_increasesAtThresholds() {
            Duration at0 = level1.getTickInterval(0);
            Duration at100 = level1.getTickInterval(100);
            Duration at200 = level1.getTickInterval(200);

            assertTrue(at100.toMillis() < at0.toMillis());
            assertTrue(at200.toMillis() < at100.toMillis());
        }
    }

    @Nested
    @DisplayName("Level2 Tests")
    class Level2Tests {

        private final Level2 level2 = new Level2();

        @Test
        @DisplayName("getName returns 'Level 2'")
        void getName_returnsLevel2() {
            assertEquals("Level 2", level2.getName());
        }

        @Test
        @DisplayName("getRotationLimit returns 4")
        void getRotationLimit_returnsFour() {
            assertEquals(4, level2.getRotationLimit());
        }

        @Test
        @DisplayName("Inherits constant speed from ClassicLevel")
        void getTickInterval_remainsConstant() {
            Duration atZero = level2.getTickInterval(0);
            Duration atHighScore = level2.getTickInterval(10000);
            assertEquals(atZero, atHighScore);
        }

        @Test
        @DisplayName("Garbage is still disabled")
        void isGarbageEnabled_returnsFalse() {
            assertFalse(level2.isGarbageEnabled());
        }
    }

    @Nested
    @DisplayName("Level3 Tests")
    class Level3Tests {

        private final Level3 level3 = new Level3();

        @Test
        @DisplayName("getName returns 'Level 3'")
        void getName_returnsLevel3() {
            assertEquals("Level 3", level3.getName());
        }

        @Test
        @DisplayName("isGarbageEnabled returns true")
        void isGarbageEnabled_returnsTrue() {
            assertTrue(level3.isGarbageEnabled());
        }

        @Test
        @DisplayName("isFlickerEnabled returns true")
        void isFlickerEnabled_returnsTrue() {
            assertTrue(level3.isFlickerEnabled());
        }

        @Test
        @DisplayName("isNextBrickHidden returns true")
        void isNextBrickHidden_returnsTrue() {
            assertTrue(level3.isNextBrickHidden());
        }

        @Test
        @DisplayName("areControlsInverted returns true")
        void areControlsInverted_returnsTrue() {
            assertTrue(level3.areControlsInverted());
        }

        @Test
        @DisplayName("Uses constant speed despite difficulty features")
        void getTickInterval_remainsConstant() {
            Duration atZero = level3.getTickInterval(0);
            Duration atHighScore = level3.getTickInterval(10000);
            assertEquals(atZero, atHighScore);
        }

        @Test
        @DisplayName("Rotation limit is unlimited")
        void getRotationLimit_isUnlimited() {
            assertEquals(-1, level3.getRotationLimit());
        }
    }

    @Nested
    @DisplayName("DynamicLevel Base Behavior Tests")
    class DynamicLevelTests {

        @Test
        @DisplayName("Speed calculation formula is correct")
        void getTickInterval_formulaCorrect() {
            // Create a testable dynamic level
            DynamicLevel testLevel = new DynamicLevel(2.0, 1.0, 100, 300) {
                @Override
                public String getName() {
                    return "Test Level";
                }
            };

            // At score 0: speed = 2.0 Hz => interval = 500ms
            Duration at0 = testLevel.getTickInterval(0);
            assertEquals(500, at0.toMillis(), 0.1);

            // At score 100: speed = 2.0 + 1.0 = 3.0 Hz => interval = 333.33ms
            Duration at100 = testLevel.getTickInterval(100);
            assertEquals(333.33, at100.toMillis(), 1.0);

            // At score 200: speed = 2.0 + 2.0 = 4.0 Hz => interval = 250ms
            Duration at200 = testLevel.getTickInterval(200);
            assertEquals(250, at200.toMillis(), 0.1);

            // At score 300 (cap): speed = 2.0 + 3.0 = 5.0 Hz => interval = 200ms
            Duration at300 = testLevel.getTickInterval(300);
            assertEquals(200, at300.toMillis(), 0.1);

            // Beyond cap should remain at cap
            Duration at1000 = testLevel.getTickInterval(1000);
            assertEquals(200, at1000.toMillis(), 0.1);
        }

        @Test
        @DisplayName("Negative score results in slower speed due to negative increments")
        void getTickInterval_negativeScoreCalculatesNegativeIncrements() {
            DynamicLevel testLevel = new DynamicLevel(2.0, 1.0, 100, 300) {
                @Override
                public String getName() {
                    return "Test Level";
                }
            };

            Duration atNegative = testLevel.getTickInterval(-100);
            Duration atZero = testLevel.getTickInterval(0);

            // Negative score leads to negative increments, resulting in slower speed
            // At -100: increments = -1, speed = 2.0 + (-1) = 1.0 Hz, interval = 1000ms
            // At 0: increments = 0, speed = 2.0 Hz, interval = 500ms
            assertTrue(atNegative.toMillis() > atZero.toMillis(),
                "Negative score should result in slower speed (longer interval)");
        }
    }

    @Nested
    @DisplayName("GameLevel Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("All levels return non-null name")
        void allLevels_returnNonNullName() {
            GameLevel[] levels = {new ClassicLevel(), new Level1(), new Level2(), new Level3()};
            for (GameLevel level : levels) {
                assertNotNull(level.getName(), level.getClass().getSimpleName() + " returned null name");
            }
        }

        @Test
        @DisplayName("All levels return non-null tick interval")
        void allLevels_returnNonNullTickInterval() {
            GameLevel[] levels = {new ClassicLevel(), new Level1(), new Level2(), new Level3()};
            for (GameLevel level : levels) {
                assertNotNull(level.getTickInterval(0), level.getClass().getSimpleName() + " returned null interval");
            }
        }

        @Test
        @DisplayName("All levels have positive tick interval")
        void allLevels_havePositiveTickInterval() {
            GameLevel[] levels = {new ClassicLevel(), new Level1(), new Level2(), new Level3()};
            for (GameLevel level : levels) {
                assertTrue(level.getTickInterval(0).toMillis() > 0,
                    level.getClass().getSimpleName() + " returned non-positive interval");
            }
        }
    }
}

