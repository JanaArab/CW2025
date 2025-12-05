/**
 * Represents a movement or rotation event in the game.
 * Contains information about the type of action and its source.
 *
 * @see EventType
 * @see EventSource
 */

package com.comp2042.tetris.model.event;

public final class MoveEvent {
    private final EventType eventType;
    private final EventSource eventSource;

    /**
     * Constructs a MoveEvent with the specified type and source.
     *
     * @param eventType the type of movement (DOWN, LEFT, RIGHT, ROTATE)
     * @param eventSource the source of the event (USER or SYSTEM)
     */
    public MoveEvent(EventType eventType, EventSource eventSource) {
        this.eventType = eventType;
        this.eventSource = eventSource;
    }

    /**
     * Gets the type of this movement event.
     *
     * @return the event type
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Gets the source of this event.
     *
     * @return the event source
     */
    public EventSource getEventSource() {
        return eventSource;
    }
}
