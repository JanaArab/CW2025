package com.comp2042.tetris.game;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * GameTimer manages the elapsed time display for the game.
 * It starts when the game starts, pauses when the game is paused,
 * resumes when the game resumes, and stops when the game is over.
 */
public class GameTimer {
    private final Label timerLabel;
    private AnimationTimer animationTimer;
    private long startTime;
    private long pausedTime;
    private long totalPausedDuration;
    private boolean isRunning;
    private boolean isPaused;

    public GameTimer(Label timerLabel) {
        this.timerLabel = timerLabel;
        this.isRunning = false;
        this.isPaused = false;
        this.totalPausedDuration = 0;
    }

    /**
     * Start the timer from zero
     */
    public void start() {
        if (isRunning) {
            stop();
        }

        startTime = System.nanoTime();
        totalPausedDuration = 0;
        isRunning = true;
        isPaused = false;

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused) {
                    long elapsedNanos = now - startTime - totalPausedDuration;
                    long elapsedSeconds = elapsedNanos / 1_000_000_000;
                    updateTimerDisplay(elapsedSeconds);
                }
            }
        };
        animationTimer.start();
    }

    /**
     * Pause the timer
     */
    public void pause() {
        if (isRunning && !isPaused) {
            isPaused = true;
            pausedTime = System.nanoTime();
        }
    }

    /**
     * Resume the timer from where it was paused
     */
    public void resume() {
        if (isRunning && isPaused) {
            long pauseDuration = System.nanoTime() - pausedTime;
            totalPausedDuration += pauseDuration;
            isPaused = false;
        }
    }

    /**
     * Stop the timer completely
     */
    public void stop() {
        if (animationTimer != null) {
            animationTimer.stop();
            animationTimer = null;
        }
        isRunning = false;
        isPaused = false;
    }

    /**
     * Reset the timer display to 00:00
     */
    public void reset() {
        stop();
        Platform.runLater(() -> {
            if (timerLabel != null) {
                timerLabel.setText("00:00");
            }
        });
    }

    /**
     * Update the timer label with formatted time
     * @param totalSeconds total elapsed seconds
     */
    private void updateTimerDisplay(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        String timeText = String.format("%02d:%02d", minutes, seconds);

        Platform.runLater(() -> {
            if (timerLabel != null) {
                timerLabel.setText(timeText);
            }
        });
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isPaused() {
        return isPaused;
    }
}

