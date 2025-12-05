package com.comp2042.tetris.model.event;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;

/**
 * Interface for publishing game events to registered listeners.
 * Implements the Observer pattern to decouple game logic from UI updates.
 *
 * <p>Events published include:</p>
 * <ul>
 *   <li>Game initialization</li>
 *   <li>Score changes</li>
 *   <li>Brick updates (movement, rotation)</li>
 *   <li>Board state changes</li>
 *   <li>Line clears</li>
 *   <li>Game over</li>
 * </ul>
 *
 * @see GameEventListener
 * @see SimpleGameEventBus
 */
public interface GameEventPublisher {

    /**
     * Registers a listener to receive game events.
     *
     * @param listener the listener to register
     */
    void registerListener(GameEventListener listener);

    /**
     * Removes a listener from receiving game events.
     *
     * @param listener the listener to unregister
     */
    void unregisterListener(GameEventListener listener);

    /**
     * Publishes a game initialization event with the initial state.
     *
     * @param snapshot the initial game state
     */
    void publishGameInitialized(GameStateSnapshot snapshot);

    /**
     * Publishes a score change event.
     *
     * @param event the score change details
     */
    void publishScoreChanged(ScoreChangeEvent event);

    /**
     * Publishes a brick update event (position or shape changed).
     *
     * @param viewData the updated view data
     */
    void publishBrickUpdated(ViewData viewData);

    /**
     * Publishes a board state update event.
     *
     * @param boardMatrix the updated board matrix
     */
    void publishBoardUpdated(int[][] boardMatrix);

    /**
     * Publishes a lines cleared event.
     *
     * @param clearRow the details of cleared rows
     */
    void publishLinesCleared(ClearRow clearRow);

    /**
     * Publishes a game over event.
     */
    void publishGameOver();

    /**
     * Publishes a brick placed event.
     *
     * @param event the brick placement event
     */
    void publishBrickPlaced(BrickPlacedEvent event);
}
