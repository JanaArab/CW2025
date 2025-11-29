package com.comp2042.tetris.model.level;
import javafx.util.Duration;
public abstract class DynamicLevel implements GameLevel {
    private final double baseDropsPerSecond;
    private final double speedIncrement;
    private final int scoreThreshold;
    private final int maxScoreForSpeedUp;

    protected DynamicLevel(double baseDropsPerSecond, double speedIncrement, int scoreThreshold, int maxScoreForSpeedUp) {
        this.baseDropsPerSecond = baseDropsPerSecond;
        this.speedIncrement = speedIncrement;
        this.scoreThreshold = scoreThreshold;
        this.maxScoreForSpeedUp = maxScoreForSpeedUp;
    }
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
