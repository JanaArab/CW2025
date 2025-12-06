package com.comp2042.tetris.controller.core;

import com.comp2042.tetris.model.event.EventSource;

/**
 * Interface for invoking game actions from commands.
 * Provides methods for all basic game movements and actions.
 *
 * @see InputHandler
 * @see com.comp2042.tetris.controller.command.GameCommand
 */
public interface GameActionInvoker {

    /**
     * Moves the brick down with a specified event source.
     *
     * @param source the source of the move event (USER or THREAD)
     */
    void moveDown(EventSource source);

    /**
     * Moves the brick down using the default event source.
     */
    void moveDown();

    /**
     * Moves the brick one cell to the left.
     */
    void moveLeft();

    /**
     * Moves the brick one cell to the right.
     */
    void moveRight();

    /**
     * Rotates the brick counterclockwise.
     */
    void rotate();

    /**
     * Instantly drops the brick to the bottom (hard drop).
     */
    void instantDrop();
}
