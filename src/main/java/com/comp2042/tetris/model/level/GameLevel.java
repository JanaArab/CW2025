package com.comp2042.tetris.model.level;

import javafx.util.Duration;
public interface GameLevel {
    Duration getTickInterval(int currentScore);
    String getName();
    default int getRotationLimit() {
        return -1;
    }
    default boolean isGarbageEnabled() {
        return false;
    }

    default boolean isFlickerEnabled() {
        return false;
    }

    default boolean isNextBrickHidden() {
        return false;
    }
}
