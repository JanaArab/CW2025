package com.comp2042.tetris.model.level;
import com.comp2042.tetris.view.UIConstants;
import javafx.util.Duration;
public class Level2 extends ClassicLevel{

    @Override
    public Duration getTickInterval(int currentScore) {
        // Level 2 uses standard speed (same as Classic)
        return UIConstants.DEFAULT_DROP_INTERVAL;
    }

    @Override
    public int getRotationLimit() {
        return 4;
    }

    @Override
    public String getName() {
        return "Level 2";
    }
}
