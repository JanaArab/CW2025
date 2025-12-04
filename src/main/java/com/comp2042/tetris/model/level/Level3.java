package com.comp2042.tetris.model.level;

import com.comp2042.tetris.view.UIConstants;
import javafx.util.Duration;

public class Level3 extends ClassicLevel {

    @Override
    public String getName() {
        return "Level 3";
    }

    @Override
    public Duration getTickInterval(int currentScore) {
        // Keeps standard speed, difficulty comes from garbage/flicker
        return UIConstants.DEFAULT_DROP_INTERVAL;
    }

    @Override
    public boolean isGarbageEnabled() {
        return true;
    }

    @Override
    public boolean isFlickerEnabled() {
        return true;
    }

    @Override
    public boolean isNextBrickHidden() {
        return true;
    }
}
