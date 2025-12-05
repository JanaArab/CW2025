package com.comp2042.tetris.model.score;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.event.GameEventPublisher;
import com.comp2042.tetris.model.event.ScoreChangeEvent;
import com.comp2042.tetris.model.event.MoveEvent;

import java.util.Objects;

/**
 * Manages score calculations and publishes score change events.
 * This class coordinates between the Score model, the scoring policy,
 * and the event system to handle all score-related operations.
 *
 * <p>Score points are awarded for:</p>
 * <ul>
 *   <li>Soft dropping bricks (moving down manually)</li>
 *   <li>Clearing lines (with bonuses for multiple lines)</li>
 * </ul>
 *
 * @see Score
 * @see ScorePolicy
 * @see GameEventPublisher
 */
public final class ScoreManager {
    private final Score score;
    private final GameEventPublisher eventPublisher;
    private final ScorePolicy scorePolicy;

    /**
     * Constructs a ScoreManager with the given dependencies.
     *
     * @param score the score model to update
     * @param eventPublisher the publisher for score change events
     * @param scorePolicy the policy for calculating score values
     * @throws NullPointerException if any parameter is null
     */
    public ScoreManager(Score score, GameEventPublisher eventPublisher, ScorePolicy scorePolicy) {
        this.score = Objects.requireNonNull(score, "score");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "eventPublisher");
        this.scorePolicy = Objects.requireNonNull(scorePolicy, "scorePolicy");
    }

    /**
     * Publishes the current score to all listeners.
     */
    public void publishCurrentScore() {
        eventPublisher.publishScoreChanged(new ScoreChangeEvent(score.getValue()));
    }

    /**
     * Handles score calculation for a drop event (soft drop).
     *
     * @param event the move event containing drop information
     * @throws NullPointerException if event is null
     */
    public void handleDrop(MoveEvent event) {
        MoveEvent dropEvent = Objects.requireNonNull(event, "event");
        int dropScore = scorePolicy.calculateDropScore(dropEvent);
        if (dropScore > 0) {
            score.add(dropScore);
            publishCurrentScore();
        }
    }

    /**
     * Handles score calculation when lines are cleared.
     *
     * @param clearRow the result of the row clearing operation
     */
    public void handleLinesCleared(ClearRow clearRow) {
        if (clearRow.linesRemoved() > 0) {
            score.add(clearRow.scoreBonus());
            publishCurrentScore();
            eventPublisher.publishLinesCleared(clearRow);
        }
    }
}
