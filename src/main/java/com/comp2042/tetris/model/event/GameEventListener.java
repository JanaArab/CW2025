package com.comp2042.tetris.model.event;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.data.ViewData;
import javafx.beans.property.IntegerProperty;

/**
 * Interface for receiving game events from the event publisher.
 * Implementations (typically UI controllers) can respond to game state changes.
 *
 * <p>Listeners receive notifications for:</p>
 * <ul>
 *   <li>Game initialization</li>
 *   <li>Score changes</li>
 *   <li>Brick updates</li>
 *   <li>Board updates</li>
 *   <li>Line clears</li>
 *   <li>Game over</li>
 *   <li>Brick placement</li>
 * </ul>
 *
 * @see GameEventPublisher
 */
public interface GameEventListener {

    /**
     * Called when the game is initialized with a new state.
     *
     * @param snapshot the initial game state
     */
    void onGameInitialized(GameStateSnapshot snapshot);

    /**
     * Called when the score changes.
     *
     * @param event the score change event
     */
    void onScoreChanged(ScoreChangeEvent event);

    /**
     * Called when the active brick is updated (moved or rotated).
     *
     * @param viewData the updated view data
     */
    void onBrickUpdated(ViewData viewData);

    /**
     * Called when the board state changes (rows cleared or merged).
     *
     * @param boardMatrix the updated board matrix
     */
    void onBoardUpdated(int[][] boardMatrix);

    /**
     * Called when one or more lines are cleared.
     *
     * @param clearRow the details of cleared rows
     */
    void onLinesCleared(ClearRow clearRow);

    /**
     * Called when the game is over.
     */
    void onGameOver();

    /**
     * Called when a brick is placed on the board.
     *
     * @param event the brick placement event
     */
    void onBrickPlaced(BrickPlacedEvent event);
}
