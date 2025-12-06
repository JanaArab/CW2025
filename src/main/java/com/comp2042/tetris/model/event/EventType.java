package com.comp2042.tetris.model.event;

/**
 * Enumeration of movement event types in the game.
 *
 * <p>Movement types:</p>
 * <ul>
 *   <li>{@link #DOWN} - Move brick down one cell</li>
 *   <li>{@link #LEFT} - Move brick left one cell</li>
 *   <li>{@link #RIGHT} - Move brick right one cell</li>
 *   <li>{@link #ROTATE} - Rotate brick counterclockwise</li>
 * </ul>
 *
 * @see MoveEvent
 */
public enum EventType {
    /** Move the brick down one cell. */
    DOWN,
    /** Move the brick left one cell. */
    LEFT,
    /** Move the brick right one cell. */
    RIGHT,
    /** Rotate the brick counterclockwise. */
    ROTATE
}
