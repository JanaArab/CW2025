package com.comp2042.tetris.game;

import javafx.util.Duration;
import java.util.Objects;

public class AnimationHandler {
    private final GameStateManager gameStateManager;
    private final Duration tickInterval;
    private final Runnable tickAction;
    private GameLoop gameLoop;

    public AnimationHandler(GameStateManager gameStateManager, Duration tickInterval, Runnable tickAction) {
        this.gameStateManager = Objects.requireNonNull(gameStateManager, "gameStateManager cannot be null");
        this.tickInterval = Objects.requireNonNull(tickInterval, "tickInterval cannot be null");
        this.tickAction = Objects.requireNonNull(tickAction, "tickAction cannot be null");
    }
    public void ensureInitialized() {
        if (gameLoop == null) {
            gameLoop = new GameLoop(tickInterval, tickAction);
        }
    }
    public void start(){
        ensureInitialized();
        gameStateManager.onGameStarted(gameLoop);
    }
    public void togglePause() {
        if (gameLoop == null || gameStateManager.isGameOver()) {
            return;
        }
        gameStateManager.togglePause(gameLoop);
    }

    public void onGameOver() {
        gameStateManager.onGameOver(gameLoop);
    }

    public boolean isPaused() {
        return gameStateManager.isPaused();
    }

    public boolean isGameOver() {
        return gameStateManager.isGameOver();
    }
}
