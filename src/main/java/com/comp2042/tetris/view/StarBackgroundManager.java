package com.comp2042.tetris.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.Random;

/**
 * Example class demonstrating how to integrate ShootingStarAnimator
 * into the Tetris game's background.
 *
 * This is a reference implementation - adapt it to your specific needs.
 */
public class StarBackgroundManager {

    private final ShootingStarAnimator animator;
    private final Pane backgroundPane;
    private final Random random;

    private Timeline ambientStarTimeline;
    private boolean isActive;

    /**
     * Creates a new StarBackgroundManager.
     *
     * @param backgroundPane the pane where stars will be rendered
     */
    public StarBackgroundManager(Pane backgroundPane) {
        this.backgroundPane = backgroundPane;
        this.animator = new ShootingStarAnimator();
        this.random = new Random();
        this.isActive = false;
    }

    /**
     * Starts the ambient star animation with default settings.
     * Stars will appear randomly every 1-5 seconds.
     */
    public void start() {
        start(1.0, 5.0);
    }

    /**
     * Starts the ambient star animation with custom timing.
     *
     * @param minInterval minimum seconds between stars
     * @param maxInterval maximum seconds between stars
     */
    public void start(double minInterval, double maxInterval) {
        if (isActive) {
            return; // Already running
        }

        isActive = true;

        // Create a timeline that checks every second if a star should appear
        ambientStarTimeline = new Timeline(new KeyFrame(
            Duration.seconds(1),
            event -> {
                double intervalRange = maxInterval - minInterval;
                double threshold = 1.0 / (minInterval + random.nextDouble() * intervalRange);

                if (random.nextDouble() < threshold) {
                    createAmbientStar();
                }
            }
        ));

        ambientStarTimeline.setCycleCount(Timeline.INDEFINITE);
        ambientStarTimeline.play();
    }

    /**
     * Stops the ambient star animation.
     * Existing stars will complete their animation.
     */
    public void stop() {
        if (ambientStarTimeline != null) {
            ambientStarTimeline.stop();
            isActive = false;
        }
    }

    /**
     * Pauses the ambient star animation.
     * Can be resumed with resume().
     */
    public void pause() {
        if (ambientStarTimeline != null && isActive) {
            ambientStarTimeline.pause();
        }
    }

    /**
     * Resumes the ambient star animation after pause.
     */
    public void resume() {
        if (ambientStarTimeline != null && isActive) {
            ambientStarTimeline.play();
        }
    }

    /**
     * Creates a single ambient shooting star with random size variation.
     */
    private void createAmbientStar() {
        // Random size variation (80-120% of default)
        double sizeMultiplier = 0.8 + random.nextDouble() * 0.4;
        double width = 100 * sizeMultiplier;
        double height = 50 * sizeMultiplier;

        animator.createAndAnimateStar(backgroundPane, width, height);
    }

    /**
     * Triggers a celebration effect with multiple shooting stars.
     * Useful for special events like clearing 4 lines (Tetris), level up, etc.
     *
     * @param intensity the number of stars (recommended: 5-15)
     */
    public void celebrate(int intensity) {
        animator.createStarBurst(backgroundPane, intensity, 2.0);
    }

    /**
     * Triggers a small celebration effect.
     * Good for minor achievements (clearing 2-3 lines, combos, etc.)
     */
    public void celebrateSmall() {
        celebrate(3);
    }

    /**
     * Triggers a medium celebration effect.
     * Good for clearing 4 lines (Tetris) or similar achievements.
     */
    public void celebrateMedium() {
        celebrate(7);
    }

    /**
     * Triggers a large celebration effect.
     * Good for level ups, high scores, or game completion.
     */
    public void celebrateLarge() {
        celebrate(12);
    }

    /**
     * Creates a single large shooting star.
     * Useful as a subtle visual cue for important events.
     */
    public void createSpecialStar() {
        animator.createAndAnimateStar(backgroundPane, 150, 75);
    }

    /**
     * Checks if the star animation is currently active.
     *
     * @return true if stars are being generated
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Gets the underlying animator for direct access if needed.
     *
     * @return the ShootingStarAnimator instance
     */
    public ShootingStarAnimator getAnimator() {
        return animator;
    }
}

