package com.comp2042.tetris.model.score;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.event.GameEventPublisher;
import com.comp2042.tetris.model.event.ScoreChangeEvent;

import java.util.Objects;


public class ScoreManager {
    private final Score score;
    private final GameEventPublisher eventPublisher;
    private final int dropScore;

    public ScoreManager(Score score, GameEventPublisher eventPublisher, int dropScore) {
        this.score = Objects.requireNonNull(score, "score");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "eventPublisher");
        this.dropScore = dropScore;
    }

    public void publishCurrentScore() {
        eventPublisher.publishScoreChanged(new ScoreChangeEvent(score.getValue()));
    }

    public void handleUserDrop() {
        score.add(dropScore);
        publishCurrentScore();
    }

    public void handleLinesCleared(ClearRow clearRow) {
        if (clearRow.linesRemoved() > 0) {
            score.add(clearRow.scoreBonus());
            publishCurrentScore();
            eventPublisher.publishLinesCleared(clearRow);
        }
    }
}
