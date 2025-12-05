package com.comp2042.tetris.model.event;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class MoveEventTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor sets eventType correctly")
        void constructor_setsEventType() {
            MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
            assertEquals(EventType.DOWN, event.getEventType());
        }

        @Test
        @DisplayName("Constructor sets eventSource correctly")
        void constructor_setsEventSource() {
            MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
            assertEquals(EventSource.USER, event.getEventSource());
        }

        @Test
        @DisplayName("Constructor accepts all EventType values")
        void constructor_acceptsAllEventTypes() {
            for (EventType type : EventType.values()) {
                MoveEvent event = new MoveEvent(type, EventSource.USER);
                assertEquals(type, event.getEventType());
            }
        }

        @Test
        @DisplayName("Constructor accepts all EventSource values")
        void constructor_acceptsAllEventSources() {
            for (EventSource source : EventSource.values()) {
                MoveEvent event = new MoveEvent(EventType.LEFT, source);
                assertEquals(source, event.getEventSource());
            }
        }
    }

    @Nested
    @DisplayName("Event Type Tests")
    class EventTypeTests {

        @Test
        @DisplayName("DOWN event type is preserved")
        void downEventType_isPreserved() {
            MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.THREAD);
            assertEquals(EventType.DOWN, event.getEventType());
        }

        @Test
        @DisplayName("LEFT event type is preserved")
        void leftEventType_isPreserved() {
            MoveEvent event = new MoveEvent(EventType.LEFT, EventSource.USER);
            assertEquals(EventType.LEFT, event.getEventType());
        }

        @Test
        @DisplayName("RIGHT event type is preserved")
        void rightEventType_isPreserved() {
            MoveEvent event = new MoveEvent(EventType.RIGHT, EventSource.USER);
            assertEquals(EventType.RIGHT, event.getEventType());
        }

        @Test
        @DisplayName("ROTATE event type is preserved")
        void rotateEventType_isPreserved() {
            MoveEvent event = new MoveEvent(EventType.ROTATE, EventSource.USER);
            assertEquals(EventType.ROTATE, event.getEventType());
        }
    }

    @Nested
    @DisplayName("Event Source Tests")
    class EventSourceTests {

        @Test
        @DisplayName("USER source distinguishes player input")
        void userSource_distinguishesPlayerInput() {
            MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.USER);
            assertEquals(EventSource.USER, event.getEventSource());
        }

        @Test
        @DisplayName("THREAD source distinguishes automatic drops")
        void threadSource_distinguishesAutomaticDrops() {
            MoveEvent event = new MoveEvent(EventType.DOWN, EventSource.THREAD);
            assertEquals(EventSource.THREAD, event.getEventSource());
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("MoveEvent is immutable - getters return same values")
        void moveEvent_isImmutable() {
            MoveEvent event = new MoveEvent(EventType.ROTATE, EventSource.USER);

            EventType type1 = event.getEventType();
            EventType type2 = event.getEventType();
            EventSource source1 = event.getEventSource();
            EventSource source2 = event.getEventSource();

            assertSame(type1, type2);
            assertSame(source1, source2);
        }
    }
}

