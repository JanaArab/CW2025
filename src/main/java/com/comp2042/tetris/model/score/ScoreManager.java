package com.comp2042.tetris.model.score;

import com.comp2042.tetris.model.board.ClearRow;
import com.comp2042.tetris.model.event.GameEventPublisher;
import com.comp2042.tetris.model.event.ScoreChangeEvent;
import com.comp2042.tetris.model.event.MoveEvent;

import java.util.Objects;


public final class ScoreManager {
    private final Score score;
    private final GameEventPublisher eventPublisher;
    private final  ScorePolicy scorePolicy;

    public ScoreManager(Score score, GameEventPublisher eventPublisher,  ScorePolicy scorePolicy)  {
        this.score = Objects.requireNonNull(score, "score");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "eventPublisher");
        this.scorePolicy = Objects.requireNonNull(scorePolicy, "scorePolicy");
    }

    public void publishCurrentScore() {
        eventPublisher.publishScoreChanged(new ScoreChangeEvent(score.getValue()));
    }

        public void handleDrop(MoveEvent event) {
            MoveEvent dropEvent = Objects.requireNonNull(event, "event");
            int dropScore = scorePolicy.calculateDropScore(dropEvent);
            if (dropScore > 0) {
                score.add(dropScore);
                publishCurrentScore();
            }
    }

    public void handleLinesCleared(ClearRow clearRow) {
        if (clearRow.linesRemoved() > 0) {
            score.add(clearRow.scoreBonus());
            publishCurrentScore();
            eventPublisher.publishLinesCleared(clearRow);
        }
    }
    }

