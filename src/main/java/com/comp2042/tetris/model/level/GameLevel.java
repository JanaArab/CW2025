package com.comp2042.tetris.model.level;

import javafx.util.Duration;
public interface GameLevel {
    Duration getTickInterval(int currentScore);
    String getName();
    default int getRotationLimit() {
        return -1;
    }
}
