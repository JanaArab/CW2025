package com.comp2042.tetris.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateManagerTest {

    @Test
    void onGameStartedClearsGameOverAndUnpauses() {
        GameStateManager manager = new GameStateManager();
        manager.onGameOver(null);
        assertTrue(manager.isGameOver());
        assertFalse(manager.isPaused());

        manager.onGameStarted(null);

        assertFalse(manager.isGameOver());
        assertFalse(manager.isPaused());
    }

    @Test
    void togglePauseTogglesBetweenPausedAndRunning() {
        GameStateManager manager = new GameStateManager();

        manager.togglePause(null);
        assertTrue(manager.isPaused());
        assertFalse(manager.isGameOver());

        manager.togglePause(null);
        assertFalse(manager.isPaused());
        assertFalse(manager.isGameOver());
    }

    @Test
    void togglePauseIgnoredWhenGameOver() {
        GameStateManager manager = new GameStateManager();
        manager.onGameOver(null);
        assertTrue(manager.isGameOver());
        assertFalse(manager.isPaused());

        manager.togglePause(null);

        assertTrue(manager.isGameOver());
        assertFalse(manager.isPaused());
    }

    @Test
    void onGameOverSetsFlagsAppropriately() {
        GameStateManager manager = new GameStateManager();
        manager.togglePause(null);
        assertTrue(manager.isPaused());

        manager.onGameOver(null);

        assertTrue(manager.isGameOver());
        assertFalse(manager.isPaused());
    }
}

