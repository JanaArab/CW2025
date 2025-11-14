package com.comp2042.tetris.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class GameStateManager {
    private final BooleanProperty paused = new SimpleBooleanProperty(false);
    private final BooleanProperty gameOver = new SimpleBooleanProperty(false);
    private GameState currentState = new RunningState(false);

    public boolean isPaused(){
        return paused.get();
    }

    public boolean isGameOver(){
        return gameOver.get();
}

    public void onGameStarted(GameLoop loop){
        transitionTo(new RunningState(true), loop);
    }

    public void togglePause(GameLoop loop) {
        if (currentState instanceof GameOverState) {
            return;
        }
        transitionTo(currentState.togglePause(), loop);
    }

    public void onGameOver(GameLoop loop) {
        transitionTo(new GameOverState(), loop);
    }

    private void transitionTo(GameState newState, GameLoop loop) {
        currentState = newState;
        currentState.onEnter(this, loop);
    }

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