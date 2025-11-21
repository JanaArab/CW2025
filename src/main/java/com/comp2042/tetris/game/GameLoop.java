package com.comp2042.tetris.game;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Objects;

public class GameLoop {
    private final Runnable tickAction;
    private final Timeline timeline;

    public GameLoop(Duration interval, Runnable tickAction) {
        this.tickAction = Objects.requireNonNull(tickAction, "tickAction cannot be null");
        this.timeline = new Timeline(new KeyFrame(interval, event -> this.tickAction.run()));
        this.timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void start() {
        timeline.playFromStart();
    }

    public void resume() {
        timeline.play();
    }

    public void pause() {
        timeline.pause();
    }

    public void stop() {
        timeline.stop();
    }

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
