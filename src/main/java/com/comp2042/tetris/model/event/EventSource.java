package com.comp2042.tetris.model.event;

/**
 * Enumeration of event sources indicating who triggered the event.
 *
 * <p>Event sources:</p>
 * <ul>
 *   <li>{@link #USER} - Event triggered by user input (keyboard)</li>
 *   <li>{@link #THREAD} - Event triggered by the game loop timer</li>
 * </ul>
 *
 * @see MoveEvent
 */
public enum EventSource {
    /** Event triggered by user keyboard input. */
    USER,
    /** Event triggered by the automatic game loop. */
    THREAD
}
