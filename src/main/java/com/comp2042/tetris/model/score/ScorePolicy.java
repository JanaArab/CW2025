package com.comp2042.tetris.model.score;

import com.comp2042.tetris.model.event.MoveEvent;

public interface ScorePolicy {
    int calculateDropScore(MoveEvent event);

}
