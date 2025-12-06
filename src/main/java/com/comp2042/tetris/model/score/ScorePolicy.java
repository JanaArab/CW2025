package com.comp2042.tetris.model.score;

import com.comp2042.tetris.model.event.MoveEvent;

/**
 * Interface for calculating score values based on game events.
 * Different implementations can provide different scoring rules.
 *
 * @see DefaultScorePolicy
 * @see ScoreManager
 */
public interface ScorePolicy {

    /**
     * Calculates the score for a drop event.
     *
     * @param event the move event containing drop information
     * @return the score points to award
     */
    int calculateDropScore(MoveEvent event);

}
