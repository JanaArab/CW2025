package com.comp2042.tetris.model.level;
import com.comp2042.tetris.view.UIConstants;
import javafx.util.Duration;

public class ClassicLevel implements GameLevel{
    @Override
    public Duration getTickInterval(int currentScore) {
        // Classic level keeps constant speed
        return UIConstants.DEFAULT_DROP_INTERVAL;
    }

    @Override
    public String getName() {
        return "Classic";
    }
}
