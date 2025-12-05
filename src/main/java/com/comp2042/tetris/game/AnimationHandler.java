package com.comp2042.tetris.game;

import javafx.util.Duration;
import java.util.Objects;

/**
 * Handles the game animation loop and coordinates game state transitions.
 * This class manages the timing of game ticks and delegates state changes
 * to the {@link GameStateManager}.
 *
 * <p>The AnimationHandler is responsible for:</p>
 * <ul>
 *   <li>Starting and stopping the game loop</li>
 *   <li>Managing pause/resume functionality</li>
 *   <li>Adjusting game speed through tick interval changes</li>
 *   <li>Handling game over state transitions</li>
 * </ul>
 *
 * @see GameLoop
 * @see GameStateManager
 */
public class AnimationHandler {
    private final GameStateManager gameStateManager;
    private Duration tickInterval;
    private final Runnable tickAction;
    private GameLoop gameLoop;

    /**
     * Constructs a new AnimationHandler with the specified parameters.
     *
     * @param gameStateManager the manager responsible for game state transitions
     * @param tickInterval the initial interval between game ticks
     * @param tickAction the action to execute on each tick (typically moves the brick down)
     * @throws NullPointerException if any parameter is null
     */
    public AnimationHandler(GameStateManager gameStateManager, Duration tickInterval, Runnable tickAction) {
        this.gameStateManager = Objects.requireNonNull(gameStateManager, "gameStateManager cannot be null");
        this.tickInterval = Objects.requireNonNull(tickInterval, "tickInterval cannot be null");
        this.tickAction = Objects.requireNonNull(tickAction, "tickAction cannot be null");
    }

    /**
     * Ensures the game loop is initialized before starting.
     * Creates a new GameLoop if one doesn't exist.
     */
    public void ensureInitialized() {
        if (gameLoop == null) {
            gameLoop = new GameLoop(tickInterval, tickAction);
        }
    }

    /**
     * Updates the tick interval for the game loop.
     * This allows dynamic speed adjustments during gameplay.
     *
     * @param newInterval the new duration between game ticks
     */
    public void setTickInterval(Duration newInterval) {
        this.tickInterval = newInterval;
        if (gameLoop != null) {
            gameLoop.updateInterval(newInterval);
        }
    }

    /**
     * Starts the game loop and transitions to the running state.
     * Initializes the game loop if not already done.
     */
    public void start() {
        ensureInitialized();
        gameStateManager.onGameStarted(gameLoop);
    }

    /**
     * Toggles the pause state of the game.
     * Has no effect if the game is over or the loop hasn't been initialized.
     */
    public void togglePause() {
        if (gameLoop == null || gameStateManager.isGameOver()) {
            return;
        }
        gameStateManager.togglePause(gameLoop);
    }

    /**
     * Transitions the game to the game over state.
     * Stops the game loop and prevents further gameplay.
     */
    public void onGameOver() {
        gameStateManager.onGameOver(gameLoop);
    }

    /**
     * Checks if the game is currently paused.
     *
     * @return true if the game is paused, false otherwise
     */
    public boolean isPaused() {
        return gameStateManager.isPaused();
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game has ended, false otherwise
     */
    public boolean isGameOver() {
        return gameStateManager.isGameOver();
    }
}
