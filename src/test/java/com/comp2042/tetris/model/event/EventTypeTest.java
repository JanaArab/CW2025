package com.comp2042.tetris.model.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTypeTest {

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @Test
        @DisplayName("DOWN value exists")
        void down_exists() {
            assertNotNull(EventType.DOWN);
        }

        @Test
        @DisplayName("LEFT value exists")
        void left_exists() {
            assertNotNull(EventType.LEFT);
        }

        @Test
        @DisplayName("RIGHT value exists")
        void right_exists() {
            assertNotNull(EventType.RIGHT);
        }

        @Test
        @DisplayName("ROTATE value exists")
        void rotate_exists() {
            assertNotNull(EventType.ROTATE);
        }

        @Test
        @DisplayName("Exactly 4 event types exist")
        void exactlyFourTypesExist() {
            assertEquals(4, EventType.values().length);
        }
    }

    @Nested
    @DisplayName("Enum Operations Tests")
    class EnumOperationsTests {

        @Test
        @DisplayName("valueOf returns correct enum for DOWN")
        void valueOf_down_returnsCorrect() {
            assertEquals(EventType.DOWN, EventType.valueOf("DOWN"));
        }

        @Test
        @DisplayName("valueOf returns correct enum for LEFT")
        void valueOf_left_returnsCorrect() {
            assertEquals(EventType.LEFT, EventType.valueOf("LEFT"));
        }

        @Test
        @DisplayName("valueOf returns correct enum for RIGHT")
        void valueOf_right_returnsCorrect() {
            assertEquals(EventType.RIGHT, EventType.valueOf("RIGHT"));
        }

        @Test
        @DisplayName("valueOf returns correct enum for ROTATE")
        void valueOf_rotate_returnsCorrect() {
            assertEquals(EventType.ROTATE, EventType.valueOf("ROTATE"));
        }

        @Test
        @DisplayName("valueOf throws for invalid name")
        void valueOf_invalidName_throws() {
            assertThrows(IllegalArgumentException.class,
                () -> EventType.valueOf("INVALID"));
        }

        @Test
        @DisplayName("name returns correct string")
        void name_returnsCorrectString() {
            assertEquals("DOWN", EventType.DOWN.name());
            assertEquals("LEFT", EventType.LEFT.name());
            assertEquals("RIGHT", EventType.RIGHT.name());
            assertEquals("ROTATE", EventType.ROTATE.name());
        }

        @Test
        @DisplayName("ordinal values are sequential")
        void ordinal_valuesAreSequential() {
            EventType[] values = EventType.values();
            for (int i = 0; i < values.length; i++) {
                assertEquals(i, values[i].ordinal());
            }
        }
    }
}

