package com.comp2042.tetris.model.level;

import javafx.util.Duration;

/**
 * Abstract base class for levels with dynamic speed that increases with score.
 * The drop speed increases as the player's score reaches certain thresholds,
 * providing progressive difficulty.
 *
 * <p>Speed calculation: The tick interval decreases (speed increases) based on
 * the formula: frequency = baseSpeed + (scoreThresholdsPassed × speedIncrement)</p>
 *
 * @see GameLevel
 * @see Level1
 */
public abstract class DynamicLevel implements GameLevel {
    private final double baseDropsPerSecond;
    private final double speedIncrement;
    private final int scoreThreshold;
    private final int maxScoreForSpeedUp;

    /**
     * Constructs a DynamicLevel with speed configuration.
     *
     * @param baseDropsPerSecond the initial drop frequency (drops per second)
     * @param speedIncrement the frequency increase per threshold
     * @param scoreThreshold points needed to trigger each speed increase
     * @param maxScoreForSpeedUp score cap for speed increases
     */
    protected DynamicLevel(double baseDropsPerSecond, double speedIncrement, int scoreThreshold, int maxScoreForSpeedUp) {
        this.baseDropsPerSecond = baseDropsPerSecond;
        this.speedIncrement = speedIncrement;
        this.scoreThreshold = scoreThreshold;
        this.maxScoreForSpeedUp = maxScoreForSpeedUp;
    }

    /**
     * {@inheritDoc}
     * Calculates tick interval based on current score with progressive speed increase.
     */
    @Override
    public Duration getTickInterval(int currentScore) {
        // Cap the score used for calculation
        int effectiveScore = Math.min(currentScore, maxScoreForSpeedUp);

        // Calculate how many thresholds have been passed
        int increments = effectiveScore / scoreThreshold;

        // Calculate new speed (Frequency = Base + (Increments * Step))
        double currentDropsPerSecond = baseDropsPerSecond + (increments * speedIncrement);

        // Convert Frequency (Hz) to Period (Duration)
        // Interval (ms) = 1000 / Frequency
        return Duration.millis(1000.0 / currentDropsPerSecond);
    }
}
