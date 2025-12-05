package com.comp2042.tetris.model.event;

import com.comp2042.tetris.model.data.ViewData;

/**
 * Snapshot of the complete game state at a point in time.
 * Used to initialize or synchronize the game view with the model.
 *
 * @param boardMatrix the current state of the game board
 * @param viewData the current brick and preview information
 * @see GameEventPublisher#publishGameInitialized(GameStateSnapshot)
 * @see GameEventListener#onGameInitialized(GameStateSnapshot)
 */
public record GameStateSnapshot(int[][] boardMatrix, ViewData viewData) {
}
