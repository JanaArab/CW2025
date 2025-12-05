/**
 * connection point between user and programme especially when it
 * comes to keys
 */

package com.comp2042.tetris.controller.core;

import com.comp2042.tetris.model.event.MoveEvent;

/**
 * Interface for handling game input events.
 * Defines the contract for responding to player actions.
 *
 * <p>Input events include:</p>
 * <ul>
 *   <li>Move down (soft drop)</li>
 *   <li>Move left</li>
 *   <li>Move right</li>
 *   <li>Rotate</li>
 *   <li>Instant drop (hard drop)</li>
 *   <li>New game</li>
 * </ul>
 *
 * @see GameController
 * @see IGameController
 */
public interface InputEventListener {

    /**
     * Handles a down movement event (soft drop).
     *
     * @param event the move event details
     */
    void onDownEvent(MoveEvent event);

    /**
     * Handles a left movement event.
     *
     * @param event the move event details
     */
    void onLeftEvent(MoveEvent event);

    /**
     * Handles a right movement event.
     *
     * @param event the move event details
     */
    void onRightEvent(MoveEvent event);

    /**
     * Handles a rotation event.
     *
     * @param event the move event details
     */
    void onRotateEvent(MoveEvent event);

    /**
     * Handles an instant drop (hard drop) event.
     *
     * @param event the move event details
     */
    void onInstantDropEvent(MoveEvent event);

    /**
     * Creates and starts a new game.
     */
    void createNewGame();
}
