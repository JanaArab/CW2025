package com.comp2042.tetris.model.score;
import com.comp2042.tetris.model.event.EventSource;
import com.comp2042.tetris.model.event.MoveEvent;

    public class DefaultScorePolicy implements ScorePolicy {
        private static final int USER_DROP_SCORE = 1;

        @Override
        public int calculateDropScore(MoveEvent event) {
            return event.getEventSource() == EventSource.USER ? USER_DROP_SCORE : 0;
        }
    }
