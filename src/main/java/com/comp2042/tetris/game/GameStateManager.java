package com.comp2042.tetris.game;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Manages the current state of the game using the State design pattern.
 * This class tracks whether the game is running, paused, or over,
 * and handles transitions between these states.
 *
 * <p>The GameStateManager supports three states:</p>
 * <ul>
 *   <li><b>Running</b>: The game is actively playing</li>
 *   <li><b>Paused</b>: The game is temporarily stopped</li>
 *   <li><b>Game Over</b>: The game has ended</li>
 * </ul>
 *
 * @see AnimationHandler
 * @see GameLoop
 */
public class GameStateManager {
    private final BooleanProperty paused = new SimpleBooleanProperty(false);
    private final BooleanProperty gameOver = new SimpleBooleanProperty(false);
    private GameState currentState = new RunningState(false);

    /**
     * Checks if the game is currently paused.
     *
     * @return true if the game is paused, false otherwise
     */
    public boolean isPaused() {
        return paused.get();
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game has ended, false otherwise
     */
    public boolean isGameOver() {
        return gameOver.get();
    }

    /**
     * Called when the game starts. Transitions to the running state
     * and starts the game loop.
     *
     * @param loop the game loop to start
     */
    public void onGameStarted(GameLoop loop) {
        transitionTo(new RunningState(true), loop);
    }

    /**
     * Toggles between paused and running states.
     * Has no effect if the game is over.
     *
     * @param loop the game loop to pause or resume
     */
    public void togglePause(GameLoop loop) {
        if (currentState instanceof GameOverState) {
            return;
        }
        transitionTo(currentState.togglePause(), loop);
    }

    /**
     * Transitions the game to the game over state.
     * Stops the game loop and marks the game as finished.
     *
     * @param loop the game loop to stop
     */
    public void onGameOver(GameLoop loop) {
        transitionTo(new GameOverState(), loop);
    }

    /**
     * Performs a state transition and triggers the new state's entry behavior.
     *
     * @param newState the state to transition to
     * @param loop the game loop affected by the transition
     */
    private void transitionTo(GameState newState, GameLoop loop) {
        currentState = newState;
        currentState.onEnter(this, loop);
    }

    /**
     * Enters the running state, optionally restarting the game.
     *
     * @param loop the game loop
     * @param restart true to restart from beginning, false to resume
     */
    void enterRunning(GameLoop loop, boolean restart) {
        paused.set(false);
        if (restart) {
            gameOver.set(false);
            if (loop != null) {
                loop.start();
            }
        } else {
            if (loop != null) {
                loop.resume();
            }
        }
    }

    void enterPaused(GameLoop loop) {
        if (loop != null) {
            loop.pause();
        }
        paused.set(true);
    }

    void enterGameOver(GameLoop loop) {
        if (loop != null) {
            loop.stop();
        }
        paused.set(false);
        gameOver.set(true);
    }

    private interface GameState {
        void onEnter(GameStateManager context, GameLoop loop);

        GameState togglePause();
    }

    private static final class RunningState implements GameState {
        private final boolean restart;

        private RunningState(boolean restart) {
            this.restart = restart;
        }

        @Override
        public void onEnter(GameStateManager context, GameLoop loop) {
            context.enterRunning(loop, restart);
        }

        @Override
        public GameState togglePause() {
            return new PausedState();
        }
    }

    private static final class PausedState implements GameState {

        @Override
        public void onEnter(GameStateManager context, GameLoop loop) {
            context.enterPaused(loop);
        }

        @Override
        public GameState togglePause() {
            return new RunningState(false);
        }
    }

    private static final class GameOverState implements GameState {

        @Override
        public void onEnter(GameStateManager context, GameLoop loop) {
            context.enterGameOver(loop);
        }

        @Override
        public GameState togglePause() {
            return this;
        }
    }



    }