package com.comp2042.tetris.model.score;
import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.event.MoveEvent;

/**
 * Default implementation of {@link ScorePolicy}.
 * Awards points only for user-initiated soft drops.
 *
 * <p>Scoring rules:</p>
 * <ul>
 *   <li>User soft drop: 1 point per cell</li>
 *   <li>Automatic drops: 0 points</li>
 * </ul>
 *
 * @see ScorePolicy
 * @see ScoreManager
 */
public class DefaultScorePolicy implements ScorePolicy {
    private static final int USER_DROP_SCORE = 1;

    /**
     * {@inheritDoc}
     */
    @Override
    public int calculateDropScore(MoveEvent event) {
        return event.getEventSource() == EventSource.USER ? USER_DROP_SCORE : 0;
    }
}
