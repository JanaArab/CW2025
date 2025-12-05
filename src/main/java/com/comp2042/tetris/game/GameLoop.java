package com.comp2042.tetris.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Manages the main game loop using JavaFX Timeline animation.
 * The GameLoop executes a tick action at regular intervals to update
 * the game state (typically moving the falling brick down).
 *
 * <p>This class provides methods to:</p>
 * <ul>
 *   <li>Start, pause, resume, and stop the game loop</li>
 *   <li>Dynamically update the tick interval for speed changes</li>
 * </ul>
 *
 * @see AnimationHandler
 */
public class GameLoop {
    private final Runnable tickAction;
    private final Timeline timeline;

    /**
     * Constructs a new GameLoop with the specified tick interval and action.
     *
     * @param interval the duration between each tick
     * @param tickAction the action to execute on each tick
     * @throws NullPointerException if tickAction is null
     */
    public GameLoop(Duration interval, Runnable tickAction) {
        this.tickAction = Objects.requireNonNull(tickAction, "tickAction cannot be null");
        this.timeline = new Timeline(new KeyFrame(interval, event -> this.tickAction.run()));
        this.timeline.setCycleCount(Animation.INDEFINITE);
    }

    /**
     * Starts the game loop from the beginning.
     * If the loop was previously running, it restarts from time zero.
     */
    public void start() {
        timeline.playFromStart();
    }

    /**
     * Resumes the game loop from where it was paused.
     */
    public void resume() {
        timeline.play();
    }

    /**
     * Pauses the game loop without resetting the timeline.
     */
    public void pause() {
        timeline.pause();
    }

    /**
     * Stops the game loop completely.
     */
    public void stop() {
        timeline.stop();
    }

    /**
     * Updates the tick interval while preserving the running state.
     * If the loop was running, it continues running at the new speed.
     *
     * @param newInterval the new duration between ticks
     */
    public void updateInterval(Duration newInterval) {
        boolean running = timeline.getStatus() == Animation.Status.RUNNING;
        timeline.stop();
        timeline.getKeyFrames().setAll(new KeyFrame(newInterval, event -> this.tickAction.run()));
        timeline.setCycleCount(Animation.INDEFINITE);
        if (running) {
            timeline.play();
        }
    }
}
